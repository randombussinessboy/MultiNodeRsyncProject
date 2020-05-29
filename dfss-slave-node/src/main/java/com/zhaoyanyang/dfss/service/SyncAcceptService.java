package com.zhaoyanyang.dfss.service;

import java.io.File;
import java.io.FileInputStream;
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
import com.zhaoyanyang.dfss.config.ServerConfig;
import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.rsync.ChecksumPair;
import com.zhaoyanyang.dfss.rsync.Configuration;
import com.zhaoyanyang.dfss.rsync.Delta;
import com.zhaoyanyang.dfss.rsync.Rdiff;
import com.zhaoyanyang.dfss.rsync.RsyncProvider;

import cn.hutool.core.util.RandomUtil;

@Service
public class SyncAcceptService {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ServerConfig serverConfig;

	@Value("${master.ipAddress}")
	String masterUrl;
	
	@Autowired
	TaskSynInformationService taskSynInformationService;

	/**
	 * 根据客服端签名文件比较，客户端文件和服务端文件的差异，并把差异块传输回去
	 * 
	 * @param hostName
	 *            同步客户端的IP和端口
	 * @param sourceFileName
	 *            同步服务端对应的文件
	 * @param dstFileName
	 *            同步客户端对应的文件
	 * @param is
	 *            差异签名文件输入流
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	@Async
	public void sourceHostReturnDelta(String hostName, String sourceFileName, String dstFileName, String taskId,
			InputStream is) throws NoSuchAlgorithmException, IOException {
		
		taskSynInformationService.notifyTasKStateToMaster(taskId, Task.complete_receive_md5);
		Security.addProvider(new RsyncProvider());
		Configuration c = new Configuration();
		Rdiff rdf = new Rdiff(c);
		// 3. 读入客户端解析文件
		List<ChecksumPair> pairs2 = rdf.readSignatures(is);
		// 4. 根据签名文件，服务器与最新文件比较得出差异（增量文件）
		InputStream inputStream=new FileInputStream(sourceFileName);
		List<Delta> deltas = rdf.makeDeltas(pairs2, inputStream);
		
		inputStream.close();
		// 5. 服务器保存差异文件到输出流
		String deltaName = RandomUtil.randomString(5) + "delta.txt";
		OutputStream outputStream = new FileOutputStream(deltaName);
		rdf.writeDeltas(deltas, outputStream);
		outputStream.close();

		// 拼写目的主机名字
		String url = String.format("%s//deliveryDifference", hostName);
		System.out.println(url);
		FileSystemResource resource = new FileSystemResource(deltaName);
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("file", resource);
		param.add("dstFileName", dstFileName);
		param.add("taskId", taskId);
		String string = restTemplate.postForObject(url, param, String.class);
		System.out.println(string);

		File file = new File(deltaName);
		file.delete();

		// // 根据信息指纹 得到差异文件 发送给客户端 并告诉给主节点
		// MultiValueMap<String, Object> paramToMaster = new LinkedMultiValueMap<>();
		// paramToMaster.add("taskId", taskId);
		// paramToMaster.add("state", Task.complete_server_to_client);
		// String masterUrlState = String.format("%s//setTaskState", masterUrl);
		// String reString = restTemplate.postForObject(masterUrlState, paramToMaster,
		// String.class);
		// System.out.println("已将差异文件发送到同步客户端，告诉主节点");

		String info = serverConfig.getUrl() + "-" + taskId + "将差异文件发送到" +hostName;

		taskSynInformationService.sendSyncProcessInfomation(taskId, info);
		taskSynInformationService.notifyTasKStateToMaster(taskId, Task.complete_server_to_client);

	}

}
