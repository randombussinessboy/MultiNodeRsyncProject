package com.zhaoyanyang.dfss.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zhaoyanyang.dfss.job.TaskSyncJob;

@Configuration
public class QuartzConfiguration {

	 /**
	  * 中断间隔为一分钟 Interval时间单位为一分钟
	  */
	 private static final int interval = 1;
	 
	    @Bean
	    public JobDetail weatherDataSyncJobDetail() {
	        return JobBuilder.newJob(TaskSyncJob.class).withIdentity("TaskSyncJob")
	        .storeDurably().build();
	    }
	     
	    @Bean
	    public Trigger weatherDataSyncTrigger() {
	        SimpleScheduleBuilder schedBuilder = SimpleScheduleBuilder.simpleSchedule()
	                .withIntervalInMinutes(interval).repeatForever();
	         
	        return TriggerBuilder.newTrigger().forJob(weatherDataSyncJobDetail())
	                .withIdentity("TaskSyncTrigger").withSchedule(schedBuilder).build();
	    }
}
