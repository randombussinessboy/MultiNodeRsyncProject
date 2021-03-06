package com.zhaoyanyang.dfss.pojo;

import java.util.List;

/**
 * 这也是一个任务类 会跟Task转换
 * @author yangzy
 *
 */
public class TaskInfo {

	    
	    private String fileUrl;//文件夹绝对路径
	    private String sourceUrl;//源节点url
	    private boolean isDirectonry;
	    private boolean isTimmingTask;
	    private Integer minutes;
	    private String taskId;

	    public String getTaskId() {
			return taskId;
		}

		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}

		List<TaskTarget> targetList; //目的节点列表


	    public String getFileUrl() {
	        return fileUrl;
	    }

	    public void setFileUrl(String fileUrl) {
	        this.fileUrl = fileUrl;
	    }

	    public String getSourceUrl() {
	        return sourceUrl;
	    }

	    public void setSourceUrl(String sourceUrl) {
	        this.sourceUrl = sourceUrl;
	    }

	    public boolean isDirectonry() {
	        return isDirectonry;
	    }

	  

	    public void setDirectonry(boolean isDirectonry) {
			this.isDirectonry = isDirectonry;
		}

		public void setTimmingTask(boolean isTimmingTask) {
			this.isTimmingTask = isTimmingTask;
		}

		public boolean isTimmingTask() {
	        return isTimmingTask;
	    }

	 

	    public Integer getMinutes() {
	        return minutes;
	    }

	    public void setMinutes(Integer minutes) {
	        this.minutes = minutes;
	    }

	    public List<TaskTarget> getTargetList() {
	        return targetList;
	    }

	    public void setTargetList(List<TaskTarget> targetList) {
	        this.targetList = targetList;
	    }
}
