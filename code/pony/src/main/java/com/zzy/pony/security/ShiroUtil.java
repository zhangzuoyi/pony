package com.zzy.pony.security;

import org.apache.shiro.SecurityUtils;

import com.zzy.pony.security.ShiroDbRealm.ShiroUser;

public class ShiroUtil {
	/**
	 * 获得当前登录用户
	 * @return
	 */
	public static ShiroUser getLoginUser(){
		//当前登录的用户
		ShiroUser suser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return suser;
	}
}
