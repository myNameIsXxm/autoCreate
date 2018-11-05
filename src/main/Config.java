package main;

public class Config {
	/*
	 * General 
	 */
	public static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	public static final String NAME = "reporter";
	public static final String PASS = "reporter";
	public static final String SCHEMA="reporter";
	public static final String WORKSPACE_PATH="D:/workspaceD/miss-parent";
	public static boolean COLUMN_HUMP = true; // 数据库字段命名方式,true为驼峰式风格,false为下划线风格
	/*
	 * Entity
	 */
	public static final String ENTITY_JAR_NAME = "miss-bean";
	public static final String ENTITY_PACKAGE = "com.es.entity";
	/*
	 * DAO
	 */
	public static final String DAO_JAR_NAME = "miss-dao";
	public static final String DAO_FACE_PACKAGE = "com.es.dao.face";
	public static final String DAO_IMPL_PACKAGE = "com.es.dao.impl";
	/*
	 * service
	 */
	public static final String SERVICE_JAR_NAME = "miss-service";
	public static final String SERVICE_FACE_PACKAGE = "com.mysi.service.face";
	public static final String SERVICE_IMPL_PACKAGE = "com.mysi.service.impl";
	// -------------- 无需修改的代码 ------------- //
	public static final String JAVA_PATH="src/main/java";
	public static String getJavaPath(String projectName){
		return WORKSPACE_PATH+"/"+projectName+"/"+JAVA_PATH+"/";
	}
	public static void main(String[] args) {
		String result = getJavaPath(ENTITY_JAR_NAME)+ENTITY_PACKAGE.replace(".","/");
		System.out.println(result);
	}
}
