package it.polimi.tiw.bigbang.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.Price;

public class PriceDAO {
	private Connection con;

	public PriceDAO(Connection connection) {
		this.con = connection;
	}

	public ArrayList<Price> findPriceByItemId(ArrayList<Integer> items) throws SQLException{
		String query = "SELECT id_item, id_vendor, price FROM price WHERE id_item = ? ";
		ArrayList<Price> prices = new ArrayList<Price>();
		for (Integer i: items) {
			try (PreparedStatement pstatement = con.prepareStatement(query);) {
				pstatement.setInt(1, i);
				try (ResultSet result = pstatement.executeQuery();) {
					if (!result.isBeforeFirst())
						return null;
				else {
					result.next();
					Price price = new Price();
					price.setIdItem(result.getInt("id_item"));
					price.setIdVendor(result.getInt("id_vendor"));
					price.setPrice(result.getInt("price"));
					prices.add(price);
				}

			}
		}
	}
	return prices;
	}


public ArrayList<Price> findLowerPriceByItemId(ArrayList<Integer> items) throws SQLException{
	String query = "SELECT P1.id_item, P1.id_vendor, P1.price \n"
			+ "FROM price as P1 \n"
			+ "WHERE P1.id_item = ? AND P1.price = (select min(price)\n"
			+ "				from price AS P2\n"
			+ "                where P1.id_item = P2.id_item) ";
	ArrayList<Price> prices = new ArrayList<Price>();
	for (Integer i: items) {
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, i);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.isBeforeFirst()) {
					
				result.next();
				Price price = new Price();
				price.setIdItem(result.getInt("id_item"));
				price.setIdVendor(result.getInt("id_vendor"));
				price.setPrice(result.getInt("price"));
				prices.add(price);
			}

		}
	}
}
return prices;
}


public Map<Integer, List<Price>> findManyByItemIDs(List<Integer> itemIDs) throws SQLException {
	  String query = "SELECT id_item, id_vendor, price FROM price WHERE id_item = ?";
	  Map<Integer, List<Price>> itemIDPriceListMap = new HashMap<>();
	  
	  for (int itemID : itemIDs) {
	   List<Price> currentItemPrices = new ArrayList<>();
	   try (PreparedStatement pstatement = con.prepareStatement(query)) {
	    pstatement.setInt(1, itemID);
	    try (ResultSet result = pstatement.executeQuery()) {
	     if (result.isBeforeFirst()) {
	      while (result.next()) {
	       Price price = new Price();
	       price.setIdItem(result.getInt("id_item"));
	       price.setIdVendor(result.getInt("id_vendor"));
	       price.setPrice(result.getFloat("price"));
	       currentItemPrices.add(price);
	      }
	     }
	    }
	   }
	   
	   
	   
	   Comparator<Price> comparePrice = new Comparator<Price>() {
	    @Override
	    public int compare(Price o1, Price o2) {
	     return (int)(o1.getPrice() - o2.getPrice());
	    }
	   };
	   currentItemPrices.sort(comparePrice);
	   itemIDPriceListMap.put(itemID, currentItemPrices);
	  }
	  
	  return itemIDPriceListMap;
	 }
}





