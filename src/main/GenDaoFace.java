package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GenDaoFace {
	private String tablename;

	public GenDaoFace(String tablenames){
		tablename = Utils.initcap(tablenames);
	}
	
	public void create() {
		String content = parse();
		try {
			String filepath = Config.getJavaPath(Config.DAO_JAR_NAME)+Config.DAO_FACE_PACKAGE.replace(".","/") + "/";
			String filename = tablename+ "DaoFace.java";
			System.out.println("路径：" + filepath+filename);
			File file = new File(filepath);
			if(!file.exists()){
				file.mkdirs();
			}
			FileWriter fw = new FileWriter(filepath+filename);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(content);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String parse() {
		String tname = tablename;
		StringBuffer sb = new StringBuffer();
		sb.append("package " + Config.DAO_FACE_PACKAGE + ";\r\n");
		sb.append("import java.util.List;\r\n");
		sb.append("import "+Config.ENTITY_PACKAGE+"."+tname+";\r\n");
		sb.append(Utils.getAnnotation(tablename + " Dao",""));
		sb.append("public interface " + tname + "DaoFace{\r\n");
		sb.append("	public int save("+tname+" bean);\r\n\r\n");
		sb.append("	public int delete(Long id);\r\n\r\n");
		sb.append("	public int update("+tname+" bean);\r\n\r\n");
		sb.append("	public "+tname+" selectById(Long id);\r\n\r\n");
		sb.append("}\r\n");
		return sb.toString();
	}


}
