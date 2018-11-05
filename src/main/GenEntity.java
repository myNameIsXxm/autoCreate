package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GenEntity {
	
	private String tablename;
	private String dbtablename;
	private List<Columns> columns;
	private List<String> ids;
	
	public GenEntity(String tablenames,String id) {
		dbtablename = tablenames;
		tablename = Utils.initcap(tablenames);
		columns = Utils.getColumns(tablenames);
		ids = Utils.getIds(id);
	}
	
	public void create(){
		String content = parse();
		try {
			String filepath = Config.getJavaPath(Config.ENTITY_JAR_NAME)+Config.ENTITY_PACKAGE.replace(".","/") + "/";
			String filename = tablename + ".java";
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

	/** 功能：生成实体类主体代码
	 * @param colnames
	 * @param colTypes
	 * @param colSizes
	 * @return */
	private String parse() {
		StringBuffer sb = new StringBuffer();
		sb.append("package " + Config.ENTITY_PACKAGE + ";\r\n");
		sb.append("import java.util.Date;\r\n");
		sb.append("import javax.persistence.Column;\r\n");
		sb.append("import javax.persistence.Entity;\r\n");
		sb.append("import javax.persistence.Table;\r\n");
		sb.append("import javax.persistence.Id;\r\n");
		sb.append(Utils.getAnnotation(tablename + " entity.",""));
		sb.append("@Entity\r\n@Table(name = \""+dbtablename+"\", schema = \""+Config.SCHEMA+"\")\r\n@SuppressWarnings(\"serial\")\r\n");
		sb.append("public class " + tablename + " implements java.io.Serializable {\r\n");
		// 属性
		sb.append(Utils.getAnnotation("Fields"));
		for (Columns col:columns) {
			sb.append("\tprivate " + Utils.sqlType2JavaType(col.getType()) + " " + Utils.initcap_low(Utils.initcap(col.getName()))+";");
			if(col.getComment()!=null && col.getComment()!="" && !col.getComment().equals("null")){
				sb.append(" // "+col.getComment()+"\r\n");
			}else{
				sb.append("\r\n");
			}
		}
		// 构造器
		sb.append(Utils.getAnnotation("Constructor"));
		sb.append("	public "+tablename+"(){ \r\n");
		sb.append("		super();\r\n");
		sb.append("	}\r\n");
		// get set方法
		sb.append(Utils.getAnnotation("Get & Set"));
		for (Columns c:columns) {
			sb.append("\tpublic void set" + Utils.initcap(c.getName()) + "(" + Utils.sqlType2JavaType(c.getType()) + " " + Utils.initcap_low(Utils.initcap(c.getName())) + "){\r\n");
			sb.append("\t\tthis." + Utils.initcap_low(Utils.initcap(c.getName())) + "=" + Utils.initcap_low(Utils.initcap(c.getName())) + ";\r\n");
			sb.append("\t}\r\n");
			if(ids.contains(c.getName())){
				sb.append("\t@Id\r\n");
			}
			sb.append("\t@Column(name=\""+c.getName()+"\")\r\n");
			sb.append("\tpublic " + Utils.sqlType2JavaType(c.getType()) + " get" + Utils.initcap(c.getName()) + "(){\r\n");
			sb.append("\t\treturn " + Utils.initcap_low(Utils.initcap(c.getName())) + ";\r\n");
			sb.append("\t}\r\n");
		}
		sb.append("}\r\n");
		return sb.toString();
	}

}
