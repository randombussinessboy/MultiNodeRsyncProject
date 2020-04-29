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
	@Autowired
	StartSyncService startSyncService;

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

		try {

			ArrayList<Task> taskList = taskService.getTaskArray();
			for (Task task : taskList) {
				if (task.isRunning()) {
					if (task.getTargetType() == Task.FILE_TASK) {

						if (task.getTaskType() == Task.REAL_TIME_TASK) {
							/*
							 * 调用同步服务,开启服务。
							 */
							System.out.printf("任务%s是实时同步任务,正在进行同步", task.getTaskId());
							System.out.println();
							startSyncService.pollMessageAndSync(task);

						} else if (task.getTaskType() == Task.TIMING_TASK) {
							/*
							 * 判断有同步次数的限制没,然后进行相应操作
							 */
							int timmingTaskMinutes = task.getMinutesCount();
							timmingTaskMinutes++;
							task.setMinutesCount(timmingTaskMinutes);

							System.out.printf("任务%s是定时同步任务,现在已经过了%d分钟", task.getTaskId(), timmingTaskMinutes);
							System.out.println();

							if (task.getMinutesCount() == task.getInterval()) {
								task.setMinutesCount(0);// 重新计数
								if (task.getUsageCount() == Task.TIMMING_TASK_UNLIMIT) {
									/*
									 * 没有次数限制的任务
									 */
									System.out.printf("任务%s是定时同步任务,正在进行同步/n", task.getTaskId());
									startSyncService.pollMessageAndSync(task);

								} else {
									int usageCount = task.getUsageCount();
									usageCount--;
									task.setUsageCount(usageCount);

									System.out.printf("任务%s是有次数定时同步任务,正在进行同步,现在还剩余%d次", task.getTaskId(), usageCount);
									System.out.println();
									/*
									 * 开始同步任务
									 */

									startSyncService.pollMessageAndSync(task);

									/*
									 * 如果剩余次数为0的话则应该把当前任务结束
									 */
									if (usageCount == 0) {
										System.out.printf("任务%s是定时同步任务,次数用完", task.getTaskId());
										System.out.println();
										taskService.removeTask(task);
									}
								}
							}

						}

					} else if (task.getTargetType() == Task.DIRECTORY_TASK) {

					
						int timmingTaskMinutes = task.getMinutesCount();
						timmingTaskMinutes++;
						task.setMinutesCount(timmingTaskMinutes);

						if (task.getMinutesCount() == task.getInterval()) {
							task.setMinutesCount(0);// 重新计数

							System.out.printf("目录任务%s是定时同步任务,正在进行同步/n", task.getTaskId());
							startSyncService.pollCatogoryMessageAndSync(task);

						}

					}
				}
			}

		} catch (Exception E) {
			E.printStackTrace();
		}

		System.out.println("定时结束：" + DateUtil.now());

	}

}
