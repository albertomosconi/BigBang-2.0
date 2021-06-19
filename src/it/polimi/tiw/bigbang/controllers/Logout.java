package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		if (session.getAttribute("user") != null) {
			session.removeAttribute("user");
		}
    response.setStatus(HttpServletResponse.SC_OK);
	  response.setContentType("application/json");
	  response.setCharacterEncoding("UTF-8");
	}
}
