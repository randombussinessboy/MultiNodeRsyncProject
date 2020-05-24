package com.zhaoyanyang.dfss.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zhaoyanyang.dfss.factory.TaskFactory;
import com.zhaoyanyang.dfss.pojo.Destination;
import com.zhaoyanyang.dfss.pojo.DfssFile;
import com.zhaoyanyang.dfss.pojo.ReducedDfssFile;
import com.zhaoyanyang.dfss.pojo.ReducedTask;
import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.pojo.TaskInfo;
import com.zhaoyanyang.dfss.pojo.TaskTarget;
/**
 * 前端适配器,将一些内部数据结构转换为适合
 * 前端显示的数据结构
 * @author yangzy
 *
 */
@Service
public class FrontAdapter {

	@Autowired
	TaskQueueService taskQueueService;
	@Autowired
	TaskFactory taskFactory;
	@Autowired
	TaskDeliverService taskDeliverService;

	public List<ReducedTask> task2Reduced() {

		List<Task> originTasks = taskQueueService.getTaskArray();

		List<ReducedTask> res = new ArrayList<ReducedTask>();

		for (Task task : originTasks) {

			ReducedTask tmp = new ReducedTask();

			tmp.setTaskId(task.getTaskId());

			StringBuffer taskType = new StringBuffer();

			int time = task.getTargetType();
			if (time == Task.REAL_TIME_TASK) {

				taskType.append("实时");

			} else if (time == Task.TIMING_TASK) {

				taskType.append("定时");
			}

			int desNum = task.getDestinations().size();

			if (desNum == 1) {
				taskType.append("一对一任务");
			} else {
				taskType.append("一对多任务");
			}

			tmp.setTaskType(taskType.toString());

			StringBuffer taskState = new StringBuffer();

			Boolean state = task.isRunning();

			if (state) {

				taskState.append("正在运行");

			} else {
				taskState.append("暂停中");
			}

			tmp.setTaskState(taskState.toString());

			if (task.getFileName() == null) {

				tmp.setSource(task.getOwnHost() + " " + task.getDirectoryName());

			} else {

				tmp.setSource(task.getOwnHost() + " " + task.getDirectoryName() + File.separator + task.getFileName());
			}

			// 设置目的主机了
			List<String> destinationString = new ArrayList<String>();

			List<Destination> destinations = task.getDestinations();

			for (Destination destination : destinations) {

				if (destination.getFileName() == null) {
					destinationString.add(destination.getUrl() + " " + destination.getDirectoryName());
				} else {

					destinationString.add(destination.getUrl() + " " + destination.getDirectoryName() + File.separator
							+ destination.getFileName());
				}

			}

			List<String> duix = tmp.getDestinations();
			duix.addAll(destinationString);

			res.add(tmp);

		}

		return res;

	}
	
	
	public List<ReducedTask> historyTask2Reduced() {

		List<Task> originTasks = taskQueueService.getHistoryTaskArray();

		List<ReducedTask> res = new ArrayList<ReducedTask>();

		for (Task task : originTasks) {

			ReducedTask tmp = new ReducedTask();

			tmp.setTaskId(task.getTaskId());

			StringBuffer taskType = new StringBuffer();

			int time = task.getTargetType();
			if (time == Task.REAL_TIME_TASK) {

				taskType.append("实时");

			} else if (time == Task.TIMING_TASK) {

				taskType.append("定时");
			}

			int desNum = task.getDestinations().size();

			if (desNum == 1) {
				taskType.append("一对一任务");
			} else {
				taskType.append("一对多任务");
			}

			tmp.setTaskType(taskType.toString());

			StringBuffer taskState = new StringBuffer();

			Boolean state = task.isRunning();

			if (state) {

				taskState.append("正在运行");

			} else {
				taskState.append("暂停中");
			}

			tmp.setTaskState(taskState.toString());

			if (task.getFileName() == null) {

				tmp.setSource(task.getOwnHost() + " " + task.getDirectoryName());

			} else {

				tmp.setSource(task.getOwnHost() + " " + task.getDirectoryName() + File.separator + task.getFileName());
			}

			// 设置目的主机了
			List<String> destinationString = new ArrayList<String>();

			List<Destination> destinations = task.getDestinations();

			for (Destination destination : destinations) {

				if (destination.getFileName() == null) {
					destinationString.add(destination.getUrl() + " " + destination.getDirectoryName());
				} else {

					destinationString.add(destination.getUrl() + " " + destination.getDirectoryName() + File.separator
							+ destination.getFileName());
				}

			}

			List<String> duix = tmp.getDestinations();
			duix.addAll(destinationString);

			res.add(tmp);

		}

		return res;

	}
	
	

	public List<ReducedDfssFile> dfssFile2Reduced(List<DfssFile> src) {

		List<ReducedDfssFile> res = new ArrayList<>();

		for (DfssFile dfssFile : src) {

			ReducedDfssFile tmp = new ReducedDfssFile();
			tmp.setName(dfssFile.getFileName());
			tmp.setSourceUrl(dfssFile.getOwnHost());
			tmp.setFileUrl(dfssFile.getAbosoluteAddr());
			if (dfssFile.getDfssFiletype() == DfssFile.DFSS_FILE_TYPE_DIRECTORY) {
				tmp.setIsParent(true);
			} else {
				tmp.setIsParent(false);
			}
			res.add(tmp);

		}

		return res;

	}

	public DfssFile reducedDfssFile2DfssFile(ReducedDfssFile reducedDfssFile) {

		DfssFile dfssFile = new DfssFile();

		if (StringUtils.isEmpty(reducedDfssFile.getFileUrl())) {
			dfssFile.setFileName("\\");
		} else {
			dfssFile.setFileName(reducedDfssFile.getFileUrl());
		}
		dfssFile.setOwnHost(reducedDfssFile.getSourceUrl());
		if (reducedDfssFile.getIsParent()) {
			dfssFile.setDfssFiletype(DfssFile.DFSS_FILE_TYPE_DIRECTORY);
		} else {
			dfssFile.setDfssFiletype(DfssFile.DFSS_FILE_TYPE_FILE);
		}

		return dfssFile;

	}
	/**
	 * 任务建造工厂里面的建造一对一 一对多的除了方法名称不一样 其他都一样 所有建造一对一的时候可以直接用一对多。
	 * @param list
	 * @return
	 */
	public String taskCobvert(List<TaskInfo> list) {

		for (TaskInfo taskInfo : list) {

			if (taskInfo.isDirectonry()) {
				System.out.println("是目录");
				if (taskInfo.isTimmingTask()) {
					
					System.out.println("是定时任务");

					ArrayList<Destination> destinations = new ArrayList<>();
					for (TaskTarget taskTarget : taskInfo.getTargetList()) {

						Destination destination = new Destination();
						destination.setDirectoryName(taskTarget.getTargetFile());
						destination.setUrl(taskTarget.getTargetUrl());
						destinations.add(destination);

					}

					Task tmp = taskFactory.createCatogoryOne2OneTimmingTimeTask(
							taskInfo.getFileUrl(),taskInfo.getSourceUrl(),destinations,
							taskInfo.getMinutes());
					taskDeliverService.deliverTaskToSlaveNode(tmp);
					taskQueueService.addTaskToQueue(tmp);
					tmp.setRunning(true);

				} else {
					
					

				}
				

			} else {//是文件同步的话,不止要不原URL分离出来 还需要把目的的分离出来
				
				if (taskInfo.isTimmingTask()) {
					
					System.out.println("是文件同步里的定时任务");
					
					ArrayList<Destination> destinations = new ArrayList<>();
					for (TaskTarget taskTarget : taskInfo.getTargetList()) {

						Destination destination = new Destination();
						
						String directoryAndFileName=taskTarget.getTargetFile();
						String fileName=directoryAndFileName.split("\\\\")[directoryAndFileName.split("\\\\").length-1];
						String directoryName=directoryAndFileName.replace("\\"+fileName, "");
						
						destination.setDirectoryName(directoryName);
						destination.setFileName(fileName);
						destination.setUrl(taskTarget.getTargetUrl());
						destinations.add(destination);

					}
					//将文件Url拆分成目录名和文件名
					
					String directoryAndFileName=taskInfo.getFileUrl();
					String fileName=directoryAndFileName.split("\\\\")[directoryAndFileName.split("\\\\").length-1];
					String directoryName=directoryAndFileName.replace("\\"+fileName, "");
					System.out.println(fileName);
					System.out.println(directoryName);
				
					Task tmp = taskFactory.createOne2MoreTimmingTimeTaskultimate(directoryName, fileName,
							taskInfo.getSourceUrl(), destinations, taskInfo.getMinutes());
					taskDeliverService.deliverTaskToSlaveNode(tmp);
					taskQueueService.addTaskToQueue(tmp);
					
					tmp.setRunning(true);
					
					
					
				}else {
					System.out.println("是文件同步里的实时任务");
					
					ArrayList<Destination> destinations = new ArrayList<>();
					for (TaskTarget taskTarget : taskInfo.getTargetList()) {

						Destination destination = new Destination();
						String directoryAndFileName=taskTarget.getTargetFile();
						String fileName=directoryAndFileName.split("\\\\")[directoryAndFileName.split("\\\\").length-1];
						String directoryName=directoryAndFileName.replace("\\"+fileName, "");
						
						destination.setDirectoryName(directoryName);
						destination.setFileName(fileName);
					
						destination.setUrl(taskTarget.getTargetUrl());
						destinations.add(destination);

					}
					//将文件Url拆分成目录名和文件名
					
					String directoryAndFileName=taskInfo.getFileUrl();
					String fileName=directoryAndFileName.split("\\\\")[directoryAndFileName.split("\\\\").length-1];
					String directoryName=directoryAndFileName.replace("\\"+fileName, "");
					System.out.println(fileName);
					System.out.println(directoryName);
				
				
					Task tmp = taskFactory.createOne2MoreRealTimeTask
							(directoryName, fileName, taskInfo.getSourceUrl(), destinations);
					taskDeliverService.deliverTaskToSlaveNode(tmp);
					taskQueueService.addTaskToQueue(tmp);
					tmp.setRunning(true);
					
				}

			}

		}

		return "success";

	}

}
