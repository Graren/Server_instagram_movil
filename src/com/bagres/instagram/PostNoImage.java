package com.bagres.instagram;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
 * Servlet implementation class PostNoImage
 */
@WebServlet("/PostNoImage")
public class PostNoImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostNoImage() {
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
	    String lat = request.getParameter("gps_lat");
	    String lng = request.getParameter("gps_lng");
	    String desc = request.getParameter("desc");
	    String path = request.getParameter("path");
	    String ext = request.getParameter("ext");
	    Integer user_id = Integer.valueOf(request.getParameter("user_id"));
		DBConn Dcon = new DBConn();
		Connection c = Dcon.getSQLConn();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date da = new Date();
		java.sql.Date sqlStartDate = null;
		sqlStartDate = new java.sql.Date(da.getTime());
		Gson j = new Gson();
		JsonObject js = new JsonObject();
	    try{
			try {
				String query = "INSERT INTO publication "
						+ "(user_id, publication_name,publication_description,publication_date,publication_path,publication_extension) "
						+ "VALUES(?,?,?,?,?,?) returning id_publication";
				String query2 = "INSERT INTO location (id_publication,location_longitude,location_latitude,location_description) values (?,?,?,?)";
				Object[] values = {user_id,name,desc,sqlStartDate,path, ext};
				PreparedStatement st = c.prepareStatement(query,ResultSet.TYPE_SCROLL_SENSITIVE,
						  ResultSet.CONCUR_READ_ONLY);
				Integer i = 1;
				for(Object o : values){
					st.setObject(i, o);
					i++;
				}
				ResultSet rs = st.executeQuery();
				String[][] s = Helper.getResult(rs);
				String p_id = s[0][0];
				System.out.println(p_id);
				st.close();
				st = c.prepareStatement(query2,ResultSet.TYPE_SCROLL_SENSITIVE,
						  ResultSet.CONCUR_READ_ONLY);
				values = null;
				String[] values2 = {p_id,lat,lng,"nowhere"};
				i = 1;
				for(Object o : values2){
					if (i == 1){
						st.setObject(i, Integer.valueOf((String)o));
					}
					else{
						st.setObject(i, o);
					}
					i++;
				}
				st.executeUpdate();
				js.addProperty("status",200);
				js.addProperty("message","Added Succesfully");
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(c != null) c.close();
			}
		}
		catch (Exception e){
			e.printStackTrace();
			js.addProperty("status",500);
			js.addProperty("message","Fuck");
		}
	    response.getWriter().print(js);
	}

}
