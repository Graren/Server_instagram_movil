package com.bagres.instagram;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class Helper {
	
	public static String[][] getResult(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        rs.beforeFirst();
        List<String[]> rsArr = new Vector<>();
        List<String> columnNames = new Vector<>();
        for (Integer i = 0; i < rsmd.getColumnCount(); i++) {
            columnNames.add(rsmd.getColumnName(i + 1));
        }
        String[] cNames = columnNames.toArray(new String[rsmd.getColumnCount()]);
        rs.beforeFirst();
        for (Integer i = 0; rs.next(); i++) {
            String[] arr = new String[rsmd.getColumnCount()];
            for (Integer j = 0; j < rsmd.getColumnCount(); j++) {
                arr[j] = rs.getObject(j + 1).toString();
            }
            rsArr.add(arr);
            
        }
        for(String[] a : rsArr){
        	for(String b : a){
        		System.out.println(b);
        	}
        }
        if (rsArr.size() == 0) {
            String[] x = {"", ""};
            rsArr.add(x);
        }
        String[][] r = rsArr.toArray(new String[rsArr.size()][rsmd.getColumnCount()]);
        return r;
    }
	
}
