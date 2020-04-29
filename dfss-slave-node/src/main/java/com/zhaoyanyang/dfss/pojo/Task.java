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
	
	/*
	 * 任务的状态不是百分比,正在运行的项目,一个轮次的状态有等待同步 
	 * 进行同步 完了回到5等待同步
	 * 进行同步有 状态1.任务已经发送到同步客户端主机
	 * 2.同步客户端主机已经将文件指纹发送给同步服务端
	 * 3.同步服务端完成差异比对,将差异文件发回同步客户端
	 * 4.同步客户端完成文件重构,一轮同步任务完成。
	 * 
	 */
	
	
	private int  proegress;
	
	
	
	
	/**
	 * 获取该任务对应的状态,可以知道任务在哪个阶段
	 * 如果获取到的任务状态是客户端完成重建，需要自己设置回等待同步
	 * @return
	 */
	public int getProegress() {
		return proegress;
	}
	/**
	 * 设置任务的状态
	 * @param proegress
	 */
	public void setProegress(int proegress) {
		this.proegress = proegress;
	}

	public static final int wait_sync=7;
	public static final int complete_send_to_client=8;
	public static final int complete_clitent_to_server=9;
	public static final int complete_server_to_client=10;
	public static final int cliten_complete_rebuild=11;
	
	
	
	//目录同步需要用到子文件
	private ArrayList<String> catagoryTaskSubfiles=new ArrayList<>();
	
	
	/**
	 * 目录同步任务中 获取目录里面的所有子文件
	 * @return
	 */
	public ArrayList<String> getCatagoryTaskSubfiles() {
		return catagoryTaskSubfiles;
	}
	/**
	 * 目录同步任务中设置目录下的所有 子文件
	 * @return
	 */
	public void setCatagoryTaskSubfiles(ArrayList<String> catagoryTaskSubfiles) {
		this.catagoryTaskSubfiles = catagoryTaskSubfiles;
	}

	private int interval;//如果是定时任务 同步的时间间隔 单位为分钟
	private int usageCount;//定时任务的次数,-1为无数次

	private boolean mutipleDestination;//是否为一对多同步
	private ArrayList<Destination> destinations=new ArrayList<>();
	
	/**
	 * 返回一个该任务对应的同步客户端 包含该客户端URL 文件目录名 文件名
	 * @return
	 */
	public ArrayList<Destination> getDestinations() {
		return destinations;
	}
	/**
	 * 添加一个同步客户端到该任务 
	 * @param destinations
	 */
	public void setDestinations(ArrayList<Destination> destinations) {
		this.destinations = destinations;
	}

	private String ownHost;//源主机
	private int targetType;//监控的是目录,还是单个文件。
	private String directoryName;//源文件目录
	private String fileName;//源文件文件名称
	private String targetDirectoryName;//设置这次同步的文件夹
	private String targetFileName;//设置这次同步的文件夹和目录
	
	
	
	/**
	 * 获取该任务对应的目标目录
	 * @return
	 */
	public String getTargetDirectoryName() {
		return targetDirectoryName;
	}
	/**
	 * 设置目标目录
	 * @param targetDirectoryName
	 */
	public void setTargetDirectoryName(String targetDirectoryName) {
		this.targetDirectoryName = targetDirectoryName;
	}
	/**
	 * 获取目标文件名字
	 * @return
	 */
	public String getTargetFileName() {
		return targetFileName;
	}
	
	/**
	 * 设置目标文件名字
	 * @return
	 */
	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}

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
	 * 返回源主机 包括IP地址和端口号 已经有HTTP协议头
	 * @return
	 */
	public String getOwnHost() {
		return ownHost;
	}
	/**
	 * 设置源主机 包括IP地址和端口号 记住加入HTTP:头 跟URL那边相统一
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
	 * 返回单次监控的目录名
	 * @return
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * 设置单次监控的文件名
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