package in.naishe.cavespring.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CaveSpringConfig {
	
	private static Properties props = null;
	public static boolean verbose = false;
	
	public static Properties getProperties(){
		if(props==null){
			props = new Properties();
			CaveSpringConfig.class.getClassLoader();
			try {
				InputStream is = 
					ClassLoader.getSystemResourceAsStream("config.properties");
				if(is!=null)
					props.load(is);
			} catch (IOException e) {
				System.err.println("Error while reading config file.");
				e.printStackTrace();
			}
		}
		return props;
	}
	
	public static String getAccessKey(){
		return getProperties().getProperty("EC2_ACCESS_KEY").trim();
	}
	
	public static String getSecretKey(){
		return getProperties().getProperty("EC2_SECRET_KEY").trim();
	}
	
	public static void setAccessKey(String accessKey){
		getProperties().setProperty("EC2_ACCESS_KEY", accessKey);
	}
	
	public static void setSecretKey(String secretKey){
		getProperties().setProperty("EC2_SECRET_KEY", secretKey);
	}
	
	public static void loadCredentials(String accessKey, String secretKey) {
		setAccessKey(accessKey);
		setSecretKey(secretKey);
	}
	
	public static void enableVerbose(){verbose = true;}
	public static void disableVerbose(){verbose = false;}
}
