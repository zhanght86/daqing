package com.midas.uitls.tools;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


public class CommonsUtils {
	
	private static final String PASSWORD_SALT = "GaGvAfEGF8yq0z5AP4sfYr4rA0CkKrnt";
	
	public static String encryptPassword(String userName, String password) {
		return new Md5Hash(password, getPasswordSalt(userName)).toString();
	}
	
	public static String getPasswordSalt(String userName) {
		return new Md5Hash(userName, PASSWORD_SALT).toString();
	}
	
	public static String covertpw(String plainText ) { 
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(plainText.getBytes()); 
			byte b[] = md.digest(); 
			int i; 
			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0; offset < b.length; offset++) { 
				i = b[offset]; 
				if(i<0) i+= 256; 
				if(i<16) 
				buf.append("0"); 
				buf.append(Integer.toHexString(i)); 
			} 
			return buf.toString().substring(8,24);
		} catch (NoSuchAlgorithmException e) { 
			e.printStackTrace(); 
		}
		return plainText; 
	} 
	
	public static String fillInSpaces(String str, int fillNumber){
		int setNumber = fillNumber - str.length();
		for(int i=0;i<setNumber;i++){
			str += " ";
		}
		return str;
	}
	
	public static String fillInSpaces(String str, int fillNumber, String append){
        int setNumber = fillNumber - str.length();
        for(int i=0;i<setNumber;i++){
            str += append;
        }
        return str;
    }

	public static String getPropertiesValue(String keystr) throws IOException{
		Resource resource = new ClassPathResource("/conf/comm.properties");
		Properties props = PropertiesLoaderUtils.loadProperties(resource);
		return (props.get(keystr)!= null && !"".equals(props.get(keystr).toString()))?props.get(keystr).toString():"";
	}
	
	public static String getPropertiesValue(String path,String keystr) throws IOException{
		Resource resource = new ClassPathResource(path);
		Properties props = PropertiesLoaderUtils.loadProperties(resource);
		return (props.get(keystr)!= null && !"".equals(props.get(keystr).toString()))?props.get(keystr).toString():"";
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String,String> getPropertiesMap(String path) throws IOException{
		Resource resource = new ClassPathResource(path);
		Properties props = PropertiesLoaderUtils.loadProperties(resource);
		Map<String, String> map = new HashMap<String, String>((Map) props);  
		return map;
	}
	
}
