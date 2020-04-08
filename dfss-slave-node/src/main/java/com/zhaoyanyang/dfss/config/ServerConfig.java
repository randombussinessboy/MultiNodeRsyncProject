package com.zhaoyanyang.dfss.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
/**
 * 一些系统设置
 * @author yangzy
 * 
 * 
 *
 */
@Component
public class ServerConfig  implements ApplicationListener<WebServerInitializedEvent> {
    private int serverPort;
    @Value("${spring.application.name}")
    private String hostName;
 
    /**
     * 
     * getUrl() 返回IP地址和端口号
     */
    public String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://"+address.getHostAddress() +":"+this.serverPort;
    }
    
    /**
     * 
     * @return
     * getPort()返回端口号
     */
    public int  getPort() {
		return this.serverPort;
	}
    
    /**
     * 
     * @return
     *  getName() 返回服务名字
     */
    
    public String getName() {
		return this.hostName;
	}
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
        //this.hostName=event.getApplicationContext().getApplicationName();
    }
 
}

