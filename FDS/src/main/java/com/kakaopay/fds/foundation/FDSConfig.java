package com.kakaopay.fds.foundation;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class FDSConfig {
	
	//datasource config
	private static String DRIVER_CLASS_NAME;
	private static String URL;
	private static String USER_NAME;
	private static String PASSWORD ;
	
	public static void loadProperties(String fileName) throws Exception {
    	String fullFilePath = System.getProperty("user.dir")+ System.getProperty("file.separator") + fileName;
    	System.out.println("read properties file :: " + fullFilePath);
    	
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream(fullFilePath);
        properties.load(inputStream);
        inputStream.close();
        
        DRIVER_CLASS_NAME = properties.getProperty("datasource.driverClassName");
        URL = properties.getProperty("datasource.url");
        USER_NAME = properties.getProperty("datasource.username");
        PASSWORD = properties.getProperty("datasource.password");
    }

	public static String getDRIVER_CLASS_NAME() {
		return DRIVER_CLASS_NAME;
	}

	public static String getURL() {
		return URL;
	}

	public static String getUSER_NAME() {
		return USER_NAME;
	}

	public static String getPASSWORD() {
		return PASSWORD;
	}

}
