package com.bagres.instagram;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
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
import javax.servlet.http.Part;

import com.google.gson.Gson;

/**
 * Servlet implementation class Likes
 */
@WebServlet("/Likes")
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String p_id = request.getParameter("p_id");
		String pass = request.getParameter("u_id");
		String like = request.getParameter("uLiked");
		Boolean uLike = ("true").equals(like) ? true: false;
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
					+ "WHERE user_action_publication.id_publication = "
					+ "GROUP BY user_action_publication.id_publication" + p_id);
			
			response.getWriter().print(g.toJson(Helper.getResult(rs)));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print("fuck");
		}
	}

}
