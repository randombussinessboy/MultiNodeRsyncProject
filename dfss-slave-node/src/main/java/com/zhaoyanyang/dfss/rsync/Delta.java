package com.zhaoyanyang.dfss.rsync;

/**
 * Delta接口，Delta节点连接起来的就是一个逻辑上的同步源文件
 * 如果这个数据块在同步目的文件上存在，这个Dleta只要表明这个块在同步目的文件
 * 中的序号，和在新文件中（就是同名源文件）的序号方便目的节点组装新文件
 * 
 * @see DataBlock
 * @see Offsets
 */
public interface Delta {
	/**
	 * 返回所代表数据的长度.
	 * 
	 * @since 1.1
	 * @return 所代表数据的长度.
	 */
	int getBlockLength();

	/**
	 * 返回这份数据在同步源文件中的偏移量
	 * 
	 * @since 1.2
	 * @return 偏移量.
	 */
	long getWriteOffset();
}