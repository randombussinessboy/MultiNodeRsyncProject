package com.zhaoyanyang.dfss.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.sym.Name;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import com.zhaoyanyang.dfss.mapper.FilechangeinfomationMapper;
import com.zhaoyanyang.dfss.pojo.Destination;
import com.zhaoyanyang.dfss.pojo.Filechangeinfomation;
import com.zhaoyanyang.dfss.pojo.Task;

import cn.hutool.core.util.RandomUtil;

/**
 * 从消息队列中主动拉取消息后,进行分析并开始调用 Rsync算法开始同步任务,消息取出来的时候可能需要存入日志,等待前端可视化分析。
 * 
 * @author yangzy
 *
 */
@Service
public class StartSyncService {
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	TaskDeliverService taskDeliverService;
	@Autowired
	TaskQueueService taskQueueService;
	@Autowired
	TaskStateQueryService taskStateQueryService;
	@Autowired
	TaskSynInformationService taskSynInformationService;
	
	@Autowired
    FilechangeinfomationMapper filechangeinfomationMapper;


	@Value("${rabbitmq.hostName}")
	String rabbitmqhost;

	/**
	 * 处理消息队列中存在的文件修改消息 可视化分析需要再此留下接口
	 * 
	 * @param task
	 * @throws Exception
	 */
	@Async
	public void pollMessageAndSync(Task task) throws Exception {

		String taskId = task.getTaskId();
		System.out.println(Thread.currentThread().getName());
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 设置RabbitMQ地址
		factory.setHost(rabbitmqhost);
		// 创建一个新的连接
		Connection connection;
		connection = factory.newConnection();

		// 创建一个通道
		Channel channel = connection.createChannel();
		// 声明要关注的队列
		channel.queueDeclare(task.getQueueName(), false, false, true, null);
		System.out.println(taskId + " 等待接受消息");

		DeclareOk declareOk = channel.queueDeclarePassive(task.getQueueName());
		int num = declareOk.getMessageCount();
		int i = 1;

		while (i <= num) {

			GetResponse getResponse = channel.basicGet(task.getQueueName(), true);
			String message = new String(getResponse.getBody());
			System.out.println(message);
			Filechangeinfomation tmp=new Filechangeinfomation();
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());
		    tmp.setMsg(formatter.format(date)+"  "+message);
			filechangeinfomationMapper.add(tmp);
			i++;

		}

		/**
		 * 这里根据文件修改信息开始进行同步任务，没有修改的话是不需要同步的
		 * 单文件删除 改名都没有考虑 之后需要考虑 改名没有什么影响 但是删除可能会造成出错
		 */
		if (num > 0) {
			
			long totalMilliSeconds = System.currentTimeMillis();
		    long currentTime = totalMilliSeconds;//单位为秒
		    task.setLastTimeStamp(currentTime);

			List<Destination> destinations = task.getDestinations();
			Iterator<Destination> iterator = destinations.iterator();
			while (iterator.hasNext()) {
				Destination destination = iterator.next();

				String url = String.format("%s//startSync", destination.getUrl());
				System.out.println(url);
				/*
				 * 这一轮次需要的设置的同步客户端目录和文件名
				 */
				task.setTargetDirectoryName(destination.getDirectoryName());
				task.setTargetFileName(destination.getFileName());

				String res = restTemplate.postForObject(url, task, String.class);
				System.out.println(res);

				// taskStateQueryService.setTaskState(taskId, Task.complete_send_to_client);
				// System.out.println(taskId + "已将一个轮次的同步请求发送到同步客户端");

				String info = taskId + "已经将同步请求发送到主机" + destination.getUrl();

				taskSynInformationService.sendSyncProcessInfomation(taskId, info);

			}

		}
		System.out.println(taskId + " 接受完毕消息");
		channel.close();
		connection.close();

	}

	/**
	 * 目录任务的同步
	 * 
	 * @param task
	 * @throws Exception
	 */
	@Async
	public void pollCatogoryMessageAndSync(Task task) throws Exception {

		String taskId = task.getTaskId();
		System.out.println(Thread.currentThread().getName());
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 设置RabbitMQ地址
		factory.setHost(rabbitmqhost);
		// 创建一个新的连接
		Connection connection;
		connection = factory.newConnection();

		// 创建一个通道
		Channel channel = connection.createChannel();
		// 声明要关注的队列
		channel.queueDeclare(task.getQueueName(), false, false, true, null);
		System.out.println(taskId + " 等待接受消息");

		// 这里应该是哪个子文件改变了,就同步哪个子文件
		// 发来的消息集合 任务本身的子文件集合 做个匹配

		DeclareOk declareOk = channel.queueDeclarePassive(task.getQueueName());
		int num = declareOk.getMessageCount();
		int i = 1;

		Map<String, String> resMap = new HashMap<>();

		while (i <= num) {

			GetResponse getResponse = channel.basicGet(task.getQueueName(), true);
			String message = new String(getResponse.getBody());

			// 一次修改多次modified 所以要用Map过滤一下
			// 其它的新建文件夹和删除文件夹也可以在这里响应
			
			Filechangeinfomation tmp=new Filechangeinfomation();
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());
		    tmp.setMsg(formatter.format(date)+"  "+message);
			filechangeinfomationMapper.add(tmp);
			
			
			
			

			if (message.contains("deleted")) {

				// 目录响应删除事件

				List<Destination> destinations = task.getDestinations();
				Iterator<Destination> iterator = destinations.iterator();
				while (iterator.hasNext()) {
					Destination destination = iterator.next();
					String url = String.format("%s//deleteFile", destination.getUrl());
					System.out.println(url);
					//被删除的文件
					String filepath=message.split(" ")[1];
					filepath=destination.getDirectoryName()+File.separator+filepath.replace(task.getDirectoryName()+File.separator, "");
					MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
					param.add("filePath",filepath);
					
					String string = restTemplate.postForObject(url, param, String.class);
					System.out.println(string);
					
					

					String info = "目录同步任务" + taskId + "已经将主机:" + destination.getUrl()+filepath+"删除";

					taskSynInformationService.sendSyncProcessInfomation(taskId, info);

				}
				i++;
				continue;

			}
			
			
			if (message.contains("created")) {

				// 目录响应新建事件

				List<Destination> destinations = task.getDestinations();
				Iterator<Destination> iterator = destinations.iterator();
				while (iterator.hasNext()) {
					Destination destination = iterator.next();
					String url = String.format("%s//createFile", destination.getUrl());
					System.out.println(url);
					//新建的文件
					String filepath=message.split(" ")[1];
					filepath=destination.getDirectoryName()+File.separator+filepath.replace(task.getDirectoryName()+File.separator, "");
					int type=Integer.valueOf(message.split(" ")[2]);
					
					MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
					param.add("filePath",filepath);
					param.add("type", type);
					String string = restTemplate.postForObject(url, param, String.class);
					System.out.println(string);
					
					

					String info = "目录同步任务" + taskId + "已经在主机:" + destination.getUrl()+filepath+"创建文件";

					taskSynInformationService.sendSyncProcessInfomation(taskId, info);

				}
				i++;
				continue;

			}
			
			if (message.contains("renamed")) {

				// 目录响应改名事件

				List<Destination> destinations = task.getDestinations();
				Iterator<Destination> iterator = destinations.iterator();
				while (iterator.hasNext()) {
					Destination destination = iterator.next();
					String url = String.format("%s//renamedFile", destination.getUrl());
					System.out.println(url);
//				被改名的文件
//					String filepath=message.split(" ")[1];
//					filepath=destination.getDirectoryName()+File.separator+filepath.replace(task.getDirectoryName()+File.separator, "");
//					int type=Integer.valueOf(message.split(" ")[2]);
					
					String oldfilePath=message.split(" ")[1];
					String newfilePath=message.split(" ")[3];
					
					
					MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
					param.add("root",destination.getDirectoryName());
					param.add("oldPath",oldfilePath);
					param.add("newPath", newfilePath);
					String string = restTemplate.postForObject(url, param, String.class);
					System.out.println(string);
					
					

					String info = "目录同步任务" + taskId + "已经在主机:" + destination.getUrl()+oldfilePath+"改名文件";

					taskSynInformationService.sendSyncProcessInfomation(taskId, info);

				}
				i++;
				continue;

			}
			
			

			System.out.println(message.split(":")[message.split(":").length-1]);

			resMap.put(message.split(":")[message.split(":").length-1], "");

			System.out.println(message);
			i++;

		}

		Set<String> xiaoxi = resMap.keySet();
		System.out.println(xiaoxi);

		if (!xiaoxi.isEmpty()) {
			
			long totalMilliSeconds = System.currentTimeMillis();
		    long currentTime = totalMilliSeconds / 1000;//单位为秒
		    task.setLastTimeStamp(currentTime);

			List<Destination> destinations = task.getDestinations();
			Iterator<Destination> iterator = destinations.iterator();
			while (iterator.hasNext()) {
				Destination destination = iterator.next();
				String url = String.format("%s//startSync", destination.getUrl());
				System.out.println(url);

				Iterator<String> fileIterator = xiaoxi.iterator();
				while (fileIterator.hasNext()) {

					task.setTargetDirectoryName(destination.getDirectoryName());
					String fileName = fileIterator.next();
					task.setTargetFileName(fileName);
					task.setFileName(fileName);
					String res = restTemplate.postForObject(url, task, String.class);
					System.out.println(res);

				}

				String info = "目录同步" + taskId + "已经将目录下的所有同步请求发送到主机" + destination.getUrl();

				taskSynInformationService.sendSyncProcessInfomation(taskId, info);

			}

			// taskStateQueryService.setTaskState(taskId, Task.complete_send_to_client);
			// System.out.println(taskId + "目录同步已将一个轮次的同步请求发送到所有同步客户端");

		}

		/**
		 * 这里根据文件修改信息开始进行同步任务，没有修改的话是不需要同步的
		 */
		// if (num > 0) {
		//
		// List<Destination> destinations = task.getDestinations();
		// Iterator<Destination> iterator = destinations.iterator();
		// while (iterator.hasNext()) {
		// Destination destination = iterator.next();
		//
		// String url = String.format("%s//startSync", destination.getUrl());
		// System.out.println(url);
		// /*
		// * 这一轮次需要的设置的同步客户端目录和文件名
		// */
		// task.setTargetDirectoryName(destination.getDirectoryName());
		// task.setTargetFileName(destination.getFileName());
		//
		// String res = restTemplate.postForObject(url, task, String.class);
		// System.out.println(res);
		// }
		//
		// }
		System.out.println(taskId + " 接受完毕消息");
		channel.close();
		connection.close();

	}

	public void testSystem() {

		// Task task = new Task();
		// task.setDirectoryName("E:\\毕业设计项目代码\\SlaveNodeDemo\\slaveNode1");
		// task.setFileName("task1-server.txt");
		// task.setTargetType(Task.FILE_TASK);
		// task.setQueueName("testPosess");
		// task.setOwnHost("localhost:8011");
		// String taskId1="任务1";
		// task.setTaskId(taskId1);
		// task.setRunning(true);
		// task.setTaskType(Task.REAL_TIME_TASK);
		// List<Destination> destinations=task.getDestinations();
		// Destination destination=new Destination();
		// destination.setUrl("localhost:8012");
		// destination.setDirectoryName("E:\\毕业设计项目代码\\SlaveNodeDemo\\slaveNode2");
		// destination.setFileName("task1-client.txt");
		// destinations.add(destination);
		//
		//
		// Task task2 = new Task();
		// task2.setDirectoryName("E:\\毕业设计项目代码\\SlaveNodeDemo\\slaveNode2");
		// task2.setFileName("task2-server.txt");
		// task2.setTargetType(Task.FILE_TASK);
		// task2.setQueueName("testPosess2");
		// task2.setOwnHost("localhost:8012");
		// String taskId2="任务2";
		// task2.setTaskId(taskId2);
		// task2.setRunning(true);
		// task2.setTaskType(Task.REAL_TIME_TASK);
		// List<Destination> destinations2=task2.getDestinations();
		// Destination destination2=new Destination();
		// destination2.setUrl("localhost:8011");
		// destination2.setDirectoryName("E:\\毕业设计项目代码\\SlaveNodeDemo\\slaveNode1");
		// destination2.setFileName("task2-client.txt");
		// Destination destination21=new Destination();
		// destination21.setUrl("localhost:8013");
		// destination21.setDirectoryName("E:\\毕业设计项目代码\\SlaveNodeDemo\\slaveNode3");
		// destination21.setFileName("task2-client.txt");
		// destinations2.add(destination2);
		// destinations2.add(destination21);
		//
		// taskQueueService.addTaskToQueue(task);
		// taskQueueService.addTaskToQueue(task2);
		// taskDeliverService.deliverTaskToSlaveNode(task);
		// taskDeliverService.deliverTaskToSlaveNode(task2);

	}

}
