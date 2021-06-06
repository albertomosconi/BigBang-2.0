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

import it.polimi.tiw.bigbang.utils.DBConnectionProvider;
import it.polimi.tiw.bigbang.utils.TemplateEngineProvider;
import it.polimi.tiw.bigbang.beans.ExtendedItem;
import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.dao.ExtendedItemDAO;
import it.polimi.tiw.bigbang.dao.ItemDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;

public class goHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	private Connection connection;
	private ServletContext servletContext;

	public void init() throws ServletException {
		servletContext = getServletContext();
		templateEngine = TemplateEngineProvider.getTemplateEngine(servletContext);
		connection = DBConnectionProvider.getConnection(servletContext);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		ItemDAO itemDAO = new ItemDAO(connection);
		List<Item> items = new ArrayList<>();
		try {
			items = itemDAO.findLastViewedByUserId(user.getId());
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		if (items == null || items.isEmpty() || items.size() < 5) {
			List<Item> fillerItems = new ArrayList<>();

			try {
				fillerItems = itemDAO.findManyByCateogoryAndNumber("Books", 5);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}

			for (Item fillerItem : fillerItems) {
				boolean found = false;
				for (Item item : items) {
					if (item.getId() == fillerItem.getId()) {
						found = true;
						break;
					}
				}
				if (!found && items.size() < 5) {
					items.add(fillerItem);
				}
				if (items.size() == 5)
					break;
			}
		}
		
		List<ExtendedItem> extendedItems = new ArrayList<>();
		ExtendedItemDAO extendedItemDAO = new ExtendedItemDAO(connection);
		
		extendedItems = extendedItemDAO.findManyItemsDetailsByCompressedItems(items);

		String path = "home";
		final WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
		webContext.setVariable("lastViewedItems", extendedItems);
		webContext.setVariable("user", user);
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
