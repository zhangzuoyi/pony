package com.zzy.pony.mapper;

import com.zzy.pony.vo.UserVo;

public interface UserMapper {
	UserVo findByLoginName(String loginName);
}
