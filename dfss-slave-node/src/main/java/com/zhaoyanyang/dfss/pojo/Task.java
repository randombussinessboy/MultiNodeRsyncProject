package com.zhaoyanyang.dfss.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author yangzy
 * 属性type用来指明任务类型 包括实时任务,
 * 
 */
public class Task implements Serializable {
	public static final int REAL_TIME_TASK=1; 
	public static final int TIMING_TASK=2;
	public static final int DIRECTORY_TASK=3;
	public static final int FILE_TASK=4;
	public static final int TIMMING_TASK_UNLIMIT=-1;
	@Deprecated
	private String name;//任务名字
	private String taskId;//跟上面的一样，随机生成的一个表示串
	private int  taskType;//任务类型,包括 实时任务 和定时任务
	private String watchId;//被监控文件夹的监控Id
	private boolean running;//任务是否启动
	private int minutesCount;
	private String queueName;//该任务对应的消息队列名字
	
	
	
	private int interval;//如果是定时任务 同步的时间间隔 单位为分钟
	private int usageCount;//定时任务的次数,-1为无数次

	private boolean mutipleDestination;//是否为一对多同步
	private ArrayList<String> destinationHosts;//同步任务的目的主机,可以为1个或则多个
	private String ownHost;//源主机
	private int targetType;//监控的是目录,还是单个文件。
	private String directoryName;//目标目录的名字
	private String fileName;//如果是文件的话,文件的名字
	
	/**
	 * 返回任务名字,可能跟任务Id那功能一样，留在备用
	 * @return 任务名字
	 */
	@Deprecated
	public String getName() {
		return name;
	}
	
	/**
	 * 设置任务名字
	 * @param name 名字
	 */
	@Deprecated
	public void setName(String name) {
		this.name = name;
	}
	public int getMinutesCount() {
		return minutesCount;
	}

	public void setMinutesCount(int minutesCount) {
		this.minutesCount = minutesCount;
	}

	/**
	 * 返回任务类型，包括实时任务和定时任务
	 * @return REAL_TIME_TASK TIMING_TASK
	 */
	public int getTaskType() {
		return taskType;
	}
	/**
	 * 设置任务类型 实时任务还是定时任务
	 * @param type REAL_TIME_TASK TIMING_TASK
	 */
	public void setTaskType(int taskType) {
		this.taskType= taskType;
	}
	/**
	 * 返回任务是否启动了
	 * @return 
	 */
	public boolean isRunning() {
		return running;
	}
	/**
	 * 设置任务开启与否
	 * @param turn 布尔值
	 */
	public void setRunning(boolean running) {
		this.running=running;
	}
	/**
	 * 返回本任务对应的消息队列名称
	 * @return
	 */
	public String getQueueName() {
		return queueName;
	}
	
	/**
	 * 设置任务对应的消息队列名字
	 * @param queueName 消息队列名字
	 */
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	/**
	 * 返回定时任务的时间间隔 单位为分钟
	 * @return
	 */
	public int getInterval() {
		return interval;
	}
	/**
	 * 设置定时任务的时间间隔
	 * @param interval 单位为分钟
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * 返回剩余同步次数 为-1就是无数次
	 * @return
	 */
	
	public int getUsageCount() {
		return usageCount;
	}
	/**
	 * 设置-1为 没有次数限制 TIMMING_TASK_UNLIMIT
	 * @param usageCount
	 */
	public void setUsageCount(int usageCount) {
		this.usageCount = usageCount;
	}

	/**
	 * 返回目的主机，因为一对多任务的存在 实际上有可能是多个主机
	 * @return
	 */
	public ArrayList<String> getDestinationHosts() {
		return destinationHosts;
	}
	/**
	 * 设置目的任务主机
	 * @param destinationHosts 主机地址localhost 加端口号目前
	 */
	public void setDestinationHosts(ArrayList<String> destinationHosts) {
		this.destinationHosts = destinationHosts;
	}
	
	/**
	 * 返回源主机
	 * @return
	 */
	public String getOwnHost() {
		return ownHost;
	}
	/**
	 * 设置源主机
	 * @param soureHost
	 */
	public void setOwnHost(String ownHost) {
		this.ownHost = ownHost;
	}
	/**
	 * 返回监控的是目录还是单个文件
	 * @return
	 */
	public int getTargetType() {
		return targetType;
	}
	
	/**
	 * 设置监控的是目录还是单个文件
	 * @param targetType
	 */
	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}
	
	/**
	 * 返回监控的目录名
	 * @return
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * 设置监控的文件名
	 * @param directoryName
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	/**
	 * 如果是监控文件的话 需要返回文件名
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 设置监控的文件名 如果是监控文件的话需要
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 是否为一对多任务
	 * @return返回布尔值
	 */
	public boolean isMutipleDestination() {
		return mutipleDestination;
	}
	
	/**
	 * 是否为一对多任务
	 * @param mutipleDestination
	 */
	public void setMutipleDestination(boolean mutipleDestination) {
		this.mutipleDestination = mutipleDestination;
	}
	
	/**
	 * 返回任务Id
	 * @return
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * 设置任务id
	 * @param taskId
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * 被监控目录的id 可用来取消子节点的监听
	 * @return
	 */
	public String getWatchId() {
		return watchId;
	}
	
	/**
	 * 设置监控目录id
	 * @param watchId
	 */
	public void setWatchId(String watchId) {
		this.watchId = watchId;
	}
	
	
	
}