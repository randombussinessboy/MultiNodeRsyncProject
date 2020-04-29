package com.zhaoyanyang.dfss.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhaoyanyang.dfss.pojo.Destination;
import com.zhaoyanyang.dfss.pojo.DfssFile;
import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.service.ListFilesService;

/**
 * 提供几种方法生成不同类型的任务
 * 
 * @author yangzy
 *
 *         文件同步六种：1 一对一实时同步任务, 2一对多实时同步任务, 3一对一定时无次数同步任务, 4一对一定时有次数同步任务
 *         ,5一对多定时无次数同步任务 ,6一对多定时有次数同步任务
 *
 *         目录同步两种：7 一对一定时目录同步 8一对多定时目录同步
 */
@Component
public class TaskFactory {
	
	@Autowired ListFilesService listFilesService;
	/*
	 * 生成的任务ID,从一开始
	 */
	int idIncrement = 0;

	/**
	 * 一对一实时同步任务
	 * 
	 * @param DirectoryName
	 *            同步服务端的目录
	 * @param fileName
	 *            同步服务端的文件名
	 * @param OwnHost
	 *            同步服务端的主机名 前缀带有http
	 * @param destinations
	 *            目的集合
	 * @return
	 */
	public Task createOne2OneRealTimeTask(String DirectoryName, String fileName, String OwnHost,
			ArrayList<Destination> destinations) {

		idIncrement++;
		Task task = new Task();
		task.setTaskId("任务" + idIncrement);
		task.setTaskType(Task.REAL_TIME_TASK);
		task.setTargetType(Task.FILE_TASK);
		task.setQueueName("Task-" + idIncrement + "-Queue");

		task.setDirectoryName(DirectoryName);
		task.setFileName(fileName);
		task.setOwnHost(OwnHost);

		
		ArrayList<Destination> myDestinations=task.getDestinations();
		myDestinations.addAll(destinations);
		
		return task;

	}

	/**
	 * 一对多实时同步任务
	 * 
	 * @param DirectoryName
	 *            同步服务端的目录
	 * @param fileName
	 *            同步服务端的文件名
	 * @param OwnHost
	 *            同步服务端的主机名 前缀带有http
	 * @param destinations
	 *            目的集合
	 * @return
	 */
	public Task createOne2MoreRealTimeTask(String DirectoryName, String fileName, String OwnHost,
			ArrayList<Destination> destinations) {

		idIncrement++;
		Task task = new Task();
		task.setTaskId("任务" + idIncrement);
		task.setTaskType(Task.REAL_TIME_TASK);
		task.setTargetType(Task.FILE_TASK);
		task.setQueueName("Task-" + idIncrement + "-Queue");

		task.setDirectoryName(DirectoryName);
		task.setFileName(fileName);
		task.setOwnHost(OwnHost);

		ArrayList<Destination> myDestinations=task.getDestinations();
		myDestinations.addAll(destinations);

		return task;

	}

	/**
	 * 创建一个一对一定时无次数限制的同步任务
	 * 
	 * @param DirectoryName
	 *            同步服务端目录
	 * @param fileName
	 *            同步服务端文件
	 * @param OwnHost
	 *            同步服务端URL
	 * @param destinations
	 *            目的集合
	 * @param interval
	 *            定时间隔 单位为分钟
	 * @return
	 */
	public Task createOne2OneTimmingTimeTaskUltimate(String DirectoryName, String fileName, String OwnHost,
			ArrayList<Destination> destinations, int interval) {

		idIncrement++;
		Task task = new Task();
		task.setTaskId("任务" + idIncrement);
		task.setTaskType(Task.TIMING_TASK);
		task.setInterval(interval);
		task.setUsageCount(Task.TIMMING_TASK_UNLIMIT);

		task.setTargetType(Task.FILE_TASK);
		task.setQueueName("Task-" + idIncrement + "-Queue");

		task.setDirectoryName(DirectoryName);
		task.setFileName(fileName);
		task.setOwnHost(OwnHost);

		ArrayList<Destination> myDestinations=task.getDestinations();
		myDestinations.addAll(destinations);

		return task;

	}

	/**
	 * 一对多定时无次数限制的任务
	 * 
	 * @param DirectoryName
	 *            同步服务端目录名
	 * @param fileName
	 *            同步服务端文件名
	 * @param OwnHost
	 *            同步服务端URL
	 * @param destinations
	 *            目的集合
	 * @param interval
	 *            同步间隔
	 * @return
	 */
	public Task createOne2MoreTimmingTimeTaskultimate(String DirectoryName, String fileName, String OwnHost,
			ArrayList<Destination> destinations, int interval) {

		idIncrement++;
		Task task = new Task();
		task.setTaskId("任务" + idIncrement);
		task.setTaskType(Task.TIMING_TASK);
		task.setInterval(interval);
		task.setUsageCount(Task.TIMMING_TASK_UNLIMIT);

		task.setTargetType(Task.FILE_TASK);
		task.setQueueName("Task-" + idIncrement + "-Queue");

		task.setDirectoryName(DirectoryName);
		task.setFileName(fileName);
		task.setOwnHost(OwnHost);

		ArrayList<Destination> myDestinations=task.getDestinations();
		myDestinations.addAll(destinations);

		return task;

	}

	/**
	 * 一对一有次数定时同步任务
	 * 
	 * @param DirectoryName
	 *            同步服务端目录名
	 * @param fileName
	 *            同步服务端文件名
	 * @param OwnHost
	 *            同步服务端URL
	 * @param destinations
	 *            同步客户端目的主机
	 * @param interval
	 *            定时间隔
	 * @param count
	 *            定时次数
	 * @return
	 */
	public Task createOne2OneTimmingTimeTaskHasCount(String DirectoryName, String fileName, String OwnHost,
			ArrayList<Destination> destinations, int interval, int count) {
		idIncrement++;
		Task task = new Task();
		task.setTaskId("任务" + idIncrement);
		task.setTaskType(Task.TIMING_TASK);
		task.setInterval(interval);
		task.setUsageCount(count);

		task.setTargetType(Task.FILE_TASK);
		task.setQueueName("Task-" + idIncrement + "-Queue");

		task.setDirectoryName(DirectoryName);
		task.setFileName(fileName);
		task.setOwnHost(OwnHost);

		ArrayList<Destination> myDestinations=task.getDestinations();
		myDestinations.addAll(destinations);

		return task;

	}

	/**
	 * 有次数一对多定时同步任务
	 * 
	 * @param DirectoryName
	 *            同步服务端目录名
	 * @param fileName
	 *            同步服务端文件名
	 * @param OwnHost
	 *            同步服务端URL
	 * @param destinations
	 *            目的集合
	 * @param interval
	 *            同步间隔
	 * @param count
	 *            同步次数
	 * @return
	 */
	public Task createOne2MoreTimmingTimeTaskHasCount(String DirectoryName, String fileName, String OwnHost,
			ArrayList<Destination> destinations, int interval, int count) {
		idIncrement++;
		Task task = new Task();
		task.setTaskId("任务" + idIncrement);
		task.setTaskType(Task.TIMING_TASK);
		task.setInterval(interval);
		task.setUsageCount(count);

		task.setTargetType(Task.FILE_TASK);
		task.setQueueName("Task-" + idIncrement + "-Queue");

		task.setDirectoryName(DirectoryName);
		task.setFileName(fileName);
		task.setOwnHost(OwnHost);

		ArrayList<Destination> myDestinations=task.getDestinations();
		myDestinations.addAll(destinations);

		return task;

	}
	
	
	/**
	 * 一对一目录同步
	 * @param DirectoryName 同步服务端文件夹
	 * @param OwnHost 同步服务端URL
	 * @param destinations 同步目的文件夹集合
	 * @param interval 同步时间间隔
	 * @return
	 */
	public Task createCatogoryOne2OneTimmingTimeTask(String DirectoryName, String OwnHost,
			ArrayList<Destination> destinations, int interval) {

		idIncrement++;
		Task task = new Task();
		task.setTaskId("任务" + idIncrement);
		task.setTaskType(Task.TIMING_TASK);
		task.setInterval(interval);
		task.setUsageCount(Task.TIMMING_TASK_UNLIMIT);

		task.setTargetType(Task.DIRECTORY_TASK);
		task.setQueueName("Task-" + idIncrement + "-Queue");
		task.setDirectoryName(DirectoryName);
		task.setOwnHost(OwnHost);

		ArrayList<Destination> myDestinations=task.getDestinations();
		myDestinations.addAll(destinations);
		
		
		
//		DfssFile dfssFile=new DfssFile();
//		dfssFile.setOwnHost(OwnHost);
//		dfssFile.setFileName(DirectoryName);
//		
//		List<DfssFile> dfssFiles= listFilesService.allSubFiles(dfssFile);
//		ArrayList<String> subFiles=task.getCatagoryTaskSubfiles();
//		
//		Iterator<DfssFile> iterator=dfssFiles.iterator();
//		
//		while (iterator.hasNext()) {
//		  DfssFile tmp=iterator.next();
//		  subFiles.add(tmp.getFileName());
//			
//		}
		
		return task;

	}
	
	
	/**
	 * 一对多目录同步
	 * @param DirectoryName 同步服务端文件夹
	 * @param OwnHost 同步服务端URL
	 * @param destinations 同步目的文件夹集合
	 * @param interval 同步时间间隔
	 * @return
	 */
	public Task createCatogoryOne2MoreTimmingTimeTask(String DirectoryName, String OwnHost,
			ArrayList<Destination> destinations, int interval) {

		idIncrement++;
		Task task = new Task();
		task.setTaskId("任务" + idIncrement);
		task.setTaskType(Task.TIMING_TASK);
		task.setInterval(interval);
		task.setUsageCount(Task.TIMMING_TASK_UNLIMIT);

		task.setTargetType(Task.DIRECTORY_TASK);
		task.setQueueName("Task-" + idIncrement + "-Queue");
		task.setDirectoryName(DirectoryName);
		task.setOwnHost(OwnHost);

		ArrayList<Destination> myDestinations=task.getDestinations();
		myDestinations.addAll(destinations);
		
		
		DfssFile dfssFile=new DfssFile();
		dfssFile.setOwnHost(OwnHost);
		dfssFile.setFileName(DirectoryName);
		
//		List<DfssFile> dfssFiles= listFilesService.allSubFiles(dfssFile);
//		ArrayList<String> subFiles=task.getCatagoryTaskSubfiles();
//		
//		Iterator<DfssFile> iterator=dfssFiles.iterator();
//		
//		while (iterator.hasNext()) {
//		  DfssFile tmp=iterator.next();
//		  subFiles.add(tmp.getFileName());
//			
//		}

		return task;

	}
	
	
}
