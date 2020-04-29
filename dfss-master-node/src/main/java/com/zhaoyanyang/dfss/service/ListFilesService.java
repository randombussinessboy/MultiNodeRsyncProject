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


@Service
public class ListFilesService {

	@Autowired RestTemplate restTemplate;
	/**
	 * 
	 * @param dfssFile 所要遍历的目录
	 * @return 返回目录下所有的文件夹和文件
	 */
	@HystrixCommand(fallbackMethod = "slave_node_file_system_not_connected")
	public List<DfssFile> ListDirectory(DfssFile dfssFile) {
		
		String ownHost=dfssFile.getOwnHost();
		String directory=dfssFile.getFileName();
	
        String url = String.format("%s/listFile",ownHost);
        System.out.println(url);
        
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("directory", directory);
		
		DfssFile[] tmp=restTemplate.postForObject(url, paramMap,DfssFile[].class);
		return Arrays.asList(tmp);
	}
	
	/**
	 * 返回一个目录下面的所有子文件,级联跟踪。
	 * @param dfssFile
	 * @return
	 */
	
	public List<DfssFile> allSubFiles(DfssFile dfssFile){
		
	   	 String ownHost=dfssFile.getOwnHost();
		 String directory=dfssFile.getFileName();
		 String url = String.format("%s/allSubFiles",ownHost);
		 MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
		 paramMap.add("directory", directory);
		 DfssFile[] tmp=restTemplate.postForObject(url, paramMap,DfssFile[].class);
		 return Arrays.asList(tmp);
		
	}
	
	public List<DfssFile> slave_node_file_system_not_connected(DfssFile adfssFile) {
		ArrayList<DfssFile> lsArrayList=new ArrayList<>();
		DfssFile dfssFile=new DfssFile();
		dfssFile.setFileName("从节点丢失,无法读取文件或者目录");
		lsArrayList.add(dfssFile);
		return lsArrayList;
	}
}
