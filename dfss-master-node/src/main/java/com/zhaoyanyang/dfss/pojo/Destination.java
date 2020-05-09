package com.zhaoyanyang.dfss.pojo;
/**
 * 任务目的
 * @author yangzy
 *
 */
public class Destination {

	
	/**
	 * 同步客户端IP地址和端口号
	 */
	private String url;
	/**
	 *同步客户端对应目录名 
	 */
	private String directoryName;
	
	/**
	 * 同步客户端对应文件名
	 */
	private String fileName;

	/**
	 * 同步客户端IP加端口号
	 * @return
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置同步客户端IP加端口号
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取同步客户端目录名
	 * @return
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * 设置同步客户端目录名
	 * @param directoryName
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	/**
	 * 获取同步客户端文件名
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 设置同步客户端文件名
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
