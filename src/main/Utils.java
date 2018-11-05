package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//�Զ����ɴ��빤����
public class Utils {
	public static void main(String[] args) {
		System.out.println(initcap_hump("JNH_ZJPT_BZZC_N"));
	}
	
	public static String getAnnotation(String msg,String sp){
		return sp+"/**\r\n"+sp+"* "+msg+" \r\n"+sp+"*/ \r\n";
	}
	
	public static String getAnnotation(String msg){
		return getAnnotation(msg,"\t");
	}
	
	public static List<Columns> getColumns(String tablename){
		List<Columns> result = new ArrayList<Columns>();
		Connection con = null;
		String sql = "select t.column_name,t.DATA_TYPE,c.COMMENTS "
			+ "FROM user_tab_columns t JOIN user_col_comments c "
			+ "on(t.TABLE_NAME=c.table_name AND t.COLUMN_NAME=c.column_name) "
			+ "WHERE t.table_name='"+tablename+"'";
		PreparedStatement pStemt = null;
		try {
			try {
				Class.forName(Config.DRIVER);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			con = DriverManager.getConnection(Config.URL, Config.NAME, Config.PASS);
			pStemt = con.prepareStatement(sql);

			ResultSet executeQuery = pStemt.executeQuery();
			while (executeQuery.next()) {
				Columns col = new Columns();
				col.setName(executeQuery.getString(1));
				col.setType(executeQuery.getString(2));
				col.setComment(executeQuery.getString(3));
				result.add(col);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static List<String> getTables(){
		List<String> tablenames = new ArrayList<String>();
		Connection con = null;
		String sql = "SELECT table_name FROM user_tables";
		PreparedStatement pStemt = null;
		try {
			try {
				Class.forName(Config.DRIVER);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			con = DriverManager.getConnection(Config.URL, Config.NAME, Config.PASS);
			pStemt = con.prepareStatement(sql);

			ResultSet executeQuery = pStemt.executeQuery();
			while (executeQuery.next()) {
				String tablename = executeQuery.getString(1);
				tablenames.add(tablename);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tablenames;
	}
	/**
	 * �Ƿ���Сд��ĸ
	 */
	private static boolean hasLowerstr(String str) {
		str = str.trim();
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			return true;
		}
		return false;
	}
	
	/**
	 * ��ABC�ĳ�Abc,AB_cd_eFg�ĳ�AbCdEfg,��AbCdEf/abCdEf�ĳ�AbCdEf
	 */
	public static String initcap(String str) {
		if(!Config.COLUMN_HUMP){ // �»���
			return initcap_hump(str);
		}else{ // �շ�ʽ
			if(hasLowerstr(str)){ 
				return initcap_capital(str);
			}else{
				return initcap_capital_only_first(str);
			}
		}
	}
	
	/** 
	 * �����»������,��JNH_ZJPT_BZZC_N�ĳ�JnhZjptBzzcN
	 */
	private static String initcap_hump(String name){
		String result="";
		String[] array = name.split("_");
		for(String s:array){
			if(s!=null && s!=""){
				result+=initcap_capital_only_first(s);
			}
		}
		return result;
	}
	
	/** ���ܣ��������ַ���������ĸ�ĳɴ�д
	 * @param str
	 * @return */
	public static String initcap_capital(String str) {
		str = str.trim();
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}
	
	/** ���ܣ��������ַ���������ĸ�ĳ�Сд
	 * @param str
	 * @return */
	public static String initcap_low(String str) {
		str = str.trim();
		char[] ch = str.toCharArray();
		if (ch[0] >= 'A' && ch[0] <= 'Z') {
			ch[0] = (char) (ch[0] + 32);
		}
		return new String(ch);
	}
	
	/**
	 * ��ABC/aBc�ĳ�Abc,����ĸ��д,������ȫ��Сд
	 */
	private static String initcap_capital_only_first(String str) {
		str = str.trim();
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		if(ch.length>1){
			for(int i=1;i<ch.length;i++){
				if (ch[i] >= 'A' && ch[i] <= 'Z') {
					ch[i] = (char) (ch[i] + 32);
				}
			}
		}
		return new String(ch);
	}

	/** ���ܣ�����е���������
	 * @param sqlType
	 * @return */
	public static String sqlType2JavaType(String sqlType) {
		if (sqlType.equalsIgnoreCase("bit")) {
			return "Boolean";
		} else if (sqlType.equalsIgnoreCase("int") || sqlType.equalsIgnoreCase("smallint") || sqlType.equalsIgnoreCase("tinyint")) {
			return "Integer";
		} else if (sqlType.equalsIgnoreCase("bigint") || sqlType.equalsIgnoreCase("number")) {
			return "Long";
		} else if (sqlType.equalsIgnoreCase("float")) {
			return "Float";
		} else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric") || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money") || sqlType.equalsIgnoreCase("smallmoney")) {
			return "Double";
		} else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("varchar2") || sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar") || sqlType.equalsIgnoreCase("text")) {
			return "String";
		} else if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("date")) {
			return "Date";
		} else if (sqlType.equalsIgnoreCase("image")) {
			return "Blod";
		} 
		return null;
	}

	public static List<String> getIds(String ids) {
		List<String> result = new ArrayList<String>();
		String[] array = ids.split(",");
		for(String a:array){
			if(a!=null && !a.trim().equals("")){
				result.add(a);
			}
		}
		return result;
	}
}

