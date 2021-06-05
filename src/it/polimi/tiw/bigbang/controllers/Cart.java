package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.bigbang.beans.ErrorMessage;
import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.Price;
import it.polimi.tiw.bigbang.beans.SelectedItem;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.beans.Vendor;
import it.polimi.tiw.bigbang.dao.ItemDAO;
import it.polimi.tiw.bigbang.dao.PriceDAO;
import it.polimi.tiw.bigbang.dao.VendorDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;
import it.polimi.tiw.bigbang.utils.OrderUtils;

public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletContext servletContext;
	private Connection connection;

	public void init() throws ServletException {
		servletContext = getServletContext();
		connection = DBConnectionProvider.getConnection(servletContext);
	}
	
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get active user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// space for errors
		ErrorMessage errorMessage;
		errorMessage = (ErrorMessage) session.getAttribute("error");

		// Rebuilt cart from vendor id and list of item id
		HashMap<Vendor, List<SelectedItem>> cart = null;
		HashMap<Vendor, float[]> shipping = null;

		//error added to session from doAddCart
		if (errorMessage != null) {
			
			//rebuilt old cart and shipping
			cart = (HashMap<Vendor, List<SelectedItem>>) session.getAttribute("cartOld");
			shipping = (HashMap<Vendor, float[]>) session.getAttribute("shippingOld");
		
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Server error: could not add items to cart");
			return;
			
		}else {
			errorMessage=null;
		}

		// Get items added to cart from session
		HashMap<Integer, HashMap<Integer, Integer>> cartSession = new HashMap<Integer, HashMap<Integer, Integer>>();
		try {
			cartSession = (HashMap<Integer, HashMap<Integer, Integer>>) session.getAttribute("cartSession");
		} catch (NumberFormatException | NullPointerException e) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Server error: cart not found");
			return;
		}

		if (cartSession.isEmpty()) {
		
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			response.getWriter().println("Cart is empty");
			return;
		}

		cart = new HashMap<Vendor, List<SelectedItem>>();
		Set<Integer> vendorSet = cartSession.keySet();

		for (int vendor : vendorSet) {

			// collect information about vendor
			VendorDAO vendorDAO = new VendorDAO(connection);
			Vendor vendorCurrent = new Vendor();
			try {

				vendorCurrent = vendorDAO.fineOneByVendorId(vendor);
			} catch (DatabaseException e) {
			
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Database error: " + e.getBody());
				return;	
			}

			Set<Integer> itemsForVendorSet = cartSession.get(vendor).keySet();
			List<SelectedItem> selectedItemList = new ArrayList<SelectedItem>();

			for (int item : itemsForVendorSet) {

				// collect information about item
				ItemDAO itemDAO = new ItemDAO(connection);
				Item itemCurrent = new Item();
				try {
					itemCurrent = itemDAO.findOneByItemId(item);
				} catch (DatabaseException e) {
					
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println("Database error: " + e.getBody());
				
					return;	
				}

				// collect price
				PriceDAO priceDAO = new PriceDAO(connection);
				Price price = new Price();
				try {
					price = priceDAO.findOneByItemIdAndVendorId(item, vendor);
				} catch (DatabaseException e) {
					
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println("Database error: " + e.getBody());
					return;	
				}

				SelectedItem selectedItem = new SelectedItem();
				selectedItem.setItem(itemCurrent);
				selectedItem.setQuantity(cartSession.get(vendor).get(item));
				selectedItem.setCost(price.getPrice());
				selectedItemList.add(selectedItem);
			}
			cart.put(vendorCurrent, selectedItemList);
		}

		// Calculate shipping cost and total expenses
		shipping = new HashMap<Vendor, float[]>(); // <Vendor, [ShippingPrice , Total]>

		for (Vendor vendor : cart.keySet()) {

			float shippingPrice = OrderUtils.calculateShipping(vendor, cart.get(vendor));
			float subtotal = 0;
			for (SelectedItem s : cart.get(vendor)) {
				subtotal = subtotal + (s.getCost() * s.getQuantity());
			}

			float[] costs = new float[3];
			costs[0] = shippingPrice;
			costs[1] = subtotal;
			costs[2] = shippingPrice + subtotal;

			shipping.put(vendor, costs);
		}
		
		//necessary just for manage errors
		request.getSession().setAttribute("cartOld", cart);
		request.getSession().setAttribute("shippingOld", shipping);

		Gson gson = new GsonBuilder().create();
		String cartJson = gson.toJson(cart);
		
		/**
		 * JSON structure
		 * 
		 * {Vendor,
		 * 	[item1: {,
		 * 	item2,
		 * 	...
		 * 	],
		 * Shipping,
		 * Total},
		 * 
		 */
		cartJson = "[";
		for(Vendor vendors: cart.keySet()) {
			cartJson+="{\"vendorName\":\""+ vendors.getName()+"\",\"vendorScore\":"+ vendors.getScore()+",\"items\":[";
			      for(SelectedItem items: cart.get(vendors)) {
			        cartJson+="{\"itemName\":\"" + items.getItem().getName()+"\",\"quantity\":"+items.getQuantity()+",\"price\":"+items.getCost()+"},";
			      }
			      cartJson = cartJson.substring(0,cartJson.length()-1);
			      cartJson+= "],";
			      cartJson+="\"shipping\":"+ shipping.get(vendors)[0]+ ",";
			      cartJson+="\"subtotal\":"+ shipping.get(vendors)[1]+ ",";
			      cartJson+="\"total\":"+ shipping.get(vendors)[2]+ "},";
			      }
		cartJson = cartJson.substring(0,cartJson.length()-1);
		cartJson+="]";
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(cartJson);
		
	}

	public void destroy() {
		try {
			DBConnectionProvider.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}