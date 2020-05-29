package com.zhaoyanyang.dfss;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

@SpringBootApplication
@EnableEurekaClient
@EnableAsync
public class DfssSlaveApplication {
	public static void main(String[] args) {
	
		int eurekaServerPort = 8761;

//		if (NetUtil.isUsableLocalPort(eurekaServerPort)) {
//			System.err.printf("检查到端口%d 未启用，判断 eureka 服务器没有启动，本服务无法使用，故退出%n", eurekaServerPort);
//			System.exit(1);
//		}
//		
//		 if(NetUtil.isUsableLocalPort(15672)) {
//			 System.err.printf("RabbitMQ 服务器未启动 ");
//	            System.exit(1);
//		 }

	            
		new SpringApplicationBuilder(DfssSlaveApplication.class).
				run(args);

	}
	
	 @Bean
	    RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
}
