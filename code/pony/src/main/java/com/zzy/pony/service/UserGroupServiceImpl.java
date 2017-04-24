package com.zzy.pony.service;


import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.UserGroupDao;
import com.zzy.pony.model.Group;



@Service
@Transactional
public class UserGroupServiceImpl implements UserGroupService {

	@Autowired
	private UserGroupDao userGroupDao;
	
	@Override
	public List<Group> list() {
		// TODO Auto-generated method stub
		return userGroupDao.findAll();
	}

	@Override
	public List<Group> listByCondition(String groupType, String groupName) {
		// TODO Auto-generated method stub
		List<Group> result = new ArrayList<Group>();
		if (!StringUtils.isEmpty(groupType) && !StringUtils.isEmpty(groupName)) {
			result = userGroupDao.findByGroupTypeAndName(groupType, groupName);
		}
		if (!StringUtils.isEmpty(groupType) && StringUtils.isEmpty(groupName)) {
			result = userGroupDao.findByGroupType(groupType);
		}
		if (StringUtils.isEmpty(groupType) && !StringUtils.isEmpty(groupName)) {
			result = userGroupDao.findByName(groupName);
		}
		if (StringUtils.isEmpty(groupType) && StringUtils.isEmpty(groupName)) {
			result = userGroupDao.findAll();
		}
		
		return result;
	}

	@Override
	public void add(Group group) {
		// TODO Auto-generated method stub
		userGroupDao.save(group);
	}

	@Override
	public void update(Group group) {
		// TODO Auto-generated method stub
		Group old = userGroupDao.findOne(group.getGroupId());
		old.setGroupType(group.getGroupType());
		old.setName(group.getName());
		old.setUsers(group.getUsers());
		userGroupDao.save(old);
	}

	@Override
	public void delete(Group group) {
		// TODO Auto-generated method stub
		userGroupDao.delete(group);
	}

	@Override
	public Group get(Integer groupId) {
		// TODO Auto-generated method stub
		return userGroupDao.findOne(groupId);
	}

	
	
	
	
	

	
	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
