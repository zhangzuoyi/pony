package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Role;
import com.zzy.pony.vo.RoleVo;

public interface RoleService {
	
	List<Role>  findAll();
	List<RoleVo> list();
	void add(Role role);
	void update(Role role);
	void delete(String	roleCode);
	Role get(String roleCode);
	Boolean isExist(String roleCode);
	
	
	
	
	
	
}
