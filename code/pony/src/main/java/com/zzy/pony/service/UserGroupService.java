package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Group;







public interface UserGroupService {
	List<Group> list();
	List<Group> listByCondition(String groupType,String groupName);
	Group get(Integer groupId);
	//用户是否在组内
	Boolean isExist(int userId,int[] groupIds);

	
	void add(Group group);
	void update(Group group);
	void delete(Group group);
}
