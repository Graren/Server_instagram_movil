package com.bagres.instagram;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Bagrelet
 */
@WebServlet("/Bagrelet")
public class Bagrelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Bagrelet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Gson g = new Gson();
		JsonObject js = new JsonObject();
		JsonArray jarr = new JsonArray();
		String[] bagres = {"Osc", "ed", "reina", "ore", "ron"};
		Arrays.asList(bagres).forEach((bagre) -> {
			JsonObject bager = new JsonObject();
			bager.addProperty("name", bagre);
			jarr.add(bager);
		});
		js.add("bagres", jarr );
		response.getWriter().print(g.toJson(js));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
