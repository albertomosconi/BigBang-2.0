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
import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.beans.View;
import it.polimi.tiw.bigbang.dao.ItemDAO;
import it.polimi.tiw.bigbang.dao.ViewDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;

/* this Servlet is used after the action of the client asking to visualize an item
		after a Serach to be able to see all the information about the item itself */

public class doView extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletContext servletContext;
	private Connection connection;

	public void init() throws ServletException {
		servletContext = getServletContext();
		connection = DBConnectionProvider.getConnection(servletContext);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		// get the user by the session
		User user = (User) session.getAttribute("user");
		Integer idUser = user.getId();

		// create the variable to store a possible error
		ErrorMessage errorMessage;
		errorMessage = (ErrorMessage) session.getAttribute("error");

		// create a variable for the id of the item of which the user ask the
		// visualization
		Integer idItemAsked = null;
		// create a Bean for the new View created
		View view = null;
		try {
			idItemAsked = Integer.parseInt(request.getParameter("idItem"));
				// check the paramether
			if (idItemAsked == null || idItemAsked < 0) {

				throw new Exception("Id asked to be viewed not valid or problem in word searched error");
			}

				//check if the id is present in the DB
			Item item = null;
			ItemDAO itemDAO = new ItemDAO(connection);
			try{
				item = itemDAO.findOneByItemId(idItemAsked);
			}catch (DatabaseException e1) {

	      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	      response.getWriter().println("Item not found!");
				return;
			}

			if (item == null) {
				//item not found in the DB
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Item not found!");
				return;
			}


			// check OK, so create the Bean View
			view = new View();
			view.setUser_id(idUser);
			view.setItem_id(idItemAsked);

		} catch (Exception e) {
			// check KO

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Request Error, Problem in finding idItem to visualized!");
			return;
		}

		// save the new object in the DB
		ViewDAO viewDAO = new ViewDAO(connection);
		try {
			viewDAO.createOneViewByUserIdAndItemId(idUser, idItemAsked);

		} catch (DatabaseException e1) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Database Error");
			return;
		}

		// the POST end successfully
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
