package com.zzy.pony.security;

import java.io.Serializable;

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
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.model.Teacher;
import com.zzy.pony.service.TeacherService;


public class ShiroDbRealm extends AuthorizingRealm {
	@Autowired
	private TeacherService teacherService;
	
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
		Teacher teacher=teacherService.findByTeacherNo(token.getUsername());
		if(teacher != null){
			ShiroUser shUser=new ShiroUser(teacher.getTeacherId(), teacher.getTeacherNo(), teacher.getName());
			return new SimpleAuthenticationInfo(shUser, token.getPassword(), getName());
		}else{
			return null;
		}
	}
	
	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 * 
	 */
	//@PostConstruct
	public void initCredentialsMatcher(Boolean isAutoLogin){
		if(isAutoLogin == true){//普通校验
			setCredentialsMatcher(new SimpleCredentialsMatcher());
		}else{//MD5对密码加密后校验
			HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Md5Hash.ALGORITHM_NAME);
			//	matcher.setHashIterations(1);
			setCredentialsMatcher(matcher);
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
//		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		info.addRole("common");
		return authorize(shiroUser.getTeacherNo());
	}
	private AuthorizationInfo authorize(String loginName){
//		User user=userService.findByEmpNo(loginName);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		if(user !=null){
//			info.addRole(user.getRole().getRoleName());
//			
//			for(Resource res:user.getRole().getResources()){
//				info.addStringPermission(res.getResKey());
//			}
//		}
		info.addRole("teacher");

		return info;
	}
	

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -1373760761780840081L;
		private Integer id;
		private String teacherNo;
		private String name;
		
		public ShiroUser(Integer id, String teacherNo, String name) {
			this.id=id;
			this.teacherNo=teacherNo;
			this.name = name;
		}

		public String getTeacherNo() {
			return teacherNo;
		}

		public String getName() {
			return name;
		}

		public Integer getId() {
			return id;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return name;
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