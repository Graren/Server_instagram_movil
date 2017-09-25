package com.bagres.instagram;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/signup")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
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
		response.setHeader("Content-Type","application/json");
		String user_id = request.getParameter("user_id");
		String user_name = request.getParameter("user_name");
		String user_password = request.getParameter("user_password");
		String user_email = request.getParameter("user_email");
		String user_datebirth = request.getParameter("user_datebirth");
		boolean user_gender= Boolean.parseBoolean(request.getParameter("user_gender"));
		String user_description = request.getParameter("user_description");
		Integer id = Integer.parseInt(user_id);

		Gson js = new Gson();
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/instagramdb", "postgres", "masterkey");
			conn.setAutoCommit(false);
			String query = "INSERT INTO user (user_id,user_name,user_password,user_email,user_datebirth,user_gender,user_description) VALUES(?,?,?,?,?,?,?)";
			Object[] values = {user_id,user_name,user_password,user_email,user_datebirth,user_gender,user_description};
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, id);
			st.setString(2, user_name);
			st.setString(3, user_password);
			st.setString(4, user_email);
			st.setDate(2, java.sql.Date.valueOf(user_datebirth));
			st.setBoolean(6, user_gender);
			st.setString(7, user_description);
			st.executeUpdate();
			st.close();
			js.add("status",200).add("message","The User has been created");
		}
		catch(Exception e1) {
			e1.printStackTrace();
			js.add("status",404).add("Message","Error");
		}
		System.out.print(js);
		response.getWriter().print(js);
	}
}
