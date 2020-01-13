package com.icss.mvp.util;

import com.icss.mvp.controller.ProjectLableController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpSession;

public class StringMD5Util {
	private static Logger logger = Logger.getLogger(StringMD5Util.class);

	/**
	 * 每一个用户存放一个session。便于各种操作！！！
	 */
	public static Map<String, HttpSession> mapSession = new HashMap<String, HttpSession>();

	// 32位MD5加密
	public static String MD5String(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			// System.out.println("MD5(" + sourceStr + ",32) = " + result);
			// System.out.println("MD5(" + sourceStr + ",16) = " +
			// buf.toString().substring(8, 24));
		} catch (NoSuchAlgorithmException e) {
			logger.error("MessageDigest.getInstance exception, error: "+e.getMessage());
		}
		return result;
	}

	// 加密及解密
	public static String convertMD5(String sourceStr) {
		char[] a = sourceStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	// 用户退出的代码（必须废除session或是remove相应的用户对象）：
	@SuppressWarnings("rawtypes")
	public static void userLogout(String username) {
		if (mapSession.get(username) != null) {
			// 得到需要退出的用户的session
			HttpSession session = mapSession.get(username);
			// 在map<username,session>中移除该用户，记住想要退出该用户，必须将该session废除或是remove掉user
			mapSession.remove(username);
			// 得到session的所属性合集
			Enumeration e = session.getAttributeNames();
			// 删除所有属性
			while (e.hasMoreElements()) {
				String sessionName = (String) e.nextElement();
				session.removeAttribute(sessionName);
			}
			// 废除该session
			session.invalidate();
		}
	}
}
