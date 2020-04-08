package com.zhaoyanyang.dfss.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.zhaoyanyang.dfss.pojo.DfssFile;
import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.service.ListFilesService;
import com.zhaoyanyang.dfss.service.TaskDeliverService;
import com.zhaoyanyang.dfss.service.TaskQueueService;

import cn.hutool.core.util.RandomUtil;

@Controller
public class MasterNodeViewController {
	@Autowired
	ListFilesService lService;
	@Autowired
	TaskDeliverService taskDeliverService;
	@Autowired TaskQueueService taskQueueService;

	/**
	 * 获取一个目录下的所有子文件和子目录
	 * 
	 * @param m
	 * @return
	 * @throws TimeoutException
	 * @throws IOException
	 */
	@RequestMapping("/list")
	public Object lisFileContent(Model m) throws IOException, TimeoutException {

		DfssFile dfssFile = new DfssFile();
		dfssFile.setDfssFiletype(DfssFile.DFSS_FILE_TYPE_DIRECTORY);
		dfssFile.setOwnHost("localhost:8011");
		dfssFile.setFileName("E:\\毕业设计项目代码");
		List<DfssFile> files = lService.ListDirectory(dfssFile);

		Task task = new Task();
		task.setDirectoryName("E:\\毕业设计项目代码");
		task.setTargetType(Task.DIRECTORY_TASK);
		task.setQueueName("testPosess");
		task.setOwnHost("localhost:8011");
		String taskId1=RandomUtil.randomString(5);
		task.setTaskId(taskId1);
		task.setRunning(true);
		task.setTaskType(Task.REAL_TIME_TASK);
		Task task2 = new Task();
		task2.setDirectoryName("E:\\兼职赚钱");
		task2.setTargetType(Task.DIRECTORY_TASK);
		task2.setQueueName("testPosess2");
		task2.setOwnHost("localhost:8011");
		String taskId2=RandomUtil.randomString(5);
		task2.setTaskId(taskId2);
		task2.setRunning(true);
		task2.setTaskType(Task.REAL_TIME_TASK);
//		taskDeliverService.deliverTaskToSlaveNode(task);
//		taskDeliverService.deliverTaskToSlaveNode(task2);

		taskQueueService.addTaskToQueue(task);
		taskQueueService.addTaskToQueue(task2);

		m.addAttribute("files", files);
		return "view";
	}
}
