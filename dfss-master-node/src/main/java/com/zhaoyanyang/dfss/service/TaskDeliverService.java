package com.zhaoyanyang.dfss.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zhaoyanyang.dfss.pojo.DfssFile;
import com.zhaoyanyang.dfss.pojo.Task;
/**
 * 将任务运输到从节点上去
 * @author yangzy
 *
 */
@Service
public class TaskDeliverService {
	
	@Autowired RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "slaveNodeConnectless")
	public Boolean deliverTaskToSlaveNode(Task task) {
		
		String ownHost=task.getOwnHost();
	
        String url = String.format("%s/deliveryTask",ownHost);
        System.out.println(url);
        
		String wathchId=restTemplate.postForObject(url, task,String.class);
		System.out.println(wathchId);
		task.setWatchId(wathchId);
		return true;
	}
	
	public Boolean slaveNodeConnectless(Task task) {
		
		task.setWatchId("error");
		return false;
	}
	
}
