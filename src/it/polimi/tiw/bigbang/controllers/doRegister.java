package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

import it.polimi.tiw.bigbang.beans.ErrorMessage;
import it.polimi.tiw.bigbang.dao.UserDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;
import it.polimi.tiw.bigbang.utils.AuthUtils;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;

/* this Servlet is called after a Registration query by a client
		after he/she filled the specific form with all the info
		needed for a Client, those have to be store in the DB too */

//to read a form send by the (Client side)
@MultipartConfig
public class doRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Connection connection;

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		connection = DBConnectionProvider.getConnection(servletContext);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// create the variable to store a possible error
		ErrorMessage errorMessage;

		// create the variables for the Client
		String name = null;
		String surname = null;
		String email = null;
		String pwd = null;
		String confirmPwd = null;
		String address = null;

		try {
			// get the form input from the request
			name = request.getParameter("name");

			surname = request.getParameter("surname");

			email = request.getParameter("email");

			pwd = request.getParameter("psw");

			confirmPwd = request.getParameter("confirmPwd");

			address = request.getParameter("address");

			// check the validity of the inputs
			if (name == null || surname == null || email == null || pwd == null || address == null || name.isEmpty()
					|| surname.isEmpty() || email.isEmpty() || pwd.isEmpty() || address.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}
		} catch (Exception e) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Missing credential value");
			return;
		}

		if (!pwd.equals(confirmPwd)) {
			// check the same psw even on Server side
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("No matching passwords");
			return;
		}

		// create new user in the DB
		UserDAO userDAO = new UserDAO(connection);
		//
		int occurency = 0;
		try {
			occurency = userDAO.findFromEmail(email);
		} catch (DatabaseException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Database Error: " + e.getBody());
			return;
		}

		if (occurency>0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("The user has already been registered");
			return;
		}

		try {

			userDAO.createUser(name, surname, email, AuthUtils.encryptString(pwd), address);

		} catch (DatabaseException e) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Database Error");
			return;
		}

		// POST ended successfully
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

	}
}
