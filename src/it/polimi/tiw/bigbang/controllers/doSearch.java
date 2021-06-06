package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.bigbang.beans.ErrorMessage;
import it.polimi.tiw.bigbang.beans.ExtendedItem;
import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.dao.ExtendedItemDAO;
import it.polimi.tiw.bigbang.dao.ItemDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;
import it.polimi.tiw.bigbang.utils.TemplateEngineProvider;

public class doSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Connection connection;
	private TemplateEngine templateEngine;

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		connection = DBConnectionProvider.getConnection(servletContext);
		templateEngine = TemplateEngineProvider.getTemplateEngine(servletContext);
	}

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		//get the user by the session
		User user = (User) session.getAttribute("user");

		//create the variable to store a possible error
    	ErrorMessage errorMessage;

		//get the lists of item searched yet or viewed yet by the session, if there are
		List<ExtendedItem> viewItem = (List<ExtendedItem>) session.getAttribute("itemViewed");
		List<ExtendedItem> extendedItemSearch = (List<ExtendedItem>) session.getAttribute("itemSearch");

		//as default remove all viewed items because is a new search
		boolean clearViewedItemList = true;
		if(session.getAttribute("clearViewItemList")!=null) {
			clearViewedItemList = (boolean)session.getAttribute("clearViewItemList");

		}


		//check if is a new search
		//so remove all the old viewed Items
		if (clearViewedItemList) {
			//each time I do a new search, remove all old items searched and visualized
			request.getSession().removeAttribute("itemSearch");
			request.getSession().removeAttribute("itemViewed");
			viewItem = null;
			}



		// Get the search parameter, so the items asked to be viewed
		String wordSearched = null;
		try {
			wordSearched = request.getParameter("keyword");
			if (wordSearched == null || wordSearched.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}
		} catch (Exception e) {
			errorMessage = new ErrorMessage("Input Error", e.getMessage());
			String path = "search";
			ServletContext servletContext = getServletContext();
			session.removeAttribute("clearViewItemList");
			final WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
			webContext.setVariable("user", user);
			webContext.setVariable("error", errorMessage);
			webContext.setVariable("searchItem", extendedItemSearch);  //this will be null, so it will return 0 items
			if (viewItem == null) {
				//no item visualized yet
				viewItem = new ArrayList<>();
			}
			webContext.setVariable("itemViewed", viewItem);         // no items will be in the List
			templateEngine.process(path, webContext, response.getWriter());
			return;
		}

		ItemDAO itemDAO = new ItemDAO(connection);
		ExtendedItemDAO extendedItemDAO = new ExtendedItemDAO(connection);
		List<Item> searchItemsId = new ArrayList<>();
		extendedItemSearch = new ArrayList<>();
		try {
			searchItemsId = itemDAO.findManyByWord(wordSearched);
			extendedItemSearch = extendedItemDAO.findManyItemsDetailsByCompressedItems(searchItemsId);

			//put the search items in the session, so they now can be find by doView when redirect to search page
			request.getSession().setAttribute("itemSearch", extendedItemSearch);
		} catch (DatabaseException e) {
			errorMessage = new ErrorMessage("Database Error", e.getBody());
			String path = "search";
			ServletContext servletContext = getServletContext();
			session.removeAttribute("clearViewItemList");
			final WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
			webContext.setVariable("user", user);
			webContext.setVariable("keyword", wordSearched);
			webContext.setVariable("error", errorMessage);
			webContext.setVariable("searchItem", extendedItemSearch);  //this will be null, so it will return 0 items
			if (viewItem == null) {
				//no item visualized yet
				viewItem = new ArrayList<>();
			}
			webContext.setVariable("itemViewed", viewItem);         // no items will be in the List
			templateEngine.process(path, webContext, response.getWriter());
			return;
		}


		if (viewItem == null) {
			//no item visualized yet
			viewItem = new ArrayList<>();
		}

		//set it true so each new search clear the attributes
		//session.setAttribute("clearViewItemList", true);
		session.removeAttribute("clearViewItemList");


		// Redirect to the search Page with the items found
		String path = "search";
		ServletContext servletContext = getServletContext();
		final WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
		webContext.setVariable("itemViewed", viewItem);
		webContext.setVariable("searchItem", extendedItemSearch);
		webContext.setVariable("user", user);
		webContext.setVariable("keyword", wordSearched);
		templateEngine.process(path, webContext, response.getWriter());
	}



	public void destroy() {
		try {
			DBConnectionProvider.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
