package com.zzy.pony.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.dao.UserDao;
import com.zzy.pony.mapper.UserMapper;
import com.zzy.pony.model.Role;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.User;
import com.zzy.pony.util.Sha1HashUtil;
import com.zzy.pony.vo.UserVo;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao dao;
	@Autowired
	private UserMapper mapper;

	@Override
	public User findByLoginName(String loginName) {
		return dao.findByLoginName(loginName);
	}

	@Override
	public UserVo findVo(String loginName) {
		return mapper.findByLoginName(loginName);
	}

	@Override
	public List<Role> findRoles(Integer userId) {
		return dao.findRoles(userId);
	}

	@Override
	public void addFromTeacher(Teacher teacher) {
		User user=new User();
		user.setCreateTime(new Date());
		user.setCreateUser("test");//TODO
		user.setLoginName(Constants.USER_TYPE_TEACHER+teacher.getTeacherNo());
		user.setPsw(Sha1HashUtil.hashPassword("123456",user.getLoginName()));
		user.setTeacher(teacher);
		user.setUpdateTime(new Date());
		user.setUpdateUser("test");//TODO
		user.setUserType(Constants.USER_TYPE_TEACHER);
		
		dao.save(user);
	}

	@Override
	public void addFromStudent(Student student) {
		User user=new User();
		user.setCreateTime(new Date());
		user.setCreateUser("test");//TODO
		user.setLoginName(Constants.USER_TYPE_STUDENT+student.getStudentNo());
		user.setPsw(Sha1HashUtil.hashPassword("123456",user.getLoginName()));
		user.setStudent(student);
		user.setUpdateTime(new Date());
		user.setUpdateUser("test");//TODO
		user.setUserType(Constants.USER_TYPE_STUDENT);
		
		dao.save(user);
	}

	@Override
	public User findById(Integer userId) {
		return dao.findOne(userId);
	}

}
