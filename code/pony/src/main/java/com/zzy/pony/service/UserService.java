package com.zzy.pony.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zzy.pony.model.Role;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.User;
import com.zzy.pony.vo.UserVo;

public interface UserService {
	User findByLoginName(String loginName);
	User findById(Integer userId);
	UserVo findVo(String loginName);
	List<Role> findRoles(Integer userId);
	void addFromTeacher(Teacher teacher);
	void addFromStudent(Student student);
	User findByTeacherId(int teacherId);
	User findByStudentId(int studentId);
	String findUserNameById(Integer id);
	Map<Integer, String> getUserNameMap();
	List<UserVo> list();
	void add(User user);
	void update(User user);
	void delete(int	userId);
	Boolean isExist(int userId);
	Page<User> findAll(Pageable pageable);
	/**
	 * 获取用户授权的资源Key
	 * @param userId
	 * @return
	 */
	Set<String> findResourceNames(Integer userId);

}
