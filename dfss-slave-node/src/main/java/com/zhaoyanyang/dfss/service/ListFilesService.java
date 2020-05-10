package com.zhaoyanyang.dfss.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.asn1.icao.LDSSecurityObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.internal.function.text.Length;
import com.zhaoyanyang.dfss.config.ServerConfig;
import com.zhaoyanyang.dfss.pojo.DfssFile;

@Service
public class ListFilesService {
	
	@Autowired ServerConfig serverConfig;
	
	private static long directorySize;
	
	
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
					try {
						tmp.setAbosoluteAddr(f.getCanonicalPath());
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					content.add(tmp);
				}else {
					DfssFile tmp=new DfssFile();
	            	tmp.setFileName(f.getName());
					tmp.setDfssFiletype(DfssFile.DFSS_FILE_TYPE_FILE);
					tmp.setOwnHost(serverConfig.getUrl());
					tmp.setHostName(serverConfig.getName());
					try {
						tmp.setAbosoluteAddr(f.getCanonicalPath());
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					content.add(tmp);
				}
	            
	            
	        }  
		 return content;
	 }
	 /**
	  * 获取一个目录下面的所有子文件
	  * @param FileDirectory 目录名称
	  * @return	文件集合
	  * @throws IOException
	  */
	 public List<DfssFile> getDirectoryAllSubfiles(String fileDirectory) throws IOException{
		 List<DfssFile> content=new ArrayList<>();
		 File file = new File(fileDirectory);
		 
		 func(file,content,fileDirectory);
		 
		 return content;
	 }
	 
	 private  void func(File file,List<DfssFile> content,String fileDirectory) throws IOException{
			File[] fs = file.listFiles();
			for(File f:fs){
				if(f.isDirectory())	//若是目录，则递归打印该目录下的文件
					func(f,content,fileDirectory);
				if(f.isFile()) {
					
					DfssFile tmp=new DfssFile();
	            	tmp.setFileName(f.getCanonicalPath().replace(fileDirectory+File.separator, ""));	
					content.add(tmp);
					
					
					System.out.println(f);
				}
					
				
				
			}
	 }
	 
	 /**
	  * 响应目录里面的删除文件操作
	  * @param filePath
	  */
	 @Async
	 public void deleteCatogryFile(String filePath) {
		 
		 	File file = new File(filePath);
			file.delete();
		 
		 
	 }
	 /**
	  * 根据filepath创建文件夹或则文件,TYPE指明是文件还是目录
	  * @param filePath
	  * @param type
	  */
	 @Async
	 public void createCatogryFile(String filePath,int type) {
			if(type==0) {
				
				File file = new File(filePath);
				try {
					file.createNewFile();
					
					
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}else {
				File file = new File(filePath);
				file.mkdirs();
			}
	 }
	 
	 /**
	  * 获取单个文件的长度
	  * @param filePath
	  * @return
	  */
	 public long getFileLength(String filePath) {
		 
		 File file=new File(filePath);
		 return file.length();
		 
	 }
	 
	 /**
	  * 获取整个目录的大小
	  * @param directory
	  * @return
	 * @throws IOException 
	  */
     public long getDirectorySize(String directory) throws IOException {
    	 directorySize=0;
    	 File file = new File(directory);
    	 func1(file);
    	 return directorySize;
     }
     
     private  void func1(File file) throws IOException{
			File[] fs = file.listFiles();
			for(File f:fs){
				if(f.isDirectory())	//若是目录，则递归打印该目录下的文件
					func1(f);
				if(f.isFile()) {
					directorySize+=f.length();
					
				}
					
				
				
			}
	 }
      
}
