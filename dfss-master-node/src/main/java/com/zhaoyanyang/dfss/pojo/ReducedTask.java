package com.zhaoyanyang.dfss.pojo;

import java.util.ArrayList;
import java.util.List;

//精简的任务类,又来返回给前端系统概况那里

//任务Id 任务类型    运行状态   源  目标
public class ReducedTask {
	
	//打算把任务那个转换成服务名字,其它没有变

	//任务Id
	String taskId;
	
	//任务类型 有一对一同步实时任务, 一对多同步实时任务 ,一对一定时任务, 一对一多定时任务
	String taskType;
	
	
	//任务状态 正在运行 暂停
	String taskState;
	
	//源主机加目录加文件
	String source;
	
	//目的主机加目录加文件
	List<String> destinations=new ArrayList<String>();

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<String> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<String> destinations) {
		this.destinations = destinations;
	}
	
	
	
	
	
}
