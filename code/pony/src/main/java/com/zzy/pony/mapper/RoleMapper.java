package com.zzy.pony.mapper;

import java.util.List;

public interface RoleMapper {
	List<String> findRoleCodeByUser(int userId);
}
