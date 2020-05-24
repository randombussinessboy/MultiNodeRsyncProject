package com.zhaoyanyang.dfss.listener;

import java.io.File;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zhaoyanyang.dfss.pojo.Task;

import net.contentobjects.jnotify.JNotifyListener;

public class FileChangesListenner extends Thread implements JNotifyListener{
	private  String queue_name;
	private Task task;
	private String rabbitmq;

	public FileChangesListenner(Task task,String rabbitMqHost) {
		System.out.println("new thread:" + Thread.currentThread().getName());
		this.task=task;
		queue_name=task.getQueueName();
		this.rabbitmq=rabbitMqHost;
	}

	/**
	 * 该方法监听文件改名事件
	 * 
	 * @param wd
	 *            被监听目录ID
	 * @param rootPath
	 *            被监听目录
	 * @param oldName
	 *            被改名前文件名
	 * @param newName
	 *            被改名后文件名
	 */
	public void fileRenamed(int wd, String rootPath, String oldName, String newName) {

		print("renamed " + oldName + " -> " + newName);
	}

	/**
	 * 该方法监听文件修改事件
	 * 
	 * @param wd
	 *            被监听目录ID
	 * @param rootPath
	 *            被监听目录
	 * @param name
	 *            被修改文件名
	 */
	public void fileModified(int wd, String rootPath, String name) {
		
		File file=new File(rootPath+File.separator+name);
		if (file.isDirectory()) {
			return;
		}

		print("modified " + rootPath + ":" + name);
	}

	/**
	 * 注意：JNotify存在一次文件修改，触发多次fileModified方法的BUG，
	 * 该方法可以用来修复一次文件修改可能会触发多个fileModified方法， 从而减少没有必要的资源重新加载。但是由于t变量是类内共享变量，所
	 * 以注意线程安全，尽量避免共用Listener导致错误
	 */
	// long t = 0;
	// public void fileModified(int wd, String rootPath, String name) {
	// File file = new File(rootPath, name);
	// if (t != file.lastModified()) {
	// print("modified " + rootPath + " : " + name + " : ");
	// t = file.lastModified();
	// }
	// }

	/**
	 * 该方法监听文件删除事件
	 * 
	 * @param wd
	 *            被监听目录ID
	 * @param rootPath
	 *            被监听目录
	 * @param name
	 *            被删除文件名
	 */
	public void fileDeleted(int wd, String rootPath, String name) {

		print("deleted " + rootPath + File.separator + name);
	}

	/**
	 * 改方法监听文件创建事件
	 * 
	 * @param wd
	 *            被监听目录ID
	 * @param rootPath
	 *            被监听目录
	 * @param name
	 *            被创建文件名
	 */
	public void fileCreated(int wd, String rootPath, String name) {
		

		File file=new File(rootPath+File.separator+name);
		if (file.isDirectory()) {
			print("created " + rootPath + File.separator + name+" 1");
			return;
		}
		print("created " + rootPath + File.separator + name+" 0");
		
	}

	/**
	 * 错误打印
	 * 
	 * @param msg
	 *            错误信息
	 */
	void print(String msg) {
		
		System.err.println(Thread.currentThread().getName()+" "+queue_name+"  " + msg);
		if (task.getTargetType()==Task.FILE_TASK) {
			
			if (!msg.contains(task.getFileName())) {
				return;
			}
		}
		try {
			// 创建连接工厂
			ConnectionFactory factory = new ConnectionFactory();
			// 设置RabbitMQ相关信息
			factory.setHost(rabbitmq);
			// 创建一个新的连接
			Connection connection = factory.newConnection();
			// 创建一个通道
			Channel channel = connection.createChannel();

			channel.queueDeclare(queue_name, false, false, true, null);
			
			channel.basicPublish("", queue_name, null, msg.getBytes("UTF-8"));
/*
			for (int i = 0; i < 100; i++) {
				String message = "direct 消息 " + i;
				// 发送消息到队列中
				channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
				System.out.println("发送消息： " + message);

			}
			*/
			// 关闭通道和连接
			channel.close();
			connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
