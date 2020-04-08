package com.zhaoyanyang.dfss.job;

import java.util.ArrayList;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.zhaoyanyang.dfss.pojo.Task;
import com.zhaoyanyang.dfss.service.StartSyncService;
import com.zhaoyanyang.dfss.service.TaskQueueService;

import cn.hutool.core.date.DateUtil;

public class TaskSyncJob extends QuartzJobBean {

	@Autowired
	private TaskQueueService taskService;
	@Autowired StartSyncService startSyncService;

	@Override
	/**
	 * 每隔一分钟中断一次,遍历任务列表查看时间是否合适，开启同步任务。
	 * 
	 * @param context
	 *            Quartz框架会自动传入应用上下文
	 * @throws JobExecutionException
	 */
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		System.out.println("定时启动：" + DateUtil.now());
		
		ArrayList<Task> taskList = taskService.getTaskArray();
		for (Task task : taskList) {
			if (task.isRunning()) {
				if (task.getTaskType()==Task.REAL_TIME_TASK) {
					/*
					 * 调用同步服务,开启服务。
					 */
					
					try {
						startSyncService.pollMessageAndSync(task);
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}else if (task.getTaskType()==Task.TIMING_TASK) {
					/*
					 * 判断有同步次数的限制没,然后进行相应操作
					 */
					int timmingTaskMinutes=task.getMinutesCount();
					timmingTaskMinutes++;
					task.setMinutesCount(timmingTaskMinutes);
					if (task.getMinutesCount()==task.getInterval()) {
						task.setMinutesCount(0);//重新计数
						if(task.getUsageCount()==Task.TIMMING_TASK_UNLIMIT) {
							/*
							 * 没有次数限制的任务
							 */
						}else {
							int usageCount=task.getUsageCount();
							usageCount--;
							/*
							 * 开始同步任务
							 */
							/*
							 * 如果剩余次数为0的话则应该把当前任务结束
							 */
							if(usageCount==0) {
								taskService.removeTask(task);
							}
						}
					}
					
				} 
			}
		}

		System.out.println("定时结束：" + DateUtil.now());

	}

}
