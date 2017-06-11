package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.dao.RoleDao;
import com.zzy.pony.model.Resource;
import com.zzy.pony.model.Role;
import com.zzy.pony.vo.RoleVo;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleDao roleDao;

	
	
	
	@Override
	public List<Role> findAll() {
		// TODO Auto-generated method stub
		return roleDao.findAll();
	}

	@Override
	public List<RoleVo> list() {
		// TODO Auto-generated method stub
		List<RoleVo> result = new ArrayList<RoleVo>();
		List<Role> roles = roleDao.findAll();
		for (Role role : roles) {
			RoleVo vo = new RoleVo();
			vo.setRoleCode(role.getRoleCode());
			vo.setRoleName(role.getRoleName());
			List<Integer> resourceList = new ArrayList<Integer>();
			for (Resource resource : role.getResources()) {
				resourceList.add(resource.getResId());
			}
			vo.setResources(resourceList.toArray(new Integer[0]));
			result.add(vo);
		}
		
		return result;
	}

	@Override
	public void add(Role role) {
		// TODO Auto-generated method stub
		roleDao.save(role);				
	}

	@Override
	public void update(Role role) {
		// TODO Auto-generated method stub
		Role old = roleDao.findOne(role.getRoleCode());
		old.setRoleName(role.getRoleName());
		old.setResources(role.getResources());
		roleDao.save(role);
	}

	@Override
	public void delete(String roleCode) {
		// TODO Auto-generated method stub
		roleDao.delete(roleCode);
	}

	@Override
	public Role get(String roleCode) {
		// TODO Auto-generated method stub
		return roleDao.findOne(roleCode);
	}

	@Override
	public Boolean isExist(String roleCode) {
		// TODO Auto-generated method stub
		Role role = roleDao.findOne(roleCode);
		if (role == null) {
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	

	
	
	
	
	

}
