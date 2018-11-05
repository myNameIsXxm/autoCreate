package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GenDaoImpl {
	private String dbtablename;
	private String tablename;

	public GenDaoImpl(String name){
		dbtablename = name;
		tablename = Utils.initcap(name);
	}
	
	public void create() {
		String content = parse();
		try {
			String filepath = Config.getJavaPath(Config.DAO_JAR_NAME)+Config.DAO_IMPL_PACKAGE.replace(".","/") + "/";
			String filename = tablename + "DaoImpl.java";
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
		StringBuffer sb = new StringBuffer();
		sb.append("package " + Config.DAO_IMPL_PACKAGE + ";\r\n");
		sb.append("import java.util.List;\r\nimport org.hibernate.Session;\r\n");
		sb.append("import org.hibernate.Transaction;\r\nimport org.slf4j.Logger;\r\n");
		sb.append("import org.slf4j.LoggerFactory;\r\n");
		sb.append("import "+Config.DAO_FACE_PACKAGE+"."+tablename+"DaoFace;\r\n");
		sb.append("import "+Config.ENTITY_PACKAGE+"."+tablename+";\r\n");
		sb.append("import org.springframework.stereotype.Component;\n\rimport org.springframework.transaction.annotation.Transactional;\n\r");
		sb.append(Utils.getAnnotation(tablename + " Dao Impl",""));
		sb.append("@Component(\""+Utils.initcap_low(tablename)+"Dao\")\n");
		sb.append("public class " + tablename + "DaoImpl extends BaseDao implements "+tablename+"DaoFace{\r\n");
		sb.append("\tprivate static final Logger log = LoggerFactory.getLogger("+tablename+"DaoImpl.class);\r\n");
		sb.append("\t@Override\n\tpublic int save("+tablename+" bean){\n\t"+parseSave()+"\n\t}\r\n\r\n");
		sb.append("	@Override\n\tpublic int delete(Long id){\n\t"+parseDelete()+"\n\t}\r\n\r\n");
		sb.append("	@Override\n\tpublic int update("+tablename+" bean){\n\t"+parseUpdate()+"\n\t}\r\n\r\n");
		sb.append("	@Override\n\tpublic "+tablename+" selectById(Long id){\n\t"+parseSelectById()+"\n\t}\r\n\r\n");
		sb.append("}\r\n");
		return sb.toString();
	}

	private String parseUpdate() {
		StringBuffer sb=new StringBuffer("");
		sb.append(around("try{",1));
		sb.append(around("getSession().update(bean);",3));
		sb.append(around("}catch(Exception e){"));
		sb.append(around("return 0;",3));
		sb.append(around("}"));
		sb.append(around("return 1;",2,0));
		return sb.toString();
	}

	private String parseDelete() {
		StringBuffer sb=new StringBuffer("");
		sb.append(around("try{",1));
		sb.append(around(tablename+" bean = selectById(id);",3));
		sb.append(around("if(bean!=null){",3));
		sb.append(around("getSession().delete(bean);",4));
		sb.append(around("}",3));
		sb.append(around("}catch(Exception e){"));
		sb.append(around("return 0;",3));
		sb.append(around("}"));
		sb.append(around("return 1;",2,0));
		return sb.toString();
	}

	private String parseSave() {
		StringBuffer sb=new StringBuffer("");
		sb.append(around("try{",1));
		sb.append(around("Long id = getId(\""+dbtablename+"\");",3));
		sb.append(around("bean.setId(id);",3));
		sb.append(around("getSession().save(bean);",3));
		sb.append(around("}catch(Exception e){"));
		sb.append(around("return 0;",3));
		sb.append(around("}"));
		sb.append(around("return 1;",2,0));
		return sb.toString();
	}

	private String parseSelectById() {
		StringBuffer sb=new StringBuffer("");
		sb.append(around("Session session = getSession();")); 
		sb.append(around(tablename+" instance = ("+tablename+") session.get(\""+Config.ENTITY_PACKAGE+"."+tablename+"\", id);"));
		sb.append(around("session.clear();"));
		sb.append(around("return instance;"));
		String result = sb.toString();
		return result.substring(1, result.length()-1);
	}

	private String around(String str) {
		return around(str,2);
	}
	
	private String around(String str,int i) {
		return around(str,i,1);
	}
	
	private String around(String str,int i,int j) {
		String re = "";
		for(int n=0;n<i;n++){
			re+="\t";
		}
		String su = "";
		for(int n=0;n<j;n++){
			su+="\n";
		}
		return re+str+su;
	}
	
}
