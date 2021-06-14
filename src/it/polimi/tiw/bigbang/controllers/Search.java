package it.polimi.tiw.bigbang.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.bigbang.beans.ErrorMessage;
import it.polimi.tiw.bigbang.beans.ExtendedItem;
import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.Price;
import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.beans.Vendor;
import it.polimi.tiw.bigbang.dao.ExtendedItemDAO;
import it.polimi.tiw.bigbang.dao.ItemDAO;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;
import it.polimi.tiw.bigbang.utils.DBConnectionProvider;

@MultipartConfig
public class Search extends HttpServlet{
  private static final long serialVersionUID = 1L;
	private ServletContext servletContext;
    private Connection connection;

    public void init() throws ServletException {
		servletContext = getServletContext();
		connection = DBConnectionProvider.getConnection(servletContext);
}

@SuppressWarnings("unchecked")
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  HttpSession session = request.getSession();

  //get the user by the session
  User user = (User) session.getAttribute("user");

  //create the variable to store a possible error
  ErrorMessage errorMessage;

  //get the lists of item searched yet or viewed yet by the session, if there are
  List<ExtendedItem> viewItem = (List<ExtendedItem>) session.getAttribute("itemViewed");
  List<ExtendedItem> extendedItemSearch = (List<ExtendedItem>) session.getAttribute("itemSearch");

  //as default remove all viewed items because is a new search
  /* so if is set, get the boolean from the session that
  say if the views have to be cleaned or not */
  boolean clearViewedItemList = true;
  if(session.getAttribute("clearViewItemList")!=null) {
    clearViewedItemList = (boolean)session.getAttribute("clearViewItemList");
  }

  /* now check if is a new search
    so remove all the old viewed Items */
  if (clearViewedItemList) {
    //each time I do a new search, remove all old items searched and visualized
    request.getSession().removeAttribute("itemSearch");
    request.getSession().removeAttribute("itemViewed");
    viewItem = null;
    }

    // Get the search parameter, so the items asked to be viewed
		String wordSearched = null;
		try {
			System.out.println(request.toString());
			System.out.println(request.getParameter("keyword"));

			wordSearched = request.getParameter("keyword");

			if (wordSearched == null || wordSearched.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}
		} catch (Exception e) {
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Input Error");
			return;

		}

    ItemDAO itemDAO = new ItemDAO(connection);
		ExtendedItemDAO extendedItemDAO = new ExtendedItemDAO(connection);
		List<Item> compressedItems = new ArrayList<>();
		extendedItemSearch = new ArrayList<>();
		try {
			compressedItems = itemDAO.findManyByWord(wordSearched);
			extendedItemSearch = extendedItemDAO.findManyItemsDetailsByCompressedItems(compressedItems);

		} catch (DatabaseException e) {
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().println("Database Error");
			return;
    }

    if (viewItem == null) {
			//no item visualized yet
			viewItem = new ArrayList<>();
		}

		//not needed anymore
		session.removeAttribute("clearViewItemList");

    //get only the id of the items viewed(if there is)
	ArrayList<Integer> idItemViewed = new ArrayList<>();
    if (viewItem != null){

		for (ExtendedItem item : viewItem) {
			idItemViewed.add(item.getId());
		}
  }

  //Gson library convers java objects in JSON and send throw the net
  Gson gson = new GsonBuilder().create();

    //WORD searched by the client
  //String keyword = wordSearched;

    //the list of ITEM returned by the search
  String extendedItemsJson = "[";
  for (ExtendedItem extendedItem : extendedItemSearch) {
    extendedItemsJson += "{\"id\":"+extendedItem.getId()+",\"name\":\""+extendedItem.getName()+"\",\"description\":\""+extendedItem.getDescription().replace("\"", "\\\"")+"\",\"category\":\""+extendedItem.getCategory()+"\",\"picture\":\""+extendedItem.getPicture()+"\",";
    String vendorString = "[";
    String priceString = "[";
    for (Map.Entry<Vendor, Price> entry : extendedItem.getValue().entrySet()) {
      Vendor v = entry.getKey();
      Price p = entry.getValue();
      vendorString += gson.toJson(v) + ",";
      priceString += gson.toJson(p) + ",";
    }
    vendorString = vendorString.substring(0, vendorString.length()-1);
    priceString = priceString.substring(0, priceString.length()-1);
    vendorString+="]";
    priceString+="]";
    extendedItemsJson += "\"vendorList\":" + vendorString + ",";
    extendedItemsJson += "\"priceList\":" + priceString + "},";
  }
  extendedItemsJson = extendedItemsJson.substring(0, extendedItemsJson.length()-1);
  extendedItemsJson+="]";
//  String extendedItemString = gson.toJson(extendedItemsJson);

    //list of item ID of wich have to be visualized
  //String idViewed = "";
  //idViewed = gson.toJson(idItemViewed);

    //put all 3 in an array list of string and write it in the response
  //ArrayList<String> JSONRequest = new ArrayList();
  //JSONRequest.add(keyword);
  //JSONRequest.add(extendedItemString);
  //JSONRequest.add(idViewed);

  response.setStatus(HttpServletResponse.SC_OK);
  response.setContentType("application/json");
  response.setCharacterEncoding("UTF-8");
  response.getWriter().println(extendedItemsJson);


}

public void destroy() {
  try {
    DBConnectionProvider.closeConnection(connection);
  } catch (SQLException e) {
    e.printStackTrace();
  }
}

}
