package com.zhaoyanyang.dfss.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.zhaoyanyang.dfss.pojo.Task;

@Service
/**
 * 进行任务调度的队列，提供了增加，删除，获取，暂停一个任务的方法 此外，如果要修改任务参数，应该先获取该任务，直接修改即可 此处不考虑并发问题
 * 
 * @author yangzy
 *
 */
public class TaskQueueService {

	/**
	 * 正在生命周期中的任务队列
	 */
	ArrayList<Task> taskQueue = new ArrayList<>();
	
	/**
	 * 运行结束,被取消的任务所在的队列，保存这些信息可能是为了之后进一步的分析,针对此队列应该有很多其它操作,等待继续完善
	 * 
	 */
	ArrayList<Task> historyTaskQueue=new ArrayList<>();

	/**
	 * 添加任务到任务队列
	 * 
	 * @param task
	 *            任务
	 */
	public void addTaskToQueue(Task task) {
		taskQueue.add(task);
	}

	/**
	 * 将一个任务移出任务队列
	 * 
	 * @param taskId
	 *            任务ID
	 */
	public void removeTask(String taskId) throws Exception{
		Task readyDelete=null;
		for (Task task : taskQueue) {
			if (task.getTaskId().equals(taskId)) {
				
				readyDelete=task;
			}
		}
		if (readyDelete==null) {
			throw new Exception("该任务Id对应的任务找不到");
		}
		historyTaskQueue.add(readyDelete);
		taskQueue.remove(readyDelete);

	}
	/**
	 * 把任务移除出队列
	 * @param task
	 */
	public void removeTask(Task task) {
		
		task.setRunning(false);
		historyTaskQueue.add(task);
		taskQueue.remove(task);

	}

	/**
	 * 将一个任务挂起
	 * 
	 * @param taskId
	 *            挂起任务
	 */
	public void puaseTask(String taskId) {

		for (Task task : taskQueue) {
			if (task.getTaskId().equals(taskId)) {
				task.setRunning(false);
			}
		}

	}

	/**
	 * 根据任务Id返回任务
	 * 
	 * @param taskId
	 *            任务
	 * @return
	 */
	public Task findTask(String taskId) throws Exception {

		for (Task task : taskQueue) {
			if (task.getTaskId().equals(taskId)) {
				return task;
			}
		}
		throw new Exception("没有找到对应的任务,请检查任务Id是否正确");
	}
	
	/**
	 * 根据文件监控号找出对应的任务
	 * @param watchId
	 * @return
	 * @throws Exception
	 */
	
	public Task findTaskByWatchId(String watchId) throws Exception {

		for (Task task : taskQueue) {
			if (watchId.equals(task.getWatchId())) {
				return task;
			}
		}
		throw new Exception("没有找到对应的任务,查看watchId是否正确");
	}
	
	

	/**
	 * 返回任务队列 注意你不能直接在本数组上面修改，你只能调用已有方法进行修改
	 * 
	 * @return 队列的引用
	 */
	public ArrayList<Task> getTaskArray() {
		return taskQueue;
	}
	
	
	/**
	 * 返回历史任务队列 注意你不能直接在本数组上面修改，你只能调用已有方法进行修改
	 * 
	 * @return 队列的引用
	 */
	public ArrayList<Task> getHistoryTaskArray() {
		return historyTaskQueue;
	}
	
	
	public Task findHistorryTask(String taskId) throws Exception {

		for (Task task : historyTaskQueue) {
			if (task.getTaskId().equals(taskId)) {
				return task;
			}
		}
		throw new Exception("没有找到对应的任务,请检查任务Id是否正确");
	}

}



