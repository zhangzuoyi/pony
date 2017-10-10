package com.zzy.pony.mapper;

import java.util.List;

import com.zzy.pony.vo.UserVo;

public interface UserMapper {
	UserVo findByLoginName(String loginName);
	List<UserVo> findPage(int firstRow,int pageSize,String userType,String userName);
	int findCount(String userType,String userName);
}
