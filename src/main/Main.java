package main;

public class Main {
	public static void main(String[] args) {
		String table="LEGEND";
		new GenEntity(table,"ID").create();
		new GenDaoFace(table).create();
		new GenDaoImpl(table).create();
		new GenServiceFace(table).create();
		new GenServiceImpl(table).create();
	}
}
