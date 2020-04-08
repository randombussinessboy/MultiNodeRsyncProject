package com.zhaoyanyang.dfss.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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

	@Value("${rabbitmq.hostName}")
	String rabbitmqhost;
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
			i++;

		}
		
		

		channel.close();
		connection.close();

	}

}
