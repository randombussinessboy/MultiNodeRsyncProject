package com.zhaoyanyang.dfss.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zhaoyanyang.dfss.DfssSlaveApplication;
import com.zhaoyanyang.dfss.service.ListFilesService;
import com.zhaoyanyang.dfss.service.StartWatchingService;
import com.zhaoyanyang.dfss.pojo.DfssFile;
import com.zhaoyanyang.dfss.pojo.Task;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DfssSlaveApplication.class)
public class TestSlaveNode {

	@Autowired ListFilesService listFilesService;
	@Autowired StartWatchingService watchService;
	
	@Test
	public void test() {
		
		Task task=new Task();
		task.setDirectoryName("E:\\毕业设计项目代码");
		task.setTargetType(Task.DIRECTORY_TASK);
		task.setQueueName("testPosess");
		Task task2=new Task();
		task2.setDirectoryName("E:\\兼职赚钱");
		task2.setTargetType(Task.DIRECTORY_TASK);
		task2.setQueueName("testPosess2");
		watchService.addMonitor(task);
		watchService.addMonitor(task2);
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
         
    }
}
