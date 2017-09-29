package com.bagres.instagram;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Likes
 */
@WebServlet("/like")
public class Likes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Likes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String p_id = request.getParameter("p_id");
		Connection conn = null ;
		Gson g = new Gson();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			response.getWriter().print("fuck");
		}
		try {
			conn = DriverManager
					.getConnection(DBValues.buildConnectionString(),
							DBValues.user, DBValues.password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Statement stmt;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
												  ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("SELECT count(user_action_publication.user_id) "
					+ "FROM user_action_publication "
					+ "WHERE user_action_publication.id_publication = " + p_id 
					+ "GROUP BY user_action_publication.id_publication" );
			
			response.getWriter().print(g.toJson(Helper.getResult(rs)));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print("fuck");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String p_id = request.getParameter("p_id");
		String u_id = request.getParameter("u_id");
		String like = request.getParameter("uLiked");
		Boolean uLike = ("true").equals(like) ? true: false;
		
		Calendar calendar = Calendar.getInstance();

		DBConn Dcon = new DBConn();
		Connection c = Dcon.getSQLConn();
		java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());

		// Ideally specify the columns here as well...
		
		Gson g = new Gson();
		JsonObject js = new JsonObject();
		PreparedStatement stmt;
		try {
			stmt = c.prepareStatement("INSERT INTO user_action (user_id, id_publication,action_date,uLiked) "
					+ "VALUES (?,?,?,?) ON CONFLICT (user_id,id_publication) DO "
					+ "UPDATE SET uLiked=EXCLUDED.uLiked, action_date= EXCLUDED.action_date ",ResultSet.TYPE_SCROLL_SENSITIVE,
												  ResultSet.CONCUR_READ_ONLY);
			stmt.setInt(1, Integer.valueOf(u_id));
			stmt.setInt(2, Integer.valueOf(p_id));
			stmt.setDate(3, date);
			stmt.setBoolean(4, uLike);
			stmt.execute();
			js.addProperty("status",200);
			js.addProperty("message","Post liked");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			js.addProperty("status",500);
			js.addProperty("message","Failure liking");
		}
		finally{
			try{ c.close(); } catch(Exception e){};
		}
		response.getWriter().print(js);
	}

}
