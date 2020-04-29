package com.zhaoyanyang.dfss.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoyanyang.dfss.pojo.Destination;
import com.zhaoyanyang.dfss.pojo.ReducedTask;
import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.pojo.TaskInfo;
import com.zhaoyanyang.dfss.pojo.TaskTarget;
import com.zhaoyanyang.dfss.service.FrontAdapter;
import com.zhaoyanyang.dfss.service.TaskQueueService;

/**
 * 处理历史任务那个controlle
 * @author yangzy
 *
 */
@RestController
public class MasterNodeHistoryTaskController {
	
	@Autowired
	private FrontAdapter frontAdapter;
	@Autowired
	private TaskQueueService taskQueueService;

	/**
	 * 返回在历史任务队列中的所有的任务
	 * @return
	 */
	@GetMapping("/getAllHistoryTasks")
	public Map<String, Object> getAllTasks(){
			
		List<ReducedTask> res=frontAdapter.historyTask2Reduced();
		Map<String,Object> map=new HashMap<>();
		map.put("rows", res);
		return map;
		
	}
	
	
	@RequestMapping("/getHistoryTaskDetails")
	public TaskInfo getHistoryTaskDetails(@RequestParam("taskId") String taskId) throws Exception{
		
		TaskInfo taskInfo = new TaskInfo();

		try {
			Task src = taskQueueService.findHistorryTask(taskId);

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
	
}
