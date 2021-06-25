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
	 * -se metto sub dove dovrei invece incrementare --> decremento se item e vendor presetni
	 * 
	 */

	@SuppressWarnings({ "unchecked", "unused"})
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get active user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Get items added to cart from session
		HashMap<Integer, HashMap<Integer, Integer>> cartSession = new HashMap<Integer, HashMap<Integer, Integer>>();
		try {
			cartSession = (HashMap<Integer, HashMap<Integer, Integer>>) session.getAttribute("cartSession");
		} catch (NumberFormatException | NullPointerException e) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Server error: could not add items to cart");
			return;
		}

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
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Vendor Parameter Error: not corret format of credential value");
				return;
				
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Vendor Parameter Error: not corret format of credential value");
			return;
		}
		if (vendorAdd == null || vendorAdd < 0) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Vendor Parameter Error: missing or empty credential value");
			return;
		}

		String string_item = request.getParameter("itemId");
		if (string_item != null && !string_item.equals("")) {
			try {
				itemAdd = Integer.parseInt(string_item);

			} catch (NumberFormatException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Item Parameter Error: not corret format of credential value");
				return;
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Item Parameter Error: not corret format of credential value");
			return;
		}
		if (itemAdd == null || itemAdd < 0) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Item Parameter Error: missing or empty credential value");
			return;
		}

		// Set if item is added from search or home pages
		String string_quantity = request.getParameter("quantity");
		if (string_quantity != null && !string_quantity.equals("")) {
			try {
				quantity = Integer.parseInt(string_quantity);
			} catch (NumberFormatException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Quantity Parameter Error: not corret format of credential value");
				return;
			}
	
		}
			//only positive values
		if(quantity<1) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Quantity Parameter Error: negative quantity ");
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
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Database Error: " + e.getBody());
			return;
		}

		if(price == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Server error: vendor or item not found");
			return;	
		}
		
		
		// Search if vendor is present yet
		boolean vendorIsPresent = false;
		if (cartSession.containsKey(vendorAdd)) {
			vendorIsPresent = true;
		}
		
		//If vendor is not present and decrement --> error
		if(!vendorIsPresent && decrement){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Vendor Parameter Error: vendor not present in cart - decrementation error");
			return;
		}
		
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

			//item is not present and decrement
			if (!isPresent && decrement) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Item Parameter Error: item not present in cart - decrementation error");
				return;
			}
			
			if (!isPresent && !decrement) { // if not present create it

				cartSession.get(vendorAdd).put(itemAdd, quantity);
				
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
		
		request.getSession().setAttribute("cartSession", cartSession);
		response.sendRedirect(getServletContext().getContextPath() + "/cart");

	}
}
