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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.OrderInfo;
import it.polimi.tiw.bigbang.beans.OrderedItem;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.beans.Vendor;
import it.polimi.tiw.bigbang.dao.ItemDAO;
import it.polimi.tiw.bigbang.dao.OrderDAO;
import it.polimi.tiw.bigbang.dao.VendorDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;

public class goOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private ServletContext servletContext;

	@Override
	public void init() throws ServletException {
		servletContext = getServletContext();
		connection = DBConnectionProvider.getConnection(servletContext);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");
		OrderDAO orderDAO = new OrderDAO(connection);
		Map<OrderInfo, List<OrderedItem>> orders = new LinkedHashMap<>();
		try {
			orders = orderDAO.findManyByUserID(user.getId());
		} catch (DatabaseException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Database error: " + e.getBody());
			return;
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
			itemDetails = itemDAO.findManyByItemsId(itemIDs);
		} catch (DatabaseException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Database error: "+e.getBody());
			return;
		}

		VendorDAO vendorDAO = new VendorDAO(connection);
		List<Vendor> vendorDetails = new ArrayList<>();
		try {
			vendorDetails = vendorDAO.findManyByVendorsId(vendorIDs);
		} catch (DatabaseException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Database error: "+e.getBody());
			return;
		}

		Map<Integer, Item> itemDetailsMap = new HashMap<>();
		for (int i = 0; i < itemIDs.size(); i++) {
			itemDetailsMap.put(itemIDs.get(i), itemDetails.get(i));
		}
		Map<Integer, Vendor> vendorDetailsMap = new HashMap<>();
		for (int i = 0; i < vendorIDs.size(); i++) {
			vendorDetailsMap.put(vendorIDs.get(i), vendorDetails.get(i));
		}

		Gson gson = new GsonBuilder().create();
		String ordersString = "[";
		for (Map.Entry<OrderInfo, List<OrderedItem>> entry : orders.entrySet()) {
			OrderInfo orderInfo = entry.getKey();
			List<OrderedItem> orderedItems = entry.getValue();
			ordersString += "{\"id\":\"" + orderInfo.getId() + "\"," + "\"id_user\":" + orderInfo.getId_user() + ","
					+ "\"date\":\"" + orderInfo.getDate() + "\"," + "\"shipping_cost\":" + orderInfo.getShipping_cost()
					+ "," + "\"total_items_cost\":" + orderInfo.getTotal_items_cost() + "," + "\"vendor\":"
					+ gson.toJson(vendorDetailsMap.get(orderInfo.getId_vendor())) + ",";
			ordersString += "\"items\":[";
			for (OrderedItem item : orderedItems) {
				ordersString += "{\"quantity\":" + item.getQuantity() + "," + "\"cost\":" + item.getCost() + ",";
				ordersString += "\"details\":" + gson.toJson(itemDetailsMap.get(item.getId_item())) + "},";
			}
			ordersString = ordersString.substring(0, ordersString.length() - 1);
			ordersString += "]},";
		}
		ordersString = ordersString.substring(0, ordersString.length() - 1);
		ordersString += "]";

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(ordersString);
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
