package com.zhaoyanyang.dfss.rsync;

import java.util.Arrays;



/**
 * Rsync算法中每一个数据块对应的强校验和和弱校验和，弱检验和是用Adler32算法计算出来的
 * 强校验和用MD4算法计算出来的
 * @author yangzy
 *
 */
public class ChecksumPair implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	// 变量.
	// -------------------------------------------------------------------------

	/**
	 * 弱检验和
	 * 
	 * @since 1.1
	 */
	int weak;

	/**
	 * 强校验和
	 * 
	 * @since 1.1
	 */
	StrongKey strong;

	/**
	 * 这个数据块在原始数据中的偏移量
	 */
	long offset;

	/** 这个数据块的长度. */
	int length;

	/** 这个数据块的序号，是第几个数据库. */
	int seq;

	// 构造器部分.
	// -------------------------------------------------------------------------

	/**
	 * 新建一对新的校验和.
	 * 
	 * @param weak
	 *            弱校验和
	 * @param strong
	 *            强校验和
	 * @param offset
	 *           计算校验和的这个数据块的偏移量
	 * @param length
	 *           计算校验和的这个数据块的长度
	 * @param seq
	 *           计算这对校验和的数据快的序号
	 */
	public ChecksumPair(int weak, byte[] strong, long offset, int length,
			int seq) {
		this.weak = weak;
		this.strong = new StrongKey(strong);
		this.offset = offset;
		this.length = length;
		this.seq = seq;
	}

	/**
	 *新建一对校验和,不填充序号和长度字段，因为我们默认就可以了
	 * 
	 * @param weak
	 *           弱校验和.
	 * @param strong
	 *            强校验和.
	 * @param offset
	 *           数据库偏移量.
	 */
	public ChecksumPair(int weak, byte[] strong, long offset) {
		this(weak, strong, offset, 0, 0);
	}

	/**
	 * 新建一对校验和，没有偏移字段
	 * 
	 * @param weak
	 *           弱校验和.
	 * @param strong
	 *           强校验和.
	 */
	public ChecksumPair(int weak, byte[] strong) {
		this(weak, strong, -1L, 0, 0);
	}

	/**
	 * 默认的无参数构成方法
	 */
	ChecksumPair() {
	}

	// 实例方法.
	// -------------------------------------------------------------------------

	/**
	 * 获取弱校验码.
	 * 
	 * @return 弱校验码.
	 * @since 1.1
	 */
	public int getWeak() {
		return weak;
	}

	/**
	 * 获取强校验码.
	 * 
	 * @return 强校验码.
	 * @since 1.1
	 */
	public StrongKey getStrong() {
		return strong;
	}

	/**
	 * 返回数据块偏移量
	 * 
	 * @return 偏移量.
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * 返回被计算的数据库长度
	 * 
	 * @return 长度.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * 返回数据块的序号 第一块 第二块
	 * 
	 * @return 返回的序号.
	 */
	public int getSequence() {
		return seq;
	}

	// 重写object类的方法
	// -------------------------------------------------------------------------

	public int hashCode() {
		return weak;
	}

	/**
	 * 我们重写了对象比较的方法，如果两个校验值都相等
	 * 我们认为这两个数据块是相同的 注意这个checksum抽象了一个数据块的内容
	 * 
	 * 
	 * @param obj
	 *            被比较的对象
	 * @return 如果相等返回正确
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof StrongKey))
			throw new ClassCastException(obj.getClass().getName());		
		
		ChecksumPair pair = (ChecksumPair)obj;
		
		if (this == pair)
			return true;
		
		return weak == (pair).weak
				&& this.strong.equals(pair.getStrong());
	}

	/**
	 * 重写toString方法 输出对象的时候会调用这个
	 * 
	 * @return 代表这一对校验码的字符串
	 * @since 1.2
	 */
	public String toString() {
		return "len=" + length + " offset=" + offset + " weak=" + weak
				+ " strong=" + Util.toHexString(strong.getBytes());
	}
	
	/**
	 * 强校验码的静态内部类
	 * @author yangzy
	 *
	 */
	public static class StrongKey implements java.io.Serializable, Comparable<StrongKey> {
		private static final long serialVersionUID = 1L;
		// 变量域.
		// --------------------------------------------------------------

		/**
		 * 这个校验和最本身的值. 一个比特数组
		 * 
		 * @since 1.0
		 */
		protected byte[] key;

		// 构造器.
		// --------------------------------------------------------------

		/**
		 * 使用计算完的校验值填充这个对象. <code>key</code> 可以为
		 * <code>null</code>.
		 * 
		 * @since 1.0
		 * @param key
		 *            这个比特数组就是强校验和
		 */
		StrongKey(byte[] key) {
			if (key != null)
				this.key = (byte[]) key.clone();
			else
				this.key = key;
		}

		// 实例方法.
		// --------------------------------------------------------------

		/**
		 * 返回强校验码具体的值.
		 * 
		 * @since 1.0
		 * @return {@link #key}, 校验码比特数组.
		 */
		public byte[] getBytes() {
			if (key != null)
				return (byte[]) key.clone();
			return null;
		}

		/**
		 * 设置这个key的比特数组.
		 * 
		 * @since 1.0
		 * @param key
		 *            这个key对象真正的比特数组.
		 */
		public void setBytes(byte[] key) {
			if (key != null)
				this.key = (byte[]) key.clone();
			else
				this.key = key;
		}

		/**
		 * 这个比特数组的长度
		 * 
		 * @since 1.0
		 * @return 这个强校验码的比特长度.
		 */
		public int length() {
			if (key != null)
				return key.length;
			return 0;
		}

		// 重写object类的方法 -----------

		/**
		 *返回强校验码的一个十六进制的表示
		 * 
		 * @return 十六进制表示{@link #key}.
		 * @since 1.0
		 */
		public String toString() {
			if (key == null || key.length == 0)
				return "nil";
			return Util.toHexString(key);
		}

		/**
		 * 这个比特数组的每32位做异或的结果
		 * blocks of the {@link #key} array.
		 * 
		 * @return 这个强校验码的hash值.
		 * @since 1.0
		 */
		public int hashCode() {
			if (key == null)
				return 0;
			int code = 0;
			for (int i = key.length - 1; i >= 0; i--)
				code ^= ((int) key[i] & 0xff) << (((key.length - i - 1) * 8) % 32);
			return code;
		}

		/**
		 * 两个强校验值是否相等 ，直接比较比特数组是不是相同
		 * {@link java.util.Arrays#equals(byte[],byte[])} 
		 * 值相同返回true
		 * 
		 * @since 1.0
		 * @throws java.lang.ClassCastException
		 *             If o is not a StrongKey.
		 * @param o
		 *            被比较的强校验对象
		 * @return <tt>true</tt> 这两个强校验码相同.
		 */
		public boolean equals(Object o) {
			return Arrays.equals(key, ((StrongKey) o).key);
		}

		// java.lang.Comparable 接口方法的实现 -----------------

		/**
		 * 这个方法是实现来比较两个对象大小的
		 * <ul>
		 * <li>0 如果 {@link #key}两个引用了相同的对象.
		 * <li>1 如果 {@link #key} 一个类为空
		 * <li>-1 如果 {@link #key} 在 <tt>o</tt> 是空的 (空总是小于如何对象
		 * ).
		 * <li>0 如果 {@link #key} 长度相同内容也相同
		 * 
		 * <li>连长度都不同的key肯定是不同的.
		 * <li>长度相同 校验值不同的也是不同的
		 * </ul>
		 * 
		 * @since 1.0
		 * @throws java.lang.ClassCastException
		 *             If o is not a StrongKey.
		 * @param o
		 *            被比较的对象.
		 * @return 这两个key的比较结果.
		 */
		public int compareTo(StrongKey sk) {
			if (key == sk.key)
				return 0;
			if (key == null)
				return 1;
			if (sk.key == null)
				return -1;

			if (key.length != sk.length())
				return key.length - sk.length();

			byte[] arr = sk.getBytes();
			for (int i = 0; i < key.length; i++)
				if (key[i] != arr[i])
					return (key[i] - arr[i]);
			return 0;
		}
	}

}