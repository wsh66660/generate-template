package constant;

import util.PropertiesLoad;

public class Constant {
	
	  public static final String JDBC_URL = PropertiesLoad.get("jdbc.url");
	  public static final String JDBC_USERNAME = PropertiesLoad.get("jdbc.username");
	  public static final String JDBC_PASSWORD = PropertiesLoad.get("jdbc.password");
	  
	  public static final String AUTHOR = PropertiesLoad.get("author");
	  public static final String TABLE_NAME = PropertiesLoad.get("tableName");
	  public static final String FILE_PATH = PropertiesLoad.get("filePath");
	  public static final String JSP_PATH = PropertiesLoad.get("jspPath");
	  public static final String CLASS_PAHT = PropertiesLoad.get("classPaht");
	  public static final String DOMAIN_NAME = PropertiesLoad.get("domainName");
	  public static final String DOMAIN_CNAME = PropertiesLoad.get("domainCname");

}
