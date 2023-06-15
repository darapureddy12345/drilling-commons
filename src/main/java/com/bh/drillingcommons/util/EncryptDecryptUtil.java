package com.bh.drillingcommons.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bh.drillingcommons.enumerators.Constants;

public class EncryptDecryptUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptDecryptUtil.class);

	private static SecretKeySpec secretKey;
	private static byte[] key;
	private static String ucd_key = Constants.TOKEN.getValue();

	public static void setKey(String myKey) {
		try {
			key = myKey.getBytes("UTF-8");
			secretKey = new SecretKeySpec(key, "AES");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String strToEncrypt) {

		String password = "";
		try {
			setKey(ucd_key);

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			password = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			LOGGER.error("Encryption Exception: " + e);
		}
		return password;
	}

	public static String decrypt(String strToDecrypt) {
		String password = "";
		try {
			setKey(ucd_key);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			password = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			LOGGER.error("Decryption Exception: " + e);
		}
		return password;
	}

}
