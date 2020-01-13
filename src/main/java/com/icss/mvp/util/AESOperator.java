package com.icss.mvp.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 */
@SuppressWarnings("restriction")
public class AESOperator {

	/*
	 * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
	 */
	private String sKey = "1234123412ABCDEF";// key，可自行修改
	private String ivParameter = "ABCDEF1234123412";// 偏移量,可自行修改
	private static AESOperator instance = null;

	private AESOperator() {

	}

	public static AESOperator getInstance() {
		if (instance == null)
			instance = new AESOperator();
		return instance;
	}

	public static String Encrypt(String encData, String secretKey, String vector) throws Exception {

		if (secretKey == null) {
			return null;
		}
		if (secretKey.length() != 16) {
			return null;
		}
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] raw = secretKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
		return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码。
	}

	// 加密
	public String encrypt(String sSrc) throws Exception {
		return encrypt(sSrc,sKey);
	}
	// 加密
	public String encrypt(String sSrc,String sKey) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
//		return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码。
		return parseByte2HexStr(encrypted);// 此处使用 二进制转换成16进制
	}

	// 解密
	public String decrypt(String sSrc) throws Exception {
		return decrypt(sSrc,sKey);
	}

	// 解密
	public String decrypt(String sSrc,String sKey) throws Exception {
		try {
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			// byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
			byte[] original = cipher.doFinal(parseHexStr2Byte(sSrc));
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public String decrypt(String sSrc, String key, String ivs) throws Exception {
		try {
			byte[] raw = key.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}

	public static String encodeBytes(byte[] bytes) {
		StringBuffer strBuf = new StringBuffer();

		for (int i = 0; i < bytes.length; i++) {
			strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
			strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
		}

		return strBuf.toString();
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		// 需要加密的字串
		 String cSrc =
		 "[{\"request_no\":\"1001\",\"service_code\":\"FS0001\",\"contract_id\":\"100002\",\"order_id\":\"0\",\"phone_id\":\"13913996922\",\"plat_offer_id\":\"100094\",\"channel_id\":\"1\",\"activity_id\":\"100045\"}]";

		 // 加密
		 long lStart = System.currentTimeMillis();
		 String enString = AESOperator.getInstance().encrypt(cSrc);
		 System.out.println("加密后的字串是：" + enString);

		 long lUseTime = System.currentTimeMillis() - lStart;
		 System.out.println("加密耗时：" + lUseTime + "毫秒");
		 // 解密
		 lStart = System.currentTimeMillis();
		 String DeString = AESOperator.getInstance().decrypt(enString);
		 System.out.println("解密后的字串是：" + DeString);
		 lUseTime = System.currentTimeMillis() - lStart;
		 System.out.println("解密耗时：" + lUseTime + "毫秒");

	}

}