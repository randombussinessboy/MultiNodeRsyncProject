package com.zhaoyanyang.dfss;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
  
import cn.hutool.core.util.NetUtil;
/**
 * 这个是服务注册和发现的微服务,主节点通过这个来发现现在有多少个子节点  
 * @author yangzy
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
      
    public static void main(String[] args) {
        //8761 这个端口是默认的，就不要修改了，后面的子项目，都会访问这个端口。
        int port = 8761;
        if(!NetUtil.isUsableLocalPort(port)) {
            System.err.printf("端口%d被占用了，无法启动%n", port );
            System.exit(1);
        }
        new SpringApplicationBuilder(EurekaServerApplication.class).properties("server.port=" + port).run(args);
    }
}