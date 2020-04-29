package com.zhaoyanyang.dfss.pojo;

/*
 * 文件改变信息的Pojo类
 */
public class Filechangeinfomation {
	
	 
    private int id;
      
    private String msg;

    /**
     * 信息的id,这个信息是文件系统的信息,不是同步任务的。
     * @return
     */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    /**
     * 获取信息
     * @return
     */
	public String getMsg() {
		return msg;
	}
	
	/**
	 * 设置信息
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
    
    
	
	
}
