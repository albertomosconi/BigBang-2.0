package it.polimi.tiw.bigbang.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class QueryFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		String requestURI = req.getRequestURI();

		if (session.isNew() || session.getAttribute("user") == null) {
			if (isPathResource(requestURI) || requestURI.endsWith("/") || requestURI.endsWith("/login.html") || requestURI.endsWith("/login") || requestURI.endsWith("/register.html") || requestURI.endsWith("/register")) {
				chain.doFilter(request, response);
				return;
			} else {
				res.sendRedirect("login.html");
				return;
			}
		}

		List<String> allowedPaths = new ArrayList<>(
				Arrays.asList("/cart", "/orders", "/search", "/register", "/doAddCart", "/doOrder", "/logout", "/lastViewed", "/visualize", "/home.html"));

		if (!allowedPaths.contains(requestURI.replace(req.getContextPath(), "")) && !isPathResource(requestURI)) {
			res.sendRedirect("home.html");
			return;
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	private boolean isPathResource(String path) {
		return path.endsWith(".css") || path.endsWith(".jpg") || path.endsWith(".js")|| path.endsWith(".png");
	}
}
