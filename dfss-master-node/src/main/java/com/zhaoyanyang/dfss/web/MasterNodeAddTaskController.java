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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoyanyang.dfss.factory.TaskFactory;
import com.zhaoyanyang.dfss.pojo.Destination;
import com.zhaoyanyang.dfss.pojo.DfssFile;
import com.zhaoyanyang.dfss.pojo.ReducedDfssFile;
import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.pojo.TaskInfo;
import com.zhaoyanyang.dfss.pojo.clusterService;
import com.zhaoyanyang.dfss.service.FrontAdapter;
import com.zhaoyanyang.dfss.service.ListFilesService;
import com.zhaoyanyang.dfss.service.StartSyncService;
import com.zhaoyanyang.dfss.service.TaskDeliverService;
import com.zhaoyanyang.dfss.service.TaskQueueService;
import com.zhaoyanyang.dfss.service.TaskStateQueryService;

/**
 * 增添任务那一栏的控制器
 * 
 * @author yangzy
 *
 */
@RestController
public class MasterNodeAddTaskController {
	@Autowired
	private DiscoveryClient discoveryClient;
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
	@Autowired FrontAdapter frontAdapter;
	
	/**
	 * 添加一个或者多个任务到系统里面
	 * @param list 任务表单
	 * @return
	 */
	@RequestMapping("/addTask")
	@ResponseBody
	public Map<String,Object> test(@RequestBody  List<TaskInfo> list){
		
		Map<String, Object> res=new HashMap<>();
		
		String result=frontAdapter.taskCobvert(list);
		res.put("result", result);
		
		return res;
	}

	/**
	 * 增添一个任务到系统里 测试专用
	 * 
	 * @return
	 */
	@GetMapping("/addTasktmp")
	public String addTask() {
		ArrayList<Destination> destinations = new ArrayList<>();
		Destination destination = new Destination();
		destination.setDirectoryName("E:\\毕业设计项目代码\\SlaveNodeDemo\\slaveNode2");
		destination.setUrl("http://localhost:8012");
		destinations.add(destination);

		Destination destination2 = new Destination();
		destination2.setDirectoryName("E:\\毕业设计项目代码\\SlaveNodeDemo\\slaveNode3");
		destination2.setUrl("http://localhost:8013");
		destinations.add(destination2);

		Task muluTask = taskFactory.createCatogoryOne2OneTimmingTimeTask("E:\\毕业设计项目代码\\SlaveNodeDemo\\slaveNode1",
				"http://localhost:8011", destinations, 3);

		taskDeliverService.deliverTaskToSlaveNode(muluTask);
		taskQueueService.addTaskToQueue(muluTask);
		muluTask.setRunning(true);

		return "addSuccess";
		
		// DfssFile dfssFile = new DfssFile();
		// dfssFile.setDfssFiletype(DfssFile.DFSS_FILE_TYPE_DIRECTORY);
		// dfssFile.setOwnHost("http://localhost:8011");
		// dfssFile.setFileName("E:\\毕业设计项目代码\\远程主机管理工具");
		
	}

	/**
	 * 获取一个目录下的所有子文件和子目录
	 * 
	 * @param
	 * @return
	 * @throws TimeoutException
	 * @throws IOException
	 */
	@RequestMapping("/list")
	public List<ReducedDfssFile> lisFileContent(ReducedDfssFile reducedDfssFile) throws IOException, TimeoutException {

		DfssFile dfssFile=frontAdapter.reducedDfssFile2DfssFile(reducedDfssFile);
		List<DfssFile> files = listService.ListDirectory(dfssFile);
		List<ReducedDfssFile> reducedDfssFiles=frontAdapter.dfssFile2Reduced(files);
		return reducedDfssFiles;

	}

	/**
	 * 获取集群中所有的子节点
	 * 
	 * @return
	 */
	@GetMapping("/getSlaveNodeServices")
	public Map<String, Object> getSlaveNodeService() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<clusterService> services = new ArrayList<>();
		List<String> serviceNames = discoveryClient.getServices();
		for (String serviceName : serviceNames) {
			List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
			for (ServiceInstance serviceInstance : serviceInstances) {
				if (serviceName.contains("dfss-slave")) {
					clusterService tmp=new clusterService();
					tmp.setUrl(serviceInstance.getUri().toString());
					tmp.setServiceName(serviceName);
					services.add(tmp);
				}
			}
		}
		map.put("rows", services);
		return map;
	}

}
