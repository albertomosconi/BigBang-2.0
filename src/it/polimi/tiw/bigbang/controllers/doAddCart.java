package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.bigbang.beans.ErrorMessage;
import it.polimi.tiw.bigbang.beans.Price;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.dao.PriceDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;

public class doAddCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	public void init() throws ServletException {

		ServletContext servletContext = getServletContext();
		connection = DBConnectionProvider.getConnection(servletContext);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * ERRORI GESTITI: 
	 * -cartSession non presente nella sessione 
	 * -vendor null or vendor id <0 
	 * -item null or item id<0
	 * -non esiste una corrispondenza tra vendor e item 
	 * -decrementare l'item di un venditore non presente 
	 * -decremenetare l'item non presente 
	 * -ho veramente passato un intero
	 * -quantity <=0
	 * -non passo a sub il valore corretto
	 * 
	 * ERRORI DA GESTIRE:
	 * -se metto sub dove dovrei invece incrementare?? 
	 * 
	 */

	@SuppressWarnings({ "unchecked", "unused"})
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get active user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// space for errors
		ErrorMessage errorMessage = null;

		// Get items added to cart from session
		HashMap<Integer, HashMap<Integer, Integer>> cartSession = new HashMap<Integer, HashMap<Integer, Integer>>();
		try {
			cartSession = (HashMap<Integer, HashMap<Integer, Integer>>) session.getAttribute("cartSession");
		} catch (NumberFormatException | NullPointerException e) {

			errorMessage= new ErrorMessage("Session Error", "resources not found");
			request.getSession().setAttribute("error", errorMessage);
			response.sendRedirect(getServletContext().getContextPath() + "/cart");
			return;
		}

		/*
		 * // Get items added to cart HashMap<Vendor, List<SelectedItem>> cart = new
		 * HashMap<Vendor, List<SelectedItem>>();
		 * 
		 * try { cart = (HashMap<Vendor, List<SelectedItem>>)
		 * session.getAttribute("cart"); } catch (NumberFormatException |
		 * NullPointerException e) {
		 * response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
		 * return; }
		 */

		Integer vendorAdd = 0;
		String sub = null;
		Integer itemAdd = 0;
		Integer quantity = 1;
		boolean decrement = false;

	
		// Read variables from request

		String string_vendor = request.getParameter("vendorId");
		if (string_vendor != null && !string_vendor.equals("")) {
			try {
				vendorAdd = Integer.parseInt(string_vendor);
			} catch (NumberFormatException e) {
				errorMessage = new ErrorMessage("Vendor Parameter Error", "not corret format of credential value");
				request.getSession().setAttribute("error", errorMessage);
				response.sendRedirect(getServletContext().getContextPath() + "/cart");
				return;
			}
		} else {
			errorMessage = new ErrorMessage("Vendor Parameter Error", "not corret format of credential value");
			request.getSession().setAttribute("error", errorMessage);
			response.sendRedirect(getServletContext().getContextPath() + "/cart");
			return;
		}
		if (vendorAdd == null || vendorAdd < 0) {

			errorMessage = new ErrorMessage("Vendor Parameter Error", "missing or empty credential value");
			request.getSession().setAttribute("error", errorMessage);
			response.sendRedirect(getServletContext().getContextPath() + "/cart");
			return;
		}

		String string_item = request.getParameter("itemId");
		if (string_item != null && !string_item.equals("")) {
			try {
				itemAdd = Integer.parseInt(string_item);

			} catch (NumberFormatException e) {
				errorMessage = new ErrorMessage("Item Parameter Error", "not corret format of credential value");
				request.getSession().setAttribute("error", errorMessage);
				response.sendRedirect(getServletContext().getContextPath() + "/cart");
				return;
			}
		} else {
			errorMessage = new ErrorMessage("Item Parameter Error", "not corret format of credential value");
			request.getSession().setAttribute("error", errorMessage);
			response.sendRedirect(getServletContext().getContextPath() + "/cart");
			return;
		}
		if (itemAdd == null || itemAdd < 0) {

			errorMessage = new ErrorMessage("Item Parameter Error", "missing or empty credential value");
			request.getSession().setAttribute("error", errorMessage);
			response.sendRedirect(getServletContext().getContextPath() + "/cart");
			return;
		}

		// Set if item is added from search or home pages
		String string_quantity = request.getParameter("quantity");
		if (string_quantity != null && !string_quantity.equals("")) {
			try {
				quantity = Integer.parseInt(string_quantity);
			} catch (NumberFormatException e) {
				errorMessage = new ErrorMessage("Quantity Parameter Error", "not corret format of credential value");
				request.getSession().setAttribute("error", errorMessage);
				response.sendRedirect(getServletContext().getContextPath() + "/cart");
				return;
			}
			/*
		} else {
			errorMessage = new ErrorMessage("Quantity Parameter Error 2 ", "not corret format of credential value");
			request.getSession().setAttribute("error", errorMessage);
			response.sendRedirect(getServletContext().getContextPath() + "/cart");
			return;
			*/
		}
			//only positive values
		if(quantity<1) {
			errorMessage= new ErrorMessage("Quantity Parameter Error", "negative quantity");
			request.getSession().setAttribute("error", errorMessage);
			response.sendRedirect(getServletContext().getContextPath() + "/cart");
			return;
		}
		
		// Set if user wish decrement the number of items added to cart
		if (request.getParameter("sub") != null && request.getParameter("sub").equals("true")) {
			decrement = true;
		}

		// check if really exist a correspondence between vendor to item
		PriceDAO priceDAO = new PriceDAO(connection);
		Price price = new Price();
		try {
			price = priceDAO.findOneByItemIdAndVendorId(itemAdd, vendorAdd);
		} catch (DatabaseException e) {
			errorMessage = new ErrorMessage("Database Error", e.getBody());
			request.getSession().setAttribute("cartSession", cartSession);
			response.sendRedirect(getServletContext().getContextPath() + "/cart");
			return;
		}

		// Search if vendor is present yet
		boolean vendorIsPresent = false;
		if (cartSession.containsKey(vendorAdd)) {
			vendorIsPresent = true;
		}
		
		//If vendor is not present and decrement --> error
		if(!vendorIsPresent && decrement){
			errorMessage = new ErrorMessage("Request Error", "vendor not present in cart");
			request.getSession().setAttribute("cartSession", cartSession);
			response.sendRedirect(getServletContext().getContextPath() + "/cart");
			return;
		}
		/*
		 * Vendor selectedVendor = null; for (Vendor v : cart.keySet()) { if (v.getId()
		 * == vendorAdd) { vendorIsPresent = true; selectedVendor = v; } }
		 */

		if (!vendorIsPresent && !decrement) //vendor is not present
		{
			HashMap<Integer, Integer> itemQuantity = new HashMap<Integer, Integer>();
			itemQuantity.put(itemAdd, quantity);
			cartSession.put(vendorAdd, itemQuantity);

		} else { // vendor is present

			// Search if item is already present
			boolean isPresent = false;
			if (cartSession.get(vendorAdd).containsKey(itemAdd)) {
				isPresent = true;
			}

			/*
			 * SelectedItem selectedItem = null; for (SelectedItem s :
			 * cart.get(selectedVendor)) { if (s.getItem().getId() == itemAdd) { isPresent =
			 * true; selectedItem = s; } }
			 */
			
			//item is not present and decrement
			if (!isPresent && decrement) {
				errorMessage = new ErrorMessage("Request Error", "item not present in cart");
				request.getSession().setAttribute("cartSession", cartSession);
				response.sendRedirect(getServletContext().getContextPath() + "/cart");
				return;	
			}
			
			if (!isPresent && !decrement) { // if not present create it

				cartSession.get(vendorAdd).put(itemAdd, quantity);
				/*
				 * // Collect information about item ItemDAO itemDAO = new ItemDAO(connection);
				 * Item item = new Item(); try { item = itemDAO.findItemsBySingleId(itemAdd); }
				 * catch (SQLException e) { e.printStackTrace(); }
				 * 
				 * // Collect information about the price of the specific item /* PriceDAO
				 * priceDAO = new PriceDAO(connection);
				 * 
				 * Price price = new Price(); try { price =
				 * priceDAO.findPriceBySingleItemId(itemAdd, vendorAdd); } catch (SQLException
				 * e) { e.printStackTrace(); }
				 * 
				 * selectedItem = new SelectedItem(); selectedItem.setItem(item);
				 * selectedItem.setQuantity(quantity); selectedItem.setCost(price.getPrice());
				 * cart.get(selectedVendor).add(selectedItem);
				 */
			} else { // item is already present
				int actualQuantity = cartSession.get(vendorAdd).get(itemAdd);

				if (decrement) {
					if (actualQuantity < 2) { // quantity == 1
						cartSession.get(vendorAdd).remove(itemAdd);
						if (cartSession.get(vendorAdd).isEmpty()) {
							cartSession.remove(vendorAdd);
						}
					} else { // simply decrement quantity
						actualQuantity = actualQuantity - quantity;
						cartSession.get(vendorAdd).put(itemAdd, actualQuantity);
					}
				} else { // simply decrement quantity
					actualQuantity = actualQuantity + quantity;
					cartSession.get(vendorAdd).put(itemAdd, actualQuantity);
				}
			}
		}
		/*
		 * 
		 * int actualQuantity = selectedItem.getQuantity(); if (decrement) { if
		 * (selectedItem.getQuantity() < 2) { // quantity == 1
		 * cart.get(selectedVendor).remove(selectedItem); if
		 * (cart.get(selectedVendor).isEmpty()) { cart.remove(selectedVendor); } } else
		 * { // simply decrement quantity actualQuantity = actualQuantity - quantity;
		 * selectedItem.setQuantity(actualQuantity); } } else { // increment
		 * actualQuantity = actualQuantity + quantity;
		 * selectedItem.setQuantity(actualQuantity); } } }
		 */
		/*
		 * // Collect information about item ItemDAO itemDAO = new ItemDAO(connection);
		 * Item item = new Item(); try { item = itemDAO.findItemsBySingleId(itemAdd); }
		 * catch (SQLException e) { e.printStackTrace(); }
		 * 
		 * // Collect information about the price of the specific item /* PriceDAO
		 * priceDAO = new PriceDAO(connection);
		 * 
		 * Price price = new Price(); try { price =
		 * priceDAO.findPriceBySingleItemId(itemAdd, vendorAdd); } catch (SQLException
		 * e) { e.printStackTrace(); }
		 * 
		 * 
		 * // Collect information about vendor VendorDAO vendorDAO = new
		 * VendorDAO(connection); Vendor vendor = new Vendor(); try { vendor =
		 * vendorDAO.findFullBySingleId(vendorAdd); } catch (SQLException e) {
		 * e.printStackTrace(); }
		 * 
		 * SelectedItem selectedItem = new SelectedItem(); selectedItem.setItem(item);
		 * selectedItem.setQuantity(quantity); selectedItem.setCost(price.getPrice());
		 * List<SelectedItem> ls = new ArrayList<SelectedItem>(); ls.add(selectedItem);
		 * cart.put(selectedVendor, ls); }
		 * 
		 */
		request.getSession().setAttribute("cartSession", cartSession);
		response.sendRedirect(getServletContext().getContextPath() + "/cart");

	}
}
