package com.zhaoyanyang.dfss.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zhaoyanyang.dfss.config.ServerConfig;
import com.zhaoyanyang.dfss.pojo.DfssFile;

@Service
public class ListFilesService {
	
	@Autowired ServerConfig serverConfig;
	
	
	/**
	 * 返回目录下的文件和文件夹
	 * @param FileDirectory 目录的绝对路径
	 * @return 返回该目录下的文件集合
	 */
	 public List<DfssFile> getDirectoryContent(String FileDirectory){
			
		 List<DfssFile> content=new ArrayList<>();
		 File file = new File(FileDirectory);
		 File[] files = file.listFiles();  
		 for (File f : files){  
			 
	            if (f.isDirectory()) {
	            	DfssFile tmp=new DfssFile();
	            	tmp.setFileName(f.getName());
	            	tmp.setOwnHost(serverConfig.getUrl());
					tmp.setDfssFiletype(DfssFile.DFSS_FILE_TYPE_DIRECTORY);
					tmp.setHostName(serverConfig.getName());
					content.add(tmp);
				}else {
					DfssFile tmp=new DfssFile();
	            	tmp.setFileName(f.getName());
					tmp.setDfssFiletype(DfssFile.DFSS_FILE_TYPE_FILE);
					tmp.setOwnHost(serverConfig.getUrl());
					tmp.setHostName(serverConfig.getName());
					content.add(tmp);
				}
	            
	            
	        }  
		 return content;
	 }
}
