package com.bagres.instagram;

import java.io.IOException;
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
		String user_fullname = request.getParameter("user_fullname");
		String dateString = request.getParameter("user_datebirth");
		boolean user_gender= Boolean.parseBoolean(request.getParameter("user_gender"));
		String user_description = request.getParameter("user_description");
		Integer id = Integer.parseInt(user_id);
		Date user_datebirth = null;
		try {
			user_datebirth = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		Gson js = new Gson();
		try {
			String query = "INSERT INTO user (user_id,user_name,user_password,user_fullname,user_datebirth,user_gender,user_description) VALUES(?,?,?,?,?,?,?)";
			Object[] values = {user_id,user_name,user_password,user_fullname,user_datebirth,user_gender,user_description};
			JDBCon jdbc = new JDBCon();
			jdbc.doConnect(5432,"localhost","instagramdb","postgres","masterkey");
			jdbc.execute(query,values);
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
