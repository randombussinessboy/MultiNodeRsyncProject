package com.zhaoyanyang.dfss.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 将同步的过程信息发送到消息队列
 * 
 * @author yangzy
 *
 */
@Service
public class TaskSynInformationService {

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${rabbitmq.hostName}")
	String rabbitmqhost;

	@Value("${master.ipAddress}")
	String masterIpAdd;

	@Async
	void sendSyncProcessInfomation(String taskId, String msg) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		msg = formatter.format(date) + ":" + msg;

		try {
			// 创建连接工厂
			ConnectionFactory factory = new ConnectionFactory();
			// 设置RabbitMQ相关信息
			factory.setHost(rabbitmqhost);
			// 创建一个新的连接
			Connection connection = factory.newConnection();
			// 创建一个通道
			Channel channel = connection.createChannel();

			channel.queueDeclare(taskId + "syncProcessInfomation", false, false, true, null);

			channel.basicPublish("", taskId + "syncProcessInfomation", null, msg.getBytes("UTF-8"));
			/*
			 * for (int i = 0; i < 100; i++) { String message = "direct 消息 " + i; //
			 * 发送消息到队列中 channel.basicPublish(EXCHANGE_NAME, "", null,
			 * message.getBytes("UTF-8")); System.out.println("发送消息： " + message);
			 * 
			 * }
			 */
			// 关闭通道和连接
			channel.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Async
	public void notifyTasKStateToMaster(String taskId, int State) {

		// 拼写目的主机名字
		String url = String.format("%s//setTaskState", masterIpAdd);
		System.out.println(url);
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("state", State);
		param.add("taskId", taskId);
		restTemplate.postForObject(url, param, String.class);

	}

}
