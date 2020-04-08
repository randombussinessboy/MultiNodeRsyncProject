package com.zhaoyanyang.dfss.pojo;

import java.io.Serializable;


/**
 * 这个类用来抽象分布式文件系统里面的各个文件
 * @author yangzy
 *
 */
public class DfssFile implements Serializable{
	
	/**
	 * 这个属性用来表示是文件类型
	 */
	public static final int DFSS_FILE_TYPE_FILE=1;
	/**
	 * 这个属性用来表示是目录类型
	 */
	public static final int DFSS_FILE_TYPE_DIRECTORY=2;
	private String fileName;
	private String ownHost;
	private String hostName;
	/**
	 * 
	 * @return
	 * 所属的服务名字
	 */
	public String getHostName() {
		return hostName;
	}
	
	/**
	 * 设置所属服务的名字
	 * @param hostName
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	/**
	 * IP地址和端口号
	 * @return
	 */
	public String getOwnHost() {
		return ownHost;
	}
	/**
	 * 设置IP地址和端口号
	 * @param ownHost
	 */
	public void setOwnHost(String ownHost) {
		this.ownHost = ownHost;
	}
	private int DfssFiletype;
	/**
	 * 返回文件或者目录的名字
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 设置文件或者目录的名字
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 文件类型,是文件还是文件夹
	 * @return
	 */
	public int getDfssFiletype() {
		return DfssFiletype;
	}
	/**
	 * 
	 * @param dfssFiletype
	 * 设置文件类型 包括 DFSS_FILE_TYPE_FILE和DFSS_FILE_TYPE_DIRECTORY
	 */
	public void setDfssFiletype(int dfssFiletype) {
		DfssFiletype = dfssFiletype;
	}
	
}