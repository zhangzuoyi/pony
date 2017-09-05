package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zzy.pony.model.Role;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.User;


public interface UserDao extends JpaRepository<User,Integer>{
	User findByLoginName(String loginName);
	@Query("select t.roles from User t where t.userId=?1")
	List<Role> findRoles(Integer userId);
	List<User> findByTeacher(Teacher teacher);
	List<User> findByStudent(Student student);
	Page<User> findAll(Pageable pageable);
	
}
