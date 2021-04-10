package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.OrderInfo;
import it.polimi.tiw.bigbang.beans.OrderedItem;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.beans.Vendor;
import it.polimi.tiw.bigbang.dao.ItemDAO;
import it.polimi.tiw.bigbang.dao.OrderDAO;
import it.polimi.tiw.bigbang.dao.VendorDAO;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;
import it.polimi.tiw.bigbang.utils.TemplateEngineProvider;

public class goOrders extends HttpServlet {
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
		OrderDAO orderDAO = new OrderDAO(connection);
		Map<OrderInfo, List<OrderedItem>> orders = new LinkedHashMap<>();
		try {
			orders = orderDAO.findOrdersByUserID(user.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ArrayList<Integer> itemIDs = new ArrayList<>();
		List<Integer> vendorIDs = new ArrayList<>();
		for (Map.Entry<OrderInfo, List<OrderedItem>> entry : orders.entrySet()) {
			if (!vendorIDs.contains(entry.getKey().getId_vendor()))
				vendorIDs.add(entry.getKey().getId_vendor());
			for (OrderedItem item : entry.getValue()) {
				if (!itemIDs.contains(item.getId_item()))
					itemIDs.add(item.getId_item());
			}
		}
		
		ItemDAO itemDAO = new ItemDAO(connection);
		List<Item> itemDetails = new ArrayList<>();
		try {
			itemDetails = itemDAO.findItemsById(itemIDs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		VendorDAO vendorDAO = new VendorDAO(connection);
		List<Vendor> vendorDetails = new ArrayList<>();
		try {
			vendorDetails = vendorDAO.findById(vendorIDs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Map<Integer, Item> itemDetailsMap = new HashMap<>();
		for (int i = 0; i < itemIDs.size(); i++) {
			itemDetailsMap.put(itemIDs.get(i), itemDetails.get(i));
		}
		Map<Integer, Vendor> vendorDetailsMap = new HashMap<>();
		for (int i = 0; i < vendorIDs.size(); i++) {
			vendorDetailsMap.put(vendorIDs.get(i), vendorDetails.get(i));
		}
		
		String path = "orders";
		final WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
		webContext.setVariable("user", user);
		webContext.setVariable("orders", orders);
		webContext.setVariable("itemDetails", itemDetailsMap);
		webContext.setVariable("vendorDetails", vendorDetailsMap);
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
