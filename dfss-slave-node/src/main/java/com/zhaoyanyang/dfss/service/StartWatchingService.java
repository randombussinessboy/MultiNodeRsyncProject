package com.zhaoyanyang.dfss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zhaoyanyang.dfss.listener.FileChangesListenner;
import com.zhaoyanyang.dfss.pojo.Task;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

@Service
public class StartWatchingService {
	
	@Value("${rabbitmq.hostName}")
	String rabbitmqhost;
	/**
	 * 将任务所拥有的文件加入监控
	 * @param task 任务
	 * @return 返回watchId
	 */
	public int addMonitor(Task task) {
		int mask = JNotify.FILE_CREATED // 文件创建
				| JNotify.FILE_DELETED // 文件删除
				| JNotify.FILE_MODIFIED // 文件修改
				| JNotify.FILE_RENAMED; // 文件改名
		boolean watchSubtree = true;
		int watchID;

		try {
			watchID = JNotify.addWatch(task.getDirectoryName(), mask, watchSubtree, new FileChangesListenner(task,rabbitmqhost));
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return 0;
		}
		task.setWatchId(String.valueOf(watchID));
		return watchID;

	}
	
	/**
	 * 移除文件监控
	 * @param watchId
	 * @return
	 */

	public Boolean removeMonitor(int watchId) {
		try {
			return JNotify.removeWatch(watchId);
		} catch (JNotifyException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
	}
}
