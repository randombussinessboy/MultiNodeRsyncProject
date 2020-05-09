package com.zhaoyanyang.dfss.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoyanyang.dfss.factory.TaskFactory;
import com.zhaoyanyang.dfss.mapper.FilechangeinfomationMapper;
import com.zhaoyanyang.dfss.pojo.Destination;
import com.zhaoyanyang.dfss.pojo.DfssFile;
import com.zhaoyanyang.dfss.pojo.Filechangeinfomation;
import com.zhaoyanyang.dfss.pojo.ReducedTask;
import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.pojo.clusterService;
import com.zhaoyanyang.dfss.service.FrontAdapter;
import com.zhaoyanyang.dfss.service.ListFilesService;
import com.zhaoyanyang.dfss.service.StartSyncService;
import com.zhaoyanyang.dfss.service.TaskDeliverService;
import com.zhaoyanyang.dfss.service.TaskQueueService;
import com.zhaoyanyang.dfss.service.TaskStateQueryService;
/**
 * 这个控制器提供的任务状态改变已经放弃使用
 * 因为一个任务对应的轮次都是异步的
 * 没有办法做到同步状态，现在改用提供把消息发到任务队列的方法
 * @author yangzy
 *
 */
@CrossOrigin
@RestController
public class MasterNodeApartController {

	@Autowired
	ListFilesService listService;
	@Autowired
	TaskDeliverService taskDeliverService;
	@Autowired
	TaskQueueService taskQueueService;
	@Autowired
	StartSyncService startSyncService;
	@Autowired
	TaskStateQueryService taskStateQueryService;

	@Autowired
	TaskFactory taskFactory;

	/**
	 * 同步节点向主节点发送同步信息,
	 * 
	 * @param taskId
	 *            任务id
	 * @param state
	 *            最新状态
	 * @return
	 */
	@RequestMapping("/setTaskState")
	public Object setTaskState(@RequestParam("taskId") String taskId, @RequestParam("state") int state) {

		taskStateQueryService.setTaskState(taskId, state);

		return "ok";
	}

	
}
