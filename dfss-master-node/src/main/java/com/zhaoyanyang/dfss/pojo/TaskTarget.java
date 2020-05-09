package com.zhaoyanyang.dfss.pojo;

/**
 * 跟Destination是一个东西
 * 为了适配前端新添加的类
 * @author yangzy
 *
 */
public class TaskTarget {

	    private String targetUrl; //目的节点url
	    private String targetFile;//目的节点文件夹地址

	    public String getTargetUrl() {
	        return targetUrl;
	    }

	    public void setTargetUrl(String targetUrl) {
	        this.targetUrl = targetUrl;
	    }

	    public String getTargetFile() {
	        return targetFile;
	    }

	    public void setTargetFile(String targetFile) {
	        this.targetFile = targetFile;
	    }
}
