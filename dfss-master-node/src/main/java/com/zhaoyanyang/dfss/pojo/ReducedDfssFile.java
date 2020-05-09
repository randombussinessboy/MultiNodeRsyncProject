package com.zhaoyanyang.dfss.pojo;

/**
 * 为跟前端兼容而使用的类
 * @author yangzy
 *兼容DfssFile 重构时两个可以合并 因为写前端时
 *收到前端控件的限制，又不想改后端原来的类
 *所以需要用到适配器模式
 */
public class ReducedDfssFile {

	String name;
	String fileUrl;
	String sourceUrl;
	boolean isParent;
	/**
	 * 获取文件或者目录名
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 *设置文件或者目录名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取所属节点
	 * @return
	 */
	public String getSourceUrl() {
		return sourceUrl;
	}
	/**
	 * 设置所属节点
	 * @param sourceUrl
	 */
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	/**
	 * 获取文件的绝对地址
	 * @return
	 */
	public String getFileUrl() {
		return fileUrl;
	}
	
	/**
	 * 设置文件的绝对地址
	 * @param fileUrl
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	/**
	 * 设置是否是目录
	 * @param isParent
	 */
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	/**
	 * 获取是否是目录
	 * @return
	 */
	public boolean getIsParent() {
		return isParent;
	}
	
	
	
	
}
