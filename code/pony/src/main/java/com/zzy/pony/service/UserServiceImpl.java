package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.config.Constants;
import com.zzy.pony.dao.UserDao;
import com.zzy.pony.mapper.RoleMapper;
import com.zzy.pony.mapper.UserMapper;
import com.zzy.pony.model.Resource;
import com.zzy.pony.model.Role;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.User;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.util.Sha1HashUtil;
import com.zzy.pony.vo.UserVo;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao dao;
	@Autowired
	private UserMapper mapper;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public User findByLoginName(String loginName) {
		return dao.findByLoginName(loginName);
	}

	@Override
	public UserVo findVo(String loginName) {
		return mapper.findByLoginName(loginName.toLowerCase());//小写比较
	}

	@Override
	public List<Role> findRoles(Integer userId) {
		return dao.findRoles(userId);
	}

	@Override
	public void addFromTeacher(Teacher teacher) {
		User user=new User();
		user.setCreateTime(new Date());
		user.setCreateUser(teacher.getCreateUser());
		user.setLoginName(Constants.USER_TYPE_TEACHER+teacher.getTeacherNo());
		user.setPsw(Sha1HashUtil.hashPassword("123456",user.getLoginName()));
		user.setTeacher(teacher);
		user.setUpdateTime(new Date());
		user.setUpdateUser(teacher.getCreateUser());
		user.setUserType(Constants.USER_TYPE_TEACHER);
		
		dao.save(user);
	}

	@Override
	public void addFromStudent(Student student) {
		User user=new User();
		user.setCreateTime(new Date());
		user.setCreateUser(student.getCreateUser());
		user.setLoginName(Constants.USER_TYPE_STUDENT+student.getStudentNo());
		user.setPsw(Sha1HashUtil.hashPassword("123456",user.getLoginName()));
		user.setStudent(student);
		user.setUpdateTime(new Date());
		user.setUpdateUser(student.getCreateUser());
		user.setUserType(Constants.USER_TYPE_STUDENT);
		
		dao.save(user);
	}

	@Override
	public User findById(Integer userId) {
		return dao.findOne(userId);
	}

	@Override
	public User findByTeacherId(int teacherId) {
		// TODO Auto-generated method stub
		Teacher teacher = teacherService.get(teacherId);
		if (teacher != null) {
			List<User> users = dao.findByTeacher(teacher);
			if (users !=null && users.size()>0) {
				return  users.get(0);
			}
		}				
		return null;
	}

	@Override
	public User findByStudentId(int studentId) {
		// TODO Auto-generated method stub
		Student student = studentService.get(studentId);		
		if (student != null) {
			List<User> users = dao.findByStudent(student);
			if (users !=null && users.size()>0) {
				return  users.get(0);
			}
		}	
		return null;
	}

	@Override
	public String findUserNameById(Integer id) {
		// TODO Auto-generated method stub
		User user = dao.findOne(id);
		
		if (Constants.USER_TYPE_STUDENT.equalsIgnoreCase(user.getUserType())) {
			return  user.getStudent().getName();
		}
		if (Constants.USER_TYPE_TEACHER.equalsIgnoreCase(user.getUserType())) {
			return user.getTeacher().getName();
		}	
		return null;
	}

	/*** 
	* <p>Description: MAP<ID,NAME></p>
	* @author  WANGCHAO262
	* @date  2017年5月12日 上午10:29:37
	*/
	@Override
	public Map<Integer, String> getUserNameMap() {
		// TODO Auto-generated method stub
		Map<Integer, String> result = new HashMap<Integer, String>();
		List<User> users = dao.findAll();
		for (User user : users) {
			String name =   this.findUserNameById(user.getUserId());
			result.put(user.getUserId(), name);
		}
		
		return result;
	}

	@Override
	public List<UserVo> list() {
		// TODO Auto-generated method stub
		List<UserVo> result = new ArrayList<UserVo>();
		List<User> users = dao.findAll();
		for (User user : users) {
			UserVo vo = new UserVo();
			vo.setLastLoginTime(user.getLastLoginTime());
			vo.setLoginName(user.getLoginName());
			vo.setPsw(user.getPsw());
			vo.setUserId(user.getUserId());
			vo.setUserType(vo.getUserType());
			List<String> roles = new ArrayList<String>();
			for(Role role : user.getRoles()){
				roles.add(role.getRoleCode());
			}
			vo.setRoles(roles.toArray(new String[0]));	
			result.add(vo);
		}
		
		
		
		return result;
	}

	@Override
	public void add(User user) {
		// TODO Auto-generated method stub
		user.setCreateTime(new Date());
		user.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
		user.setUpdateTime(new Date());
		user.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		dao.save(user);
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		User old = dao.findOne(user.getUserId());
		old.setPsw(user.getPsw());
		old.setRoles(user.getRoles());
		old.setUpdateTime(new Date());
		old.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		dao.save(old);
	}

	@Override
	public void delete(int userId) {
		// TODO Auto-generated method stub
		dao.delete(userId);
	}

	@Override
	public Boolean isExist(int userId) {
		// TODO Auto-generated method stub
		User user = dao.findOne(userId);
		if (user == null) {
			return false;
		}	
		return true;
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return dao.findAll(pageable);
	}

	@Override
	public Set<String> findResourceNames(Integer userId) {
		List<Role> roles=dao.findRoles(userId);
		Set<String> result=new HashSet<String>();
		for(Role role:roles){
			for(Resource res: role.getResources()){
				result.add(res.getResKey());
			}
		}
		return result;
	}

	@Override
	public Page<UserVo> findPage(int currentPage, int pageSize, String userType, String userName) {
		List<UserVo> list=mapper.findPage(currentPage*pageSize, pageSize, userType, userName);
		for(UserVo vo: list) {
			List<String> roles=roleMapper.findRoleCodeByUser(vo.getUserId());
			vo.setRoles(roles.toArray(new String[0]));
		}
		int count=mapper.findCount(userType, userName);
		Pageable pageable = new PageRequest(currentPage, pageSize);
		Page<UserVo> result = new PageImpl<UserVo>(list, pageable, count);
		return result;
	}
	
	
	
	
	
	
	
	

}
