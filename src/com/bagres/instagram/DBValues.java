package com.bagres.instagram;

public final class DBValues {
	
	public static final Integer port = 5432;
	public static final String host = "localhost";
	public static final String dbName = "indb";
	public static final String user = "postgres";
	public static final String password = "masterkey";
	
	private DBValues(){
		System.out.println("molleja e bagre");
	}
	
	public static String buildConnectionString(){
		return "jdbc:postgresql://" + host + ":" + port + "/" + dbName ;
	}
}
