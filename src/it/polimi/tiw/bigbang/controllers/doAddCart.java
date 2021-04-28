package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.bigbang.beans.ErrorMessage;
import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.Price;
import it.polimi.tiw.bigbang.beans.SelectedItem;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.beans.Vendor;
import it.polimi.tiw.bigbang.dao.ItemDAO;
import it.polimi.tiw.bigbang.dao.PriceDAO;
import it.polimi.tiw.bigbang.dao.VendorDAO;
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

	@SuppressWarnings({ "unchecked", "unused" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		ErrorMessage error = new ErrorMessage();

		// Get items added to cart from session
		// Rebuilt cart from vendor id and list of item id
		HashMap<Integer, HashMap<Integer, Integer>> cartSession = new HashMap<Integer, HashMap<Integer, Integer>>();
		try {
			cartSession = (HashMap<Integer, HashMap<Integer, Integer>>) session.getAttribute("cartSession");
		} catch (NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
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

		// controllo che ho veramente passato un intero?

		// Read variables from request
		try {
			vendorAdd = Integer.parseInt(request.getParameter("vendorId"));
			if (vendorAdd == null || vendorAdd < 0) {
				throw new Exception("Missing or wrong credential value vendor");
			}

			itemAdd = Integer.parseInt(request.getParameter("itemId"));
			if (itemAdd == null) {
				throw new Exception("Missing or empty credential value item");
			}

			// Set if item is added from search or home pages
			if (request.getParameter("quantity") != null) {
				quantity = Integer.parseInt(request.getParameter("quantity"));
			}

			// Set if user wish decrement the number of items added to cart
			if (request.getParameter("sub") != null) {
				decrement = true;
			}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value quantity total");
			return;
		}

		// only for
		// check if really exist a correspondence between vendor to item
		PriceDAO priceDAO = new PriceDAO(connection);
		Price price = new Price();
		try {
			price = priceDAO.findPriceBySingleItemId(itemAdd, vendorAdd);
		} catch (SQLException e) {
			request.getSession().setAttribute("cartSession", cartSession);
			response.sendRedirect(getServletContext().getContextPath() + "/cart");
			return;
		}

		// Search if vendor is present yet

		boolean vendorIsPresent = false;
		if (cartSession.containsKey(vendorAdd)) {
			vendorIsPresent = true;
		}


		/*
		 * Vendor selectedVendor = null; for (Vendor v : cart.keySet()) { if (v.getId()
		 * == vendorAdd) { vendorIsPresent = true; selectedVendor = v; } }
		 */

		if (vendorIsPresent) {

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
			if (!isPresent) { // if not present create it
				
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
						cartSession.get(vendorAdd).put(itemAdd,actualQuantity);
					}
				}else { // simply decrement quantity
						actualQuantity = actualQuantity + quantity;
						cartSession.get(vendorAdd).put(itemAdd,actualQuantity);
					}
			}
		}
					/*
					 	
				int actualQuantity = selectedItem.getQuantity();
				if (decrement) {
					if (selectedItem.getQuantity() < 2) { // quantity == 1
						cart.get(selectedVendor).remove(selectedItem);
						if (cart.get(selectedVendor).isEmpty()) {
							cart.remove(selectedVendor);
						}
					} else { // simply decrement quantity
						actualQuantity = actualQuantity - quantity;
						selectedItem.setQuantity(actualQuantity);
					}
				} else { // increment
					actualQuantity = actualQuantity + quantity;
					selectedItem.setQuantity(actualQuantity);
				}
			}
		}
*/
		else { // vendor is not present

			HashMap<Integer,Integer> itemQuantity = new HashMap<Integer,Integer>();
			itemQuantity.put(itemAdd, quantity);
			cartSession.put(vendorAdd, itemQuantity);
			
			/*
			// Collect information about item
			ItemDAO itemDAO = new ItemDAO(connection);
			Item item = new Item();
			try {
				item = itemDAO.findItemsBySingleId(itemAdd);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Collect information about the price of the specific item
			/*
			 * PriceDAO priceDAO = new PriceDAO(connection);
			 * 
			 * Price price = new Price(); try { price =
			 * priceDAO.findPriceBySingleItemId(itemAdd, vendorAdd); } catch (SQLException
			 * e) { e.printStackTrace(); }
			 

			// Collect information about vendor
			VendorDAO vendorDAO = new VendorDAO(connection);
			Vendor vendor = new Vendor();
			try {
				vendor = vendorDAO.findFullBySingleId(vendorAdd);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			SelectedItem selectedItem = new SelectedItem();
			selectedItem.setItem(item);
			selectedItem.setQuantity(quantity);
			selectedItem.setCost(price.getPrice());
			List<SelectedItem> ls = new ArrayList<SelectedItem>();
			ls.add(selectedItem);
			cart.put(selectedVendor, ls);
		}

*/		
}
			
		request.getSession().setAttribute("cartSession", cartSession);
		response.sendRedirect(getServletContext().getContextPath() + "/cart");

	}
	}


