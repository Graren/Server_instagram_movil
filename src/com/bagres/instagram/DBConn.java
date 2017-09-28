package com.bagres.instagram;

import ogcg.org.jdbc.JDBCOConnection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by oscar on 9/3/2017.
 */
public class DBConn {
    public JDBCOConnection con;

    public JDBCOConnection getCon() {
        return con;
    }
    
    public DBConn(){
    	init();
    }

    public void setCon(JDBCOConnection con) {
        this.con = con;
    }

    public void init(){
        con = new JDBCOConnection("postgresql", 5432, "instadb", "postgres", "masterkey", "localhost");
        con.startConnection();
        System.out.println("INSTANCE INITIALIZED");
    }
    
    public Connection getSQLConn(){
        return con.getConn();
    }

    public void close() {
        try {
            getSQLConn().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
