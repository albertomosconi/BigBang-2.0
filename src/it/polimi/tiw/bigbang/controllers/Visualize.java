package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.context.WebContext;

import it.polimi.tiw.bigbang.beans.ErrorMessage;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.beans.View;
import it.polimi.tiw.bigbang.dao.ViewDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;

public class Visualize extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletContext servletContext;
  private Connection connection;

	public void init() throws ServletException {
	servletContext = getServletContext();
	connection = DBConnectionProvider.getConnection(servletContext);
	}

  @SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	  System.out.println("Enter in Visualize");
		HttpSession session = request.getSession();

		//get the user by the session
		User user = (User) session.getAttribute("user");
		Integer idUser = user.getId();

		//create the variable to store a possible error
		ErrorMessage errorMessage;
		errorMessage = (ErrorMessage) session.getAttribute("error");

		//get the id of the item of which the user ask the visualization
		Integer idItemAsked = null;
		//String wordSearchedString = null;

		//create a variable for the new View created
		View view = null;
		try {
			idItemAsked = Integer.parseInt(request.getParameter("idItem"));
			System.out.println(idItemAsked);
			//wordSearchedString = request.getParameter("keyword");

				// check the paramether from session
			if (idItemAsked == null || idItemAsked < 0) {
				
				throw new Exception("Id asked to be viewed not valid or problem in word searched error");
			}

				// check OK
			view = new View();
			view.setUser_id(idUser);
			view.setItem_id(idItemAsked);

		} catch (Exception e) {
				//check KO
			e.printStackTrace();
			
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Request Error, Problem in finding idItem to visualized!");
			return;
		}

		//creating the new object in the DB
		ViewDAO viewDAO = new ViewDAO(connection);
		try {
			viewDAO.createOneViewByUserIdAndItemId(idUser, idItemAsked);
			System.out.println("Do the post in DB");
		}catch (DatabaseException e1) {
			e1.printStackTrace();
			
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().println("Database Error");
			return;
		}

		//saving the viewId in the session
							//probably this will be useless
							/*
		List<Integer> idItemViewed = new ArrayList<Integer>();
		if(session.getAttribute("itemViewed")!=null) {
			idItemViewed = (List<Integer>) session.getAttribute("itemViewed");
			System.out.println("create new session attribute");
		}
		idItemViewed.add(idItemAsked);
		session.setAttribute("itemViewed", idItemViewed);

		//reloading the search page set this boolean attribute to false to not lost this and all old item viewed yet
		session.setAttribute("clearViewItemList", false);*/

		response.setStatus(HttpServletResponse.SC_OK);
	  response.setContentType("application/json");
	  response.setCharacterEncoding("UTF-8");

	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


}
