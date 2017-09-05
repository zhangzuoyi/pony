package com.zzy.pony.util;

import org.apache.shiro.crypto.hash.Sha1Hash;

public class Sha1HashUtil {
	public static String hashPassword(String psw, String loginName) {
		Sha1Hash hash = new Sha1Hash(psw, loginName);
		return hash.toString();
	}
}
