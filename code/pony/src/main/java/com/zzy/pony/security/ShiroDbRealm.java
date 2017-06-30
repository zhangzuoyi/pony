package com.zzy.pony.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Role;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.TeacherService;
import com.zzy.pony.service.TeacherSubjectService;
import com.zzy.pony.service.UserService;
import com.zzy.pony.vo.UserVo;


public class ShiroDbRealm extends AuthorizingRealm {
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private UserService userService;
	@Autowired
	private TeacherSubjectService tsService;
	@Autowired
	private SchoolClassService classService;
	
	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		return authen(authcToken);
	}
	
	@Transactional(readOnly=true)
	private AuthenticationInfo authen(AuthenticationToken authcToken){
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		//设置校验密码的方式，是否要加密
//		initCredentialsMatcher(isAutoLogin);
//		StaffDict sd=staffDao.findByUserName(token.getUsername());
//		StaffDict sd=staffDao.findOne(token.getUsername());//改用empNo登录
//		Teacher teacher=teacherService.findByTeacherNo(token.getUsername());
		UserVo vo=userService.findVo(token.getUsername());
		if(vo != null){
			ShiroUser shUser=new ShiroUser(vo.getUserId(), vo.getLoginName(), vo.getShowName(), vo.getUserType());
			if(Constants.USER_TYPE_TEACHER.equals(vo.getUserType())){
				Teacher teacher=new Teacher();
				teacher.setTeacherId(vo.getTeacherId());
				List<TeacherSubject> list=tsService.findCurrentByTeacher(teacher);
				if(list.size()>0){
					List<Integer> tsIds=new ArrayList<Integer>();
					for(TeacherSubject ts: list){
						tsIds.add(ts.getTsId());
					}
					shUser.tsIds=tsIds;
				}
				List<SchoolClass> sclasses=classService.findCurrentByTeacher(teacher);
				if(sclasses.size()>0){
					List<Integer> classIds=new ArrayList<Integer>();
					for(SchoolClass sc: sclasses){
						classIds.add(sc.getClassId());
					}
					shUser.classIds=classIds;
				}
			}
			return new SimpleAuthenticationInfo(shUser, vo.getPsw(), ByteSource.Util.bytes(vo.getLoginName()), getName());
		}else{
			return null;
		}
	}
	
	/** 
	* 设定Password校验的Hash算法与迭代次数. 
	* 
	*/ 
	@PostConstruct 
	public void initCredentialsMatcher() {
//		if (isAutoLogin == true) {// 普通校验
//			setCredentialsMatcher(new SimpleCredentialsMatcher());
//		} else {// sha1对密码加密后校验
//			// HashedCredentialsMatcher matcher = new
//			// HashedCredentialsMatcher(Md5Hash.ALGORITHM_NAME);
//			HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Sha1Hash.ALGORITHM_NAME);
//
//			setCredentialsMatcher(matcher);
//		}
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Sha1Hash.ALGORITHM_NAME);

		setCredentialsMatcher(matcher);
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
//		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		info.addRole("common");
		return authorize(shiroUser);
	}
	private AuthorizationInfo authorize(ShiroUser su){
//		User user=userService.findByEmpNo(loginName);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		if(user !=null){
//			info.addRole(user.getRole().getRoleName());
//			
//			for(Resource res:user.getRole().getResources()){
//				info.addStringPermission(res.getResKey());
//			}
//		}
		List<Role> roles=userService.findRoles(su.getId());
		for(Role role: roles){
			info.addRole(role.getRoleCode());
		}
		info.addStringPermissions(userService.findResourceNames(su.getId()));
		if(Constants.USER_TYPE_STUDENT.equals(su.getUserType())){
			info.addRole("student");//学生
		}else if(Constants.USER_TYPE_TEACHER.equals(su.getUserType())){
			info.addRole("staff");//教职工
			if(su.getTsIds() !=null && su.getTsIds().size()>0){
				info.addRole("teacher");//任课教师
			}
			if(su.getClassIds() !=null && su.getClassIds().size()>0){
				info.addRole("headteacher");//班主任
			}
		}

		return info;
	}
	

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -1373760761780840081L;
		private Integer id;
		private String loginName;
		private String showName;
		private String userType;
		private List<Integer> tsIds;//老师任课表的ID
		private List<Integer> classIds;//担任班主任的班级列表
		
		public ShiroUser(Integer id, String loginName, String showName, String userType) {
			this.id=id;
			this.loginName=loginName;
			this.showName = showName;
			this.userType=userType;
		}

		public Integer getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getShowName() {
			return showName;
		}

		public String getUserType() {
			return userType;
		}
		
		public List<Integer> getTsIds() {
			return tsIds;
		}

		public List<Integer> getClassIds() {
			return classIds;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return showName;
		}

		/**
		 * 重载equals,只计算loginName;
		 */
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

		/**
		 * 重载equals,只比较loginName
		 */
		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
	}
}