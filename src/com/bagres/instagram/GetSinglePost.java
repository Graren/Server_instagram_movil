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

/**
 * Servlet implementation class GetSinglePost
 */
@WebServlet("/getSinglePost")
public class GetSinglePost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSinglePost() {
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
					ResultSet rs = stmt.executeQuery("SELECT "
							+ "p.id_publication as pid, "
							+ "u.user_name as username, "
							+ "p.publication_name as pName, "
							+ "p.publication_description as description, "
							+ "p.publication_date as date,"
							+ "p.publication_path as url, "
							+ "p.publication_extension as ext, "
							+ "l.gps_longitude as lng, "
							+ "l.gps_latitude as lat "
							+ "sum(case when user_action.id_publication = " + p_id + " then 1 else 0 ) "
							+ "FROM publication p "
							+ "INNER JOIN location l ON p.id_publication = l.id_publication "
							+ "INNER JOIN user_action u ON u.id_publication = p.id_publication "
							+ "INNER JOIN user us ON us.user_id = p.user_id "
							+ "WHERE p.id_publication=" + p_id);
					response.getWriter().print(g.toJson(Helper.getResult(rs)));
					conn.close();
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
		doGet(request, response);
	}

}
