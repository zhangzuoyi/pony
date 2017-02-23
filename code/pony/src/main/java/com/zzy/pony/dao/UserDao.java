package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zzy.pony.model.Role;
import com.zzy.pony.model.User;


public interface UserDao extends JpaRepository<User,Integer>{
	User findByLoginName(String loginName);
	@Query("select roles from User where userId=?1")
	List<Role> findRoles(Integer userId);
}
