package com.zhaoyanyang.dfss.pojo;

/**
 * 各个微服务的类,包括服务名 和服务对应的URL
 * @author yangzy
 *
 */
public class clusterService {
	
	public String serviceName;
	public String url;
	
	/**
	 * 获取服务名
	 * @return
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * 设置服务名
	 * @param serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * 设置服务的地址
	 * @return
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 获取服务的地址
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	

}
