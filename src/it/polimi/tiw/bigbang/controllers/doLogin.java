package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.bigbang.beans.ErrorMessage;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.dao.UserDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;
import it.polimi.tiw.bigbang.utils.AuthUtils;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;
import it.polimi.tiw.bigbang.utils.TemplateEngineProvider;

@MultipartConfig
public class doLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;
	private ServletContext servletContext;

	@Override
	public void init() throws ServletException {
		servletContext = getServletContext();
		connection = DBConnectionProvider.getConnection(servletContext);
		templateEngine = TemplateEngineProvider.getTemplateEngine(servletContext);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		String path = "login";
		templateEngine.process(path, ctx, response.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ErrorMessage errorMessage = null;
		
		// obtain and escape params
		String email = null;
		String pwd = null;
		try {
			// usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			// pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			email = request.getParameter("email");
			pwd = request.getParameter("pwd");
			if (email == null || pwd == null || email.isEmpty() || pwd.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}

		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Missing credential value");
			return;
		}

		// query db to authenticate for user
		UserDAO userDao = new UserDAO(connection);
		User user = null;
		try {
			user = userDao.checkCredentials(email, AuthUtils.encryptString(pwd));
		} catch (DatabaseException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getBody());
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message
		String path;
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Invalid Credentials");
			return;
		} else {
			request.getSession().setAttribute("user", user);
			// [VendorId || ItemId, Quantity]
			//debugging
			HashMap<Integer, HashMap<Integer, Integer>> cart = new HashMap<Integer, HashMap<Integer, Integer>>();
			HashMap<Integer, Integer> item = new HashMap<Integer, Integer>();
			item.put(1,1);
			item.put(2, 2);
			cart.put(1, item);
			HashMap<Integer, Integer> item2 = new HashMap<Integer, Integer>();
			item2.put(8, 8);
			cart.put(2, item2);
			request.getSession().setAttribute("cartSession", cart);
//			
			//request.getSession().setAttribute("cartSession", new HashMap<Integer, HashMap<Integer, Integer>>());
//			path = getServletContext().getContextPath() + "/home";
//			response.sendRedirect(path);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Gson gson = new GsonBuilder().create();
			String userString = gson.toJson(user);
			response.getWriter().println(userString);
		}
	}

	@Override
	public void destroy() {
		try {
			DBConnectionProvider.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
