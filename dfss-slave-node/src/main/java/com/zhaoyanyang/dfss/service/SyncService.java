package com.zhaoyanyang.dfss.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.zhaoyanyang.dfss.config.ServerConfig;
import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.rsync.RsyncProvider;

import cn.hutool.core.util.RandomUtil;

import com.zhaoyanyang.dfss.rsync.ChecksumPair;
import com.zhaoyanyang.dfss.rsync.Configuration;
import com.zhaoyanyang.dfss.rsync.Delta;
import com.zhaoyanyang.dfss.rsync.Rdiff;

@Service
public class SyncService {

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	TaskSynInformationService taskSynInformationService;

	@Autowired
	ServerConfig serverConfig;

	@Value("${master.ipAddress}")
	String masterUrl;

	/**
	 * 从任务中获取这个任务对应的目标文件夹和文件 调用Rsync算法生成第一部分 这边先考虑一对一同步
	 * 
	 * @param task
	 */
	@Async
	public void destinationHostAnalysis(Task task) throws Exception {
		// 因为windows和Linux下文件路径分隔符不是统一的。
		String directoryName = task.getTargetDirectoryName();
		String fileName = task.getTargetFileName();
		String filePath = directoryName + File.separator + fileName;

		Security.addProvider(new RsyncProvider());
		Configuration c = new Configuration();
		Rdiff rdf = new Rdiff(c);
		// 1. 计算获得客户端（待同步文件）的文件签名
		InputStream inputStream=new FileInputStream(filePath);
		List<ChecksumPair> pairs = rdf.makeSignatures(inputStream);
		inputStream.close();
		String sigNatureName = serverConfig.getName() + task.getTaskId() + RandomUtil.randomString(3) + ".txt";
		// 2. 将文件签名写入输出流,调用对方提供的方法
		OutputStream outputStream = new FileOutputStream(sigNatureName);
		rdf.writeSignatures(pairs, outputStream);
		outputStream.close();

		// 拼写源主机名字
		String ownHost = task.getOwnHost();
		String url = String.format("%s//deliverySignatures", ownHost);
		System.out.println(url);
		FileSystemResource resource = new FileSystemResource(sigNatureName);

		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("file", resource);
		param.add("hostName", serverConfig.getUrl());
		param.add("sourceFileName", task.getDirectoryName() + File.separator + task.getFileName());
		param.add("dstFileName", filePath);
		param.add("taskId", task.getTaskId());
		String string = restTemplate.postForObject(url, param, String.class);
		System.out.println(string);

		File file = new File(sigNatureName);
		file.delete();

		// // 已经发送信息指纹到同步服务端,将这个信息告诉主节点
		// MultiValueMap<String, Object> paramToMaster = new LinkedMultiValueMap<>();
		// paramToMaster.add("taskId", task.getTaskId());
		// paramToMaster.add("state", Task.complete_clitent_to_server);
		// String masterUrlState = String.format("%s//setTaskState", masterUrl);
		// String reString = restTemplate.postForObject(masterUrlState, paramToMaster,
		// String.class);
		// System.out.println("将信息指纹发送到同步服务端,告诉主节点");
		String info = serverConfig.getUrl() + "-" + task.getTaskId() + "将信息指纹发送到" + ownHost;

		taskSynInformationService.sendSyncProcessInfomation(task.getTaskId(), info);

	}

	/**
	 * 根据同步客户端的文件和差异文件块,重建最新的文件
	 * 
	 * @param dstFileName
	 *            同步客户端对应的文件
	 * @param is
	 *            差异文件块输入流
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	@Async
	public void reBuildFile(String dstFileName, String taskId, InputStream is)
			throws IOException, NoSuchAlgorithmException {
		Security.addProvider(new RsyncProvider());
		Configuration c = new Configuration();
		Rdiff rdf = new Rdiff(c);
		// 6. 客户端读取差异流
		List<Delta> deltas2 = rdf.readDeltas(is);
		// 7. 客户端根据差异流，生成最新的文件
		OutputStream outputStream=new FileOutputStream(dstFileName+"tmp");
		File file=new File(dstFileName);
		rdf.rebuildFile(file, deltas2, outputStream);
		outputStream.close();
		file.delete();
		
		File afterfile=new File(dstFileName+"tmp");
		afterfile.renameTo(new File(dstFileName));
		

		// // 已经同步完成 告诉主节点
		// MultiValueMap<String, Object> paramToMaster = new LinkedMultiValueMap<>();
		// paramToMaster.add("taskId", taskId);
		// paramToMaster.add("state", Task.cliten_complete_rebuild);
		// String masterUrlState = String.format("%s//setTaskState", masterUrl);
		// String reString = restTemplate.postForObject(masterUrlState, paramToMaster,
		// String.class);
		// System.out.println("重构文件完成,告诉主节点");

		String info = serverConfig.getUrl() + "-" + taskId + "-"+dstFileName+"信息同步完成";

		taskSynInformationService.sendSyncProcessInfomation(taskId, info);

	}

}
