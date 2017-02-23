package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Role;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.User;
import com.zzy.pony.vo.UserVo;

public interface UserService {
	User findByLoginName(String loginName);
	UserVo findVo(String loginName);
	List<Role> findRoles(Integer userId);
	void addFromTeacher(Teacher teacher);
	void addFromStudent(Student student);
}
