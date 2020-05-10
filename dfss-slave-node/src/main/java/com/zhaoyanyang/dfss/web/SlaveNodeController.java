package com.zhaoyanyang.dfss.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.zhaoyanyang.dfss.service.ListFilesService;
import com.zhaoyanyang.dfss.service.StartWatchingService;
import com.zhaoyanyang.dfss.service.SyncAcceptService;
import com.zhaoyanyang.dfss.service.SyncService;
import com.zhaoyanyang.dfss.service.TaskQueueService;

import net.contentobjects.jnotify.JNotifyException;

import com.zhaoyanyang.dfss.pojo.DfssFile;
import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.rsync.ChecksumPair;
import com.zhaoyanyang.dfss.rsync.Configuration;
import com.zhaoyanyang.dfss.rsync.Delta;
import com.zhaoyanyang.dfss.rsync.Rdiff;
import com.zhaoyanyang.dfss.rsync.RsyncProvider;

@RestController
public class SlaveNodeController {
	@Autowired
	ListFilesService listFilesService;
	@Autowired
	TaskQueueService taskQueueService;
	@Autowired
	StartWatchingService startWatchingService;
	@Autowired
	SyncService syncService;
	@Autowired
	SyncAcceptService SyncAcceptService;
	
	
	/**
	 * 创建文件 type为0是文件 其它为目录
	 * @param filePath
	 * @param type
	 * @return
	 */
	@RequestMapping("/createFile")
	public Object createFile(@RequestParam("filePath") String filePath,@RequestParam("type") int type) {
		
		System.out.println(filePath+type);
		listFilesService.createCatogryFile(filePath, type);
		
		return "ok";
	}
	
	
	/**
	 * 删除文件操作 
	 * @param filePath
	 * @return
	 */
	@RequestMapping("/deleteFile")
	public Object deleteFile(@RequestParam("filePath") String filePath) {
		System.out.println(filePath);
		listFilesService.deleteCatogryFile(filePath);
		
		return "ok";
	}
	
	
	/**
	 * 为了完成目录同步的功能,提供了这个接口
	 * 会把目录下 包括子目录下面的 ，所有的文件返回来
	 * @param directory 需要的目录
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/allSubFiles")
	public Object allSubFiles(@RequestParam("directory") String directory) throws IOException {
		List<DfssFile> ls = listFilesService.getDirectoryAllSubfiles(directory);
		return ls;
	}

	/**
	 * 给出子目录和文件夹
	 * 
	 * @param directory
	 *            需要遍历的目录
	 * @return 返回一个文件数组
	 * @throws Exception
	 */
	@RequestMapping("/listFile")
	public Object listFile(@RequestParam("directory") String directory) throws Exception {
		List<DfssFile> ls = listFilesService.getDirectoryContent(directory);
		return ls;
	}
	
	
	/**
	 * 获取单个文件的大小
	 * @param filePath 文件路径
	 * @return
	 */
	@RequestMapping("/getFileLength")
	public Long getFileLength(@RequestParam("filePath") String filePath) {
		System.out.println(filePath);
		long length=listFilesService.getFileLength(filePath);
		
		return length;
	}
	/**
	 * 获取指定文件夹的大小
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getDirectorySize")
	public Long getDirectorySize(@RequestParam("directory") String directory) throws IOException {
        long length=listFilesService.getDirectorySize(directory);
		return length;
	}
	
	

	/**
	 * 给从节点增添任务
	 * 
	 * @param task
	 *            由主节点发开的任务
	 * @return String 返回监控的Id
	 * @throws JNotifyException 
	 */
	@RequestMapping("/deliveryTask")
	public Object deliveryTask(@RequestBody Task task) throws JNotifyException {
		taskQueueService.addTaskToQueue(task);
		System.out.println(task.getFileName());
		int wathcId = startWatchingService.addMonitor(task);
		return String.valueOf(wathcId);
	}

	/**
	 * 通过监控Id来删除这个任务
	 * 
	 * @param watchId
	 *            对应的文件I
	 * @throws Exception
	 */
	@RequestMapping("/removeTask")
	public String removeTask(@RequestParam("watchId") String watchId) throws Exception {
		startWatchingService.removeMonitor(Integer.parseInt(watchId));
		Task task = taskQueueService.findTaskByWatchId(watchId);
		taskQueueService.removeTask(task);
		return "success";
	}
	
	
	
	
	

	
	

	/**
	 * 开始同步任务,参数就是对应的任务,不要再添加到任务队列里面了 将该任务要求的文件计算出各个块的弱检验,和强校验,发送到目的主机上面。
	 * 目的主机返回需要改变的块,组装回来即可。可能需要一个hash表来映射一下关系 才能正确组装回来
	 * 
	 * @param task
	 * @throws Exception
	 */
	@RequestMapping("/startSync")
	public String startSync(@RequestBody Task task) throws Exception {

		/*
		 * 接受主节点过来的任务,分析需要被同步的文件的校验和
		 */
		syncService.destinationHostAnalysis(task);
		return "ok";
	}

	/**
	 * 
	 * @param hostName  同步客户端的IP和端口
	 * @param sourceFileName 同步服务端对应的文件
	 * @param dstFileName 同步客户端对应的文件
	 * @param file	同步客户端传来的签名文件
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping("/deliverySignatures")
	public String deliverySignatures(@RequestParam("hostName") String hostName,
			@RequestParam("sourceFileName") String sourceFileName, @RequestParam("dstFileName") String dstFileName,
			@RequestParam("taskId") String taskId,@RequestBody MultipartFile file) throws IllegalStateException, IOException, NoSuchAlgorithmException {
		System.out.println(hostName);
		System.out.println(sourceFileName);
		System.out.println(dstFileName);
		System.out.println(taskId);
		/*
		 * 向同步客户端返回差异文件
		 */
		SyncAcceptService.sourceHostReturnDelta(hostName, sourceFileName, dstFileName,taskId, file.getInputStream());

		return "ok";

	}

	/**
	 * 使用差异文件同步成最新的文件
	 * @param dstFileName 同步客户端对应的文件
	 * @param file	差异文件
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	@RequestMapping("/deliveryDifference")
	public String deliveryDifference(@RequestParam("dstFileName") String dstFileName,
			@RequestParam("taskId") String taskId,@RequestBody MultipartFile file) throws NoSuchAlgorithmException, IOException {
			
		syncService.reBuildFile(dstFileName,taskId, file.getInputStream());
		return "ok";
	}

}
