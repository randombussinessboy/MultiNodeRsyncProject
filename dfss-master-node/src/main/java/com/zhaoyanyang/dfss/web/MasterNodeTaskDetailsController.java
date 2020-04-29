package com.zhaoyanyang.dfss.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zhaoyanyang.dfss.pojo.Destination;
import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.pojo.TaskInfo;
import com.zhaoyanyang.dfss.pojo.TaskTarget;
import com.zhaoyanyang.dfss.service.TaskQueueService;
import com.zhaoyanyang.dfss.service.TaskStateQueryService;

/**
 * 任务详情那一栏
 * 
 * @author yangzy
 *
 */
@CrossOrigin
@RestController
public class MasterNodeTaskDetailsController {
	@Autowired
	TaskQueueService taskQueueService;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	TaskStateQueryService taskStateQueryService;

	/**
	 * 暂停任务
	 * 
	 * @param taskId
	 *            任务id
	 * @return
	 */
	@RequestMapping("/pauseTask")
	public Map<String, Object> pauseTask(@RequestParam("taskId") String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Task target = taskQueueService.findTask(taskId);
			target.setRunning(false);
			map.put("result", "success");
			return map;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();

			map.put("result", "fail");
			return map;
		}

	}

	/**
	 * 删除任务
	 * 
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/deleteTask")
	public Map<String, Object> deleteTask(@RequestParam("taskId") String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Task target = taskQueueService.findTask(taskId);

			// 拼写源主机名字
			String ownHost = target.getOwnHost();
			String url = String.format("%s//removeTask", ownHost);
			System.out.println(url);
			MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
			param.add("watchId", target.getWatchId());
			String string = restTemplate.postForObject(url, param, String.class);
			System.out.println(string);
			taskQueueService.removeTask(taskId);

			map.put("result", "success");
			return map;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();

			map.put("result", "fail");
			return map;
		}

	}

	/**
	 * 把任务状态设置为运行
	 * 
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/startTask")
	public Map<String, Object> startTask(@RequestParam("taskId") String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Task target = taskQueueService.findTask(taskId);
			target.setRunning(true);
			map.put("result", "success");
			return map;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();

			map.put("result", "fail");
			return map;
		}

	}

	@RequestMapping("/getTaskDetails")
	public TaskInfo getTaskDetails(@RequestParam("taskId") String taskId) {

		TaskInfo taskInfo = new TaskInfo();

		try {
			Task src = taskQueueService.findTask(taskId);

			taskInfo.setSourceUrl(src.getOwnHost());
			taskInfo.setTaskId(src.getTaskId());

			if (src.getTargetType() == Task.DIRECTORY_TASK) {

				taskInfo.setDirectonry(true);
				taskInfo.setFileUrl(src.getDirectoryName());

			} else {
				taskInfo.setDirectonry(false);
				taskInfo.setFileUrl(src.getDirectoryName() + File.separator + src.getFileName());
			}

			if (src.getTaskType() == Task.TIMING_TASK) {
				taskInfo.setTimmingTask(true);
				taskInfo.setMinutes(src.getInterval());
			} else {
				taskInfo.setTimmingTask(false);
			}

			ArrayList<TaskTarget> targets = new ArrayList<>();
			for (Destination destination : src.getDestinations()) {

				TaskTarget target = new TaskTarget();
				target.setTargetUrl(destination.getUrl());
				if (src.getTargetType() == Task.DIRECTORY_TASK) {

					target.setTargetFile(destination.getDirectoryName());
				} else {
					target.setTargetFile(destination.getDirectoryName() + File.separator + destination.getFileName());
				}
				targets.add(target);

			}

			taskInfo.setTargetList(targets);

		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		return taskInfo;

	}
	
	@RequestMapping("/getTaskInformation")
	public Map<String, Object> getTaskInformation(@RequestParam("taskId") String taskId) throws IOException, TimeoutException{
		
		Map<String, Object> map=new HashMap<>();
		
	    List<String> res=taskStateQueryService.getMsgFromQueue(taskId);
		
	    map.put("rows", res);
		
		return map;
		
	}
	
	@RequestMapping("/modifyTask")
	public Map<String,Object> modifyTask(@RequestBody TaskInfo taskInfo){
		Map<String, Object> map=new HashMap<>();
		
		String taskId=taskInfo.getTaskId();
		try {
			Task src = taskQueueService.findTask(taskId);
			if (src.getTaskType()==Task.TIMING_TASK) {
				src.setInterval(taskInfo.getMinutes());
			}
			
			ArrayList<Destination> destinations = new ArrayList<>();
			for (TaskTarget taskTarget : taskInfo.getTargetList()) {

				Destination destination = new Destination();
				destination.setDirectoryName(taskTarget.getTargetFile());
				destination.setUrl(taskTarget.getTargetUrl());
				destinations.add(destination);

			}
			
			ArrayList<Destination> origin=src.getDestinations();
			origin.clear();
			origin.addAll(destinations);
			
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			map.put("result", "fail");
		}
		
		map.put("result", "success");
		return map;
	}

}
