package com.zhaoyanyang.dfss.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.zhaoyanyang.dfss.factory.TaskFactory;
import com.zhaoyanyang.dfss.pojo.Destination;
import com.zhaoyanyang.dfss.pojo.DfssFile;
import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.service.ListFilesService;
import com.zhaoyanyang.dfss.service.StartSyncService;
import com.zhaoyanyang.dfss.service.TaskDeliverService;
import com.zhaoyanyang.dfss.service.TaskQueueService;
import com.zhaoyanyang.dfss.service.TaskStateQueryService;

import cn.hutool.core.util.RandomUtil;

@CrossOrigin
@Controller
public class MasterNodeViewController {
	
	@GetMapping("/index")
    public String view() throws Exception {
        return "view";
    }
	
}
