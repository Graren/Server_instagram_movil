package com.bagres.instagram;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Feed
 */
@WebServlet("/feed")
public class Feed extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Feed() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String user = request.getParameter("user");
		String pass = request.getParameter("password");
		
		DBConn Dcon = new DBConn();
		Connection c = Dcon.getSQLConn();
		Gson g = new Gson();
		JsonObject js = new JsonObject();
		JsonArray jarr = new JsonArray();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			response.getWriter().print("fuck");
		}
		Statement stmt;
		try {
			stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
												  ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("SELECT "
					+ "p.id_publication as pid, "
					+ "us.user_name as username, "
					+ "p.publication_name as pName, "
					+ "p.publication_description as description, "
					+ "p.publication_date as date,"
					+ "p.publication_path as url, "
					+ "p.publication_extension as ext, "
					+ "location.location_longitude as lng, "
					+ "location.location_latitude as lat, "
					+ "sum(Case when u.uliked is true then 1 else 0 end) as c "
					+ "FROM publication p "
					+ "INNER JOIN users us ON us.user_id = p.user_id "
					+ "INNER JOIN location ON p.id_publication = location.id_publication "
					+ "LEFT JOIN user_action u ON u.id_publication = p.id_publication "
					+ "GROUP BY p.id_publication, us.user_name, location.location_longitude,location.location_latitude");
			String[][] arr = Helper.getResult(rs);
			for(String[] a : arr){
				for(String b : a){
					
				}
			}
			response.getWriter().print(g.toJson(js));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print("fuck");
		}
		finally{
			if(c != null)
				try {
					c.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
