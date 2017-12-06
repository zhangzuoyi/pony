package com.zzy.pony.util;


import org.apache.shiro.crypto.hash.Sha1Hash;

public class Sha1HashUtil {
	public static String hashPassword(String psw, String loginName) {
		Sha1Hash hash = new Sha1Hash(psw, loginName);
		return hash.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(hashPassword("123456","t002"));
	}

}
