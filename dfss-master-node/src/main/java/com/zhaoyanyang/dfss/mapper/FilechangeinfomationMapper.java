package com.zhaoyanyang.dfss.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.zhaoyanyang.dfss.pojo.Filechangeinfomation;

@Mapper
public interface FilechangeinfomationMapper {

	//一个是查询最新十条
	//一个是插入
	
	@Insert(" insert into filechangeinfomation_ ( msg ) values (#{msg}) ") 
    public int add(Filechangeinfomation filechangeinfomation ); 
	
	@Select(" select * from filechangeinfomation_ WHERE id = (SELECT MAX(id) FROM filechangeinfomation_) ") 
	public List<Filechangeinfomation> selectLastTenMsg();
	
	
}
