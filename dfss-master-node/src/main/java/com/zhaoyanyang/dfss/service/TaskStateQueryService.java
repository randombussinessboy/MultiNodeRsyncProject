package com.zhaoyanyang.dfss.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.zhaoyanyang.dfss.pojo.Task;

@Service
/**
 * 查询设置任务状态
 * @author yangzy
 *
 */
public class TaskStateQueryService {
	
	@Autowired TaskQueueService taskQueueService;
	@Value("${rabbitmq.hostName}")
	String rabbitmqhost;
	/**
	 * 本地根据任务id 设置任务的状态
	 * @param taskId
	 * @param state
	 */
	public void setTaskState(String taskId,int state) {
		
		try {
			Task task=taskQueueService.findTask(taskId);
			task.setProegress(state);
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
	}
	/**
	 * 本地根据任务Id获取任务状态
	 * @param taskId
	 * @return
	 */
	public int getTaskState(String taskId) {
		
		
		try {
			Task task=taskQueueService.findTask(taskId);
			return task.getProegress();
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return -1;
		}
		
	}
	
	
	
	public List<String> getMsgFromQueue(String taskId) throws IOException, TimeoutException{
		
		
		String queueName=taskId+"syncProcessInfomation";
		ArrayList<String> result=new ArrayList<>();
		
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
		channel.queueDeclare(queueName, false, false, true, null);
		System.out.println(taskId + " 等待接受消息");

		DeclareOk declareOk = channel.queueDeclarePassive(queueName);
		int num = declareOk.getMessageCount();
		int i = 1;

		while (i <= num) {

			GetResponse getResponse = channel.basicGet(queueName, true);
			String message = new String(getResponse.getBody());
			result.add(message);
			System.out.println(message);
			i++;

		}
		
		return result;
		
		
	}
	
	
	
}
