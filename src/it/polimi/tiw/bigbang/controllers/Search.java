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

    request.getSession().removeAttribute("itemSearch");


    // Get the search parameter, so the items asked to be viewed
		String wordSearched = null;
		try {

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
		List<ExtendedItem> extendedItemSearch = new ArrayList<>();
		try {
			compressedItems = itemDAO.findManyByWord(wordSearched);
			extendedItemSearch = extendedItemDAO.findManyItemsDetailsByCompressedItems(compressedItems);

		} catch (DatabaseException e) {
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().println("Database Error");
			return;
    }

    System.out.println(extendedItemSearch.toString());

    //Gson library convers java objects in JSON and send throw the net
    Gson gson = new GsonBuilder().create();

      //WORD searched by the client
    //String keyword = wordSearched;
    String extendedItemsJson = "";

    if (extendedItemSearch != null && !extendedItemSearch.isEmpty()) {
      System.out.println("here in NOT empty");
      //the list of ITEM returned by the search
    extendedItemsJson = "[";
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
  }
  else{
    System.out.println("here in empty");
    extendedItemsJson = "";
  }

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
