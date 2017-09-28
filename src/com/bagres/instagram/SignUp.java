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
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/signup")
@MultipartConfig
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
		String user_name = request.getParameter("user_name");
		String user_password = request.getParameter("user_password");
		String user_email = request.getParameter("user_email");
		String user_birthdate = request.getParameter("user_birthdate");
		boolean user_gender= Boolean.parseBoolean(request.getParameter("user_gender"));
		String user_description = request.getParameter("user_description");
		String user_fullname = request.getParameter("user_fullname");
		Part filePart = request.getPart("file");
	    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
	    String[] ps = fileName.split("\\.");
	    String ext = ps[ps.length-1];
	    System.out.println(fileName);
	    System.out.println(ext);
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
		Connection conn = null;
		try {
			InputStream fileContent = filePart.getInputStream();
		    OutputStream os = null;
			try{
				String base = BagrePaths.path;
				String filePath = base + "/" + System.currentTimeMillis() + "." + ext;
				os = new FileOutputStream(filePath);
				int read = 0;
				byte[] bytes = new byte[1024];
				while( (read = fileContent.read(bytes)) != -1){
					os.write(bytes,0,read);
				};
				Class.forName("org.postgresql.Driver");
				conn = DriverManager
						.getConnection(DBValues.buildConnectionString(),
								DBValues.user, DBValues.password);
				conn.setAutoCommit(false);
				String query = "INSERT INTO users "
						+ "(user_name,user_password,user_email,user_birthdate,user_gender,user_description, user_image_path,user_fullname) "
						+ "VALUES(?,?,?,?,?,?,?,?)";
				PreparedStatement st = conn.prepareStatement(query);
				st.setString(1, user_name);
				st.setString(2, user_password);
				st.setString(3, user_email);
				st.setDate(4, sqlStartDate );
				st.setBoolean(5, user_gender);
				st.setString(6, user_description);
				st.setString(7, filePath);
				st.setString(8, user_fullname);
				st.executeUpdate();
				st.close();
				js.addProperty("status",200);
				js.addProperty("message","The User has been created");
			}
			catch (Exception e){
				e.printStackTrace();
				throw e;
			}
			finally{
				if(fileContent != null) fileContent.close();
				if(os != null) os.close();
				if(conn != null) {
					System.out.println("AAAAa");
					conn.close();
				}
				
			}
			
		}
		catch(Exception e1) {
			e1.printStackTrace();
			js.addProperty("status",404);
			js.addProperty("Message","Error");
		}
		System.out.print(js);
		response.getWriter().print(js);
	}
}
