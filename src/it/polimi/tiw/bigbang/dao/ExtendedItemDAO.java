package it.polimi.tiw.bigbang.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;

import it.polimi.tiw.bigbang.beans.ExtendedItem;
import it.polimi.tiw.bigbang.beans.Item;
import it.polimi.tiw.bigbang.beans.Price;
import it.polimi.tiw.bigbang.beans.Vendor;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;

public class ExtendedItemDAO {
	private Connection connection;

	public ExtendedItemDAO(Connection connection) {
		this.connection = connection;
	}

	public List<ExtendedItem> findManyItemsDetailsByCompressedItems(List<Item> items) {

		List<ExtendedItem> extendedItems = new ArrayList<>();
		// extract a list of the IDs of the items
		Map<Integer, Item> itemIdItemMap = new LinkedHashMap<>();
		List<Integer> itemIDs = new ArrayList<>();
		for (Item item : items) {
			itemIDs.add(item.getId());
			itemIdItemMap.put(item.getId(), item);
		}
		// for each item obtain the list of prices it's sold at
		Map<Integer, List<Price>> itemPrices = new LinkedHashMap<>();
		PriceDAO priceDAO = new PriceDAO(connection);
		try {
			itemPrices = priceDAO.findManyForEachItemId(itemIDs);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		// for each item, for each price, find all the details of the vendor
		for (int itemID : itemPrices.keySet()) {
			// extract all vendor IDs for the current item
			List<Integer> itemVendorIDs = new ArrayList<>();
			Map<Integer, Price> vendorIdPriceMap = new HashMap<>();
			for (Price price : itemPrices.get(itemID)) {
				itemVendorIDs.add(price.getIdVendor());
				vendorIdPriceMap.put(price.getIdVendor(), price);
			}
			// fetch the vendor details from the list of IDs
			List<Vendor> itemVendors = new ArrayList<>();
			VendorDAO vendorDAO = new VendorDAO(connection);
			try {
				itemVendors = vendorDAO.findManyByVendorsId(itemVendorIDs);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}

			ExtendedItem extendedItem = new ExtendedItem();
			extendedItem.setItem(itemIdItemMap.get(itemID));
			extendedItem.setValue(new LinkedHashMap<Vendor, Price>());
			for (Vendor vendor : itemVendors) {
				extendedItem.getValue().put(vendor, vendorIdPriceMap.get(vendor.getId()));
			}
			extendedItems.add(extendedItem);
		}

		return extendedItems;
	}
}
