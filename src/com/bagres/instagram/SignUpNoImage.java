package com.bagres.instagram;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * Servlet implementation class SignUpNoImage
 */
@WebServlet("/SignUpNoImage")
public class SignUpNoImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpNoImage() {
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
				String user_name = request.getParameter("user_name");
				String user_password = request.getParameter("user_password");
				String user_email = request.getParameter("user_email");
				String user_birthdate = request.getParameter("user_birthdate");
				boolean user_gender= Boolean.parseBoolean(request.getParameter("user_gender"));
				String user_description = request.getParameter("user_description");
				String user_fullname = request.getParameter("user_fullname");
				String path = request.getParameter("path");
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
				java.util.Date date;
				java.sql.Date sqlStartDate = null;
				try {
					date = sdf1.parse(user_birthdate);
					sqlStartDate = new java.sql.Date(date.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Gson j = new Gson();
				JsonObject js = new JsonObject();
				
				DBConn Dcon = new DBConn();
				Connection c = Dcon.getSQLConn();
		        Date d = new Date();
		        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		        Integer game_id;
				try {
					try{
						String query = "INSERT INTO users "
								+ "(user_name,user_password,user_email,user_gender,user_description, user_image_path,user_fullname,user_birthdate) "
								+ "VALUES(?,?,?,?,?,?,?,?)";
						PreparedStatement st = c.prepareStatement(query);
						st.setString(1, user_name);
						st.setString(2, user_password);
						st.setString(3, user_email);
						st.setBoolean(4, user_gender);
						st.setString(5, user_description);
						st.setString(6, path);
						st.setString(7, user_fullname);
						st.setDate(8, sqlStartDate );
						st.execute();
						js.addProperty("status",200);
						js.addProperty("message","The User has been created");
					}
					catch (Exception e){
						e.printStackTrace();
						throw e;
					}
					finally{
						if(c != null) {
							System.out.println("AAAAa");
							c.close();
						}
						
					}
					
				}
				catch(Exception e1) {
					e1.printStackTrace();
					js.addProperty("status",404);
					js.addProperty("Message","Error");
				}
				response.getWriter().print(js);
	}

}
