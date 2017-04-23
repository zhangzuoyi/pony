package com.zzy.pony.dao;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Group;



public interface UserGroupDao extends JpaRepository<Group,Integer>{
	List<Group> findByGroupTypeAndName(String groupType,String groupName);
	List<Group> findByGroupType(String groupType);
	List<Group> findByName(String name);

}
