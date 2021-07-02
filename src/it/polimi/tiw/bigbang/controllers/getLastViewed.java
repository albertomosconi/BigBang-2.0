package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
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

import it.polimi.tiw.bigbang.beans.ExtendedItem;
import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.Price;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.beans.Vendor;
import it.polimi.tiw.bigbang.dao.ExtendedItemDAO;
import it.polimi.tiw.bigbang.dao.ItemDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;

public class getLastViewed extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletContext servletContext;
	private Connection connection;

	public void init() throws ServletException {
		servletContext = getServletContext();
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
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Database error");
			return;
		}

		if (items == null || items.isEmpty() || items.size() < 5) {
			List<Item> fillerItems = new ArrayList<>();

			try {
				fillerItems = itemDAO.findManyByCateogoryAndNumber("Books", 5);
			} catch (DatabaseException e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Database error");
				return;
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

//		private int id;
//		private String name;
//		private String description;
//		private String category;
//		private String picture;
		Gson gson = new GsonBuilder().create();
		String extendedItemsJson = "[";
		for (ExtendedItem extendedItem : extendedItems) {
			extendedItemsJson += "{\"id\":" + extendedItem.getId() + ",\"name\":\"" + extendedItem.getName()
					+ "\",\"description\":\"" + extendedItem.getDescription().replace("\"", "\\\"")
					+ "\",\"category\":\"" + extendedItem.getCategory() + "\",\"picture\":\""
					+ extendedItem.getPicture() + "\",";
			String vendorString = "[";
			String priceString = "[";
			for (Map.Entry<Vendor, Price> entry : extendedItem.getValue().entrySet()) {
				Vendor v = entry.getKey();
				Price p = entry.getValue();
				vendorString += gson.toJson(v) + ",";
				priceString += gson.toJson(p) + ",";
			}
			vendorString = vendorString.substring(0, vendorString.length() - 1);
			priceString = priceString.substring(0, priceString.length() - 1);
			vendorString += "]";
			priceString += "]";
			extendedItemsJson += "\"vendorList\":" + vendorString + ",";
			extendedItemsJson += "\"priceList\":" + priceString + "},";
		}
		extendedItemsJson = extendedItemsJson.substring(0, extendedItemsJson.length() - 1);
		extendedItemsJson += "]";

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

//		String extendedItemString = gson.toJson(extendedItems);
		response.getWriter().println(extendedItemsJson);
	}
}
