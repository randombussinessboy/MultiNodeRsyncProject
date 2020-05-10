package com.zhaoyanyang.dfss.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zhaoyanyang.dfss.pojo.Task;

import cn.hutool.core.util.RandomUtil;

/**
 * 查询任务的进度和速度,有几个数据进度就算到哪了,在这个任务所属的节点计算速度
 * 
 * @author yangzy
 *
 */
@Service
public class TaskProgressQueryService {

	@Autowired
	TaskQueueService taskQueueService;
	@Autowired
	RestTemplate restTemplate;

	


	public List<Integer> getTaskProgress(String taskId) throws Exception {
		
		List res = null;

		// if (res.size()==11) {
		// res.clear();
		// }
		//
		// res.add(RandomUtil.randomInt(20,60));
		Task task;
		task = taskQueueService.findTask(taskId);
		res=task.getSpeedProgress();

		return res;

	}


}
