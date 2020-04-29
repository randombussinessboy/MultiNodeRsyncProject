package com.zhaoyanyang.dfss.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoyanyang.dfss.mapper.FilechangeinfomationMapper;
import com.zhaoyanyang.dfss.pojo.Filechangeinfomation;
import com.zhaoyanyang.dfss.pojo.ReducedTask;
import com.zhaoyanyang.dfss.pojo.clusterService;
import com.zhaoyanyang.dfss.service.FrontAdapter;

/**
 * 系统概况那一栏的的接口
 * @author yangzy
 *
 */
@RestController
@CrossOrigin
public class MasterNodeSystemOverviewController {

	@Autowired
	private DiscoveryClient discoveryClient;
	@Autowired
	private FrontAdapter frontAdapter;
	
	@Autowired
	private FilechangeinfomationMapper filechangeinfomationMapper;

	/**
	 * 获取注册在Eureka中的服务名称
	 * 
	 * @return
	 */
	@GetMapping("/getEurekaServices")
	public Map<String, Object> getEurekaServices() {
		Map<String, Object> map=new HashMap<String, Object>();
		System.out.println("访问后端");
		List<clusterService> services = new ArrayList<>();
		List<String> serviceNames = discoveryClient.getServices();
		for (String serviceName : serviceNames) {
			List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
			for (ServiceInstance serviceInstance : serviceInstances) {
				clusterService tmp=new clusterService();
				tmp.setServiceName(serviceName);
				tmp.setUrl(serviceInstance.getUri().toString());
				services.add(tmp);
				
			}
		}
		map.put("rows", services);
		return map;
	}

	
	
	
	
	
	/**
	 * 返回在任务队列中的所有任务
	 * @return
	 */
	@GetMapping("/getAllTasks")
	public Map<String, Object> getAllTasks(){
			
		List<ReducedTask> res=frontAdapter.task2Reduced();
		Map<String,Object> map=new HashMap<>();
		map.put("rows", res);
		return map;
		
	}
	
	/**
	 * 获取所有节点最新的十条信息
	 * @return
	 */
	@GetMapping("/getLastTenInfo")
	public Map<String, Object> getLastTenInfo(){
		
		List<Filechangeinfomation> res=filechangeinfomationMapper.selectLastTenMsg();
		Map<String,Object> map=new HashMap<>();
		map.put("rows", res);
		return map;
		
	}
	
}
