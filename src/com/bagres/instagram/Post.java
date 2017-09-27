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
 * Servlet implementation class Post
 */
@WebServlet("/post")
@MultipartConfig
public class Post extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Post() {
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
	    String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
	    String name = request.getParameter("name");
	    String date = request.getParameter("date");
	    String lat = request.getParameter("gps_lat");
	    String lng = request.getParameter("gps_lng");
	    String desc = request.getParameter("desc");
	    Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
	    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
	    String ext = fileName.split("\\.")[1];
	    InputStream fileContent = filePart.getInputStream();
	    OutputStream os = null;
		try{
			String base = "C:/test";
			String filePath = base + "/" + System.currentTimeMillis() + "." + ext;
			os = new FileOutputStream(filePath);
			int read = 0;
			byte[] bytes = new byte[1024];
			while( (read = fileContent.read(bytes)) != -1){
				os.write(bytes,0,read);
			};
			Gson j = new Gson();
			JsonObject js = new JsonObject();
			Connection conn = null;
			try {
				Class.forName("org.postgresql.Driver");
				conn = DriverManager
						.getConnection(DBValues.buildConnectionString(),
								DBValues.user, DBValues.password);
				conn.setAutoCommit(false);
				String query = "INSERT INTO post p "
						+ "(publication_name,publication_description,publication_date,publication_path,publication_ext) "
						+ "VALUES(?,?,?,?,?)";
				Object[] values = {name,description,date,base + "/" + fileName, ext};
				PreparedStatement st = conn.prepareStatement(query);
				Integer i = 1;
				for(Object o : values){
					st.setObject(i, o);
					i++;
				}
				ResultSet rs = st.executeQuery();
				String[][] s = Helper.getResult(rs);
				for(String[] r : s){
					for(String c: r){
						System.out.println(c);
					}
				}
				st.close();
				js.addProperty("status",200);
				js.addProperty("message","The User has been created");
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(conn != null) conn.close();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			if(fileContent != null) fileContent.close();
			if(os != null) os.close();
			
		}
	}

}
