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

@MultipartConfig
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Connection connection;

    public void init() throws ServletException {
    	ServletContext servletContext = getServletContext();
		connection = DBConnectionProvider.getConnection(servletContext);
			}

      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		// TODO Auto-generated method stub
    		response.getWriter().append("Served at: ").append(request.getContextPath());
    	}

      protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    		//create the variable to store a possible error
        	ErrorMessage errorMessage;

          //create the variables
        String name = null;
        String surname = null;
        String email = null;
        String pwd = null;
        String confirmPwd = null;
        String address = null;
        System.out.println(request.toString());

        try {
    			name = request.getParameter("name");
    			System.out.println(name);
    			surname = request.getParameter("surname");
    			System.out.println(surname);
    			email = request.getParameter("email");
    			System.out.println(email);
    			pwd = request.getParameter("pwd");
    			System.out.println(pwd);
    			confirmPwd = request.getParameter("confirmPwd");
    			System.out.println(confirmPwd);
    			address = request.getParameter("address");
    			if (name == null || surname == null || email == null || pwd == null || address == null || name.isEmpty() || surname.isEmpty() || email.isEmpty() || pwd.isEmpty() || address.isEmpty()) {
    				throw new Exception("Missing or empty credential value");
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    			System.out.println("Problem 1");
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    			response.getWriter().println("Missing credential value");
    			return;
    		}

        if (!pwd.equals(confirmPwd)) {
          //check the same psw even on Server side
        	System.out.println("Problem 2");
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    			response.getWriter().println("No matching passwords");
    			return;
        }

        //create new user in DB
    		UserDAO userDAO = new UserDAO(connection);
    		try {
    			userDAO.createUser(name, surname, email, AuthUtils.encryptString(pwd), address);
    		} catch (DatabaseException e) {
    			e.printStackTrace();
    			System.out.println("Problem 3");
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          response.getWriter().println("Database Error");
    			return;
    		}

        response.setStatus(HttpServletResponse.SC_OK);
    	  response.setContentType("application/json");
    	  response.setCharacterEncoding("UTF-8");

        }
}

      
