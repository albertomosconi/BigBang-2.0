package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.SelectedItem;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.beans.Vendor;
import it.polimi.tiw.bigbang.dao.ItemDAO;
import it.polimi.tiw.bigbang.dao.OrderDAO;
import it.polimi.tiw.bigbang.dao.PriceDAO;
import it.polimi.tiw.bigbang.dao.VendorDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;
import it.polimi.tiw.bigbang.utils.OrderUtils;

public class doOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private ServletContext servletContext;

	@Override
	public void init() throws ServletException {
		servletContext = getServletContext();
		connection = DBConnectionProvider.getConnection(servletContext);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get the cart from the session
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Map<Integer, HashMap<Integer, Integer>> cart = (Map<Integer, HashMap<Integer, Integer>>) session
				.getAttribute("cartSession");

		// get the vendor id from the request
		Integer vendorID;
		String vendorString = request.getParameter("vendorId");
		if (vendorString != null && !vendorString.equals("")) {
			try {
				vendorID = Integer.parseInt(vendorString);
			} catch (NumberFormatException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Vendor Parameter Error: not corret format of credential value");
				return;
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Vendor Parameter Error: not corret format of credential value");
			return;
		}
		if (vendorID == null || vendorID < 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Vendor Parameter Error: missing or empty value");
			return;
		}		

		// get the vendor details
		VendorDAO vendorDAO = new VendorDAO(connection);
		Vendor vendor = new Vendor();
		try {
			vendor = vendorDAO.fineOneByVendorId(vendorID);
		} catch (DatabaseException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Database error: " + e.getBody());
			return;
		}

		// get all the items with their respective quantities for this vendor
		HashMap<Integer, Integer> items = cart.get(vendorID);
		
		// check if items is empty
		if (items == null || items.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Vendor Parameter Error: you don't have any items from this vendor in your cart.");
			return;
		}

		// get details for all items

		ItemDAO itemDAO = new ItemDAO(connection);
		List<Item> fullItems = new ArrayList<>();
		try {
			fullItems = itemDAO.findManyByItemsId(new ArrayList<>(items.keySet()));
		} catch (DatabaseException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Database error: " + e.getBody());
			return;
		}

		PriceDAO priceDAO = new PriceDAO(connection);

		List<SelectedItem> selectedItems = new ArrayList<>();
		for (Item i : fullItems) {
			SelectedItem selectedItem = new SelectedItem();
			selectedItem.setItem(i);
			selectedItem.setQuantity(items.get(i.getId()));
			try {
				selectedItem.setCost(priceDAO.findOneByItemIdAndVendorId(i.getId(), vendorID).getPrice());
			} catch (DatabaseException e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Database error: " + e.getBody());
				return;
			}
			selectedItems.add(selectedItem);
		}

		float shipping_cost = OrderUtils.calculateShipping(vendor, selectedItems);

		OrderDAO orderDAO = new OrderDAO(connection);
		try {
			orderDAO.createOrder(user.getId(), vendorID, shipping_cost, selectedItems);
		} catch (DatabaseException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Database error: " + e.getBody());
			return;
		}

		cart.remove(vendorID);
		session.setAttribute("cartSession", cart);

		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println("order created successfully");
	}
}
