package com.zhaoyanyang.dfss.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoyanyang.dfss.service.ListFilesService;
import com.zhaoyanyang.dfss.service.StartWatchingService;
import com.zhaoyanyang.dfss.service.TaskQueueService;
import com.zhaoyanyang.dfss.pojo.DfssFile;
import com.zhaoyanyang.dfss.pojo.Task;

@RestController
public class SlaveNodeController {
	@Autowired ListFilesService lService;
	@Autowired TaskQueueService tService;
	@Autowired StartWatchingService sService;
	/**
	 * 给出子目录和文件夹
	 * @param directory 需要遍历的目录
	 * @return 返回一个文件数组
	 * @throws Exception 
	 */
	@RequestMapping("/listFile")
    public Object listFile(@RequestParam("directory") String directory) throws Exception {
		List<DfssFile> ls=lService.getDirectoryContent(directory);
        return ls;
    }
	/**
	 * 给从节点增添任务
	 * @param task 由主节点发开的任务
	 * @return String 返回监控的Id
	 */
	@RequestMapping("/deliveryTask")
	public Object deliveryTask(@RequestBody Task task) {
		tService.addTaskToQueue(task);
		System.out.println(task.getFileName());
		int wathcId=sService.addMonitor(task);
		return String.valueOf(wathcId);
	}
	/**
	 * 通过监控Id来删除这个任务
	 * @param watchId 对应的文件I
	 * @throws Exception
	 */
	@RequestMapping("/removeTask")
	public void removeTask(@RequestParam("watchId")String watchId) throws Exception {
		sService.removeMonitor(Integer.parseInt(watchId));
		Task task=tService.findTaskByWatchId(watchId);
		tService.removeTask(task);
	}
	
}
