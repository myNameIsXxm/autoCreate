package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GenServiceImpl {
	private String dbtablename;
	private String tablename;

	public GenServiceImpl(String name){
		dbtablename = name;
		tablename = Utils.initcap(name);
	}
	
	public void create() {
		String content = parse();
		try {
			String filepath = Config.getJavaPath(Config.SERVICE_JAR_NAME)+Config.SERVICE_IMPL_PACKAGE.replace(".","/") + "/";
			String filename = tablename + "ServiceImpl.java";
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
		sb.append("package " + Config.SERVICE_IMPL_PACKAGE + ";\r\n");
		sb.append("import java.util.List;\r\nimport org.hibernate.Session;\r\n");
		sb.append("import org.hibernate.Transaction;\r\nimport org.slf4j.Logger;\r\n");
		sb.append("import org.slf4j.LoggerFactory;\r\n");
		sb.append("import javax.inject.Inject;\r\n");
		sb.append("import "+Config.DAO_FACE_PACKAGE+"."+tablename+"DaoFace;\r\n");
		sb.append("import "+Config.SERVICE_FACE_PACKAGE+"."+tablename+"ServiceFace;\r\n");
		sb.append("import "+Config.ENTITY_PACKAGE+"."+tablename+";\r\n");
		sb.append("import org.springframework.stereotype.Component;\n\rimport org.springframework.transaction.annotation.Transactional;\n\r");
		sb.append(Utils.getAnnotation(tablename + " Service Impl",""));
		sb.append("@Component(\""+Utils.initcap_low(tablename)+"Service\")\n@Transactional\n");
		sb.append("public class " + tablename + "ServiceImpl implements "+tablename+"ServiceFace{\r\n");
		sb.append("\n\tprivate static final Logger log = LoggerFactory.getLogger("+tablename+"ServiceImpl.class);\r\n");
		sb.append("\n\t@Inject\r\n\tprivate "+tablename+"DaoFace "+Utils.initcap_low(tablename)+"Dao;\r\n\n");
		sb.append("\t@Override\n\tpublic int save("+tablename+" bean){\n\t"+parseSave()+"\n\t}\r\n\r\n");
		sb.append("	@Override\n\tpublic int delete(Long id){\n\t"+parseDelete()+"\n\t}\r\n\r\n");
		sb.append("	@Override\n\tpublic int update("+tablename+" bean){\n\t"+parseUpdate()+"\n\t}\r\n\r\n");
		sb.append("	@Override\n\tpublic "+tablename+" selectById(Long id){\n\t"+parseSelectById()+"\n\t}\r\n\r\n");
		sb.append("}\r\n");
		return sb.toString();
	}

	private String parseUpdate() {
		return around("return "+Utils.initcap_low(tablename)+"Dao.update(bean);",1,0);
	}

	private String parseDelete() {
		return around("return "+Utils.initcap_low(tablename)+"Dao.delete(id);",1,0);
	}

	private String parseSave() {
		return around("return "+Utils.initcap_low(tablename)+"Dao.save(bean);",1,0);
	}

	private String parseSelectById() {
		return around("return "+Utils.initcap_low(tablename)+"Dao.selectById(id);",1,0);
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
