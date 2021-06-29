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

/* this Servlet is used when the client search an item typing an input in the field
    the items returned are all the ones that have that word (or words) in the description, category or name */

//used to indicate that the servlet on which it is declared expects requests to be made using the multipart/form-data MIME type
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

    request.getSession().removeAttribute("itemSearch");


      // Get the search parameter, so the items asked to be viewed
		String wordSearched = null;
		try {

			wordSearched = request.getParameter("keyword");
          //check the validity
			if (wordSearched == null || wordSearched.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}
		} catch (Exception e) {

      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);   //400
			response.getWriter().println("Input Error");
			return;

		}

    ItemDAO itemDAO = new ItemDAO(connection);
		ExtendedItemDAO extendedItemDAO = new ExtendedItemDAO(connection);
		List<Item> compressedItems = new ArrayList<>();
		List<ExtendedItem> extendedItemSearch = new ArrayList<>();
		try {
          //find the items with the wordSearched in the name, description or category
			compressedItems = itemDAO.findManyByWord(wordSearched);
          //create the complete item with even the vendor and prices information
			extendedItemSearch = extendedItemDAO.findManyItemsDetailsByCompressedItems(compressedItems);

		} catch (DatabaseException e) {

      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);   //400
      response.getWriter().println("Database Error");
			return;
    }

      //convert the java object (Bean) into a String with Gson
    //Gson library convers java objects in JSON and send throw the net
    Gson gson = new GsonBuilder().create();

    String extendedItemsJson = "";

    if (extendedItemSearch != null && !extendedItemSearch.isEmpty())  {
      //the list of ITEM returned by the search
      System.out.println("1");
    extendedItemsJson = "[";
    for (ExtendedItem extendedItem : extendedItemSearch) {
      System.out.println("2");
      if (!extendedItem.getValue().keySet().isEmpty() || !extendedItem.getValue().values().isEmpty()) {
System.out.println("3");
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
else{
  System.out.println("4");
  extendedItemsJson += "x"; //this will be removed by row 136
    System.out.println(extendedItemsJson);
}
}
extendedItemsJson = extendedItemsJson.substring(0, extendedItemsJson.length()-1);
extendedItemsJson+="]";
}
  else{
    //if the search returned 0 items
    extendedItemsJson = "[]";
  }
System.out.println(extendedItemsJson);
      //the search went successfully
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
