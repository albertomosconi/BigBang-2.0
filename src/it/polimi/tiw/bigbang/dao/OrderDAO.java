package it.polimi.tiw.bigbang.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import it.polimi.tiw.bigbang.beans.OrderInfo;
import it.polimi.tiw.bigbang.beans.OrderedItem;
import it.polimi.tiw.bigbang.beans.SelectedItem;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;

public class OrderDAO {
	private Connection connection;

	public OrderDAO(Connection connection) {
		this.connection = connection;
	}

	public Map<OrderInfo, List<OrderedItem>> findManyByUserID(int userID) throws DatabaseException {

		String orderInfoQuery = "SELECT * FROM order_info WHERE id_user = ? ORDER BY date DESC";
		String orderedItemsQuery = "SELECT * FROM ordered_item WHERE id_order = ? ORDER BY id_item";

		Map<OrderInfo, List<OrderedItem>> userOrders = new LinkedHashMap<>();

		try (PreparedStatement psInfo = connection.prepareStatement(orderInfoQuery)) {
			psInfo.setInt(1, userID);

			try (ResultSet resultInfo = psInfo.executeQuery()) {
				while (resultInfo.next()) {
					OrderInfo orderInfo = new OrderInfo();
					orderInfo.setId(resultInfo.getString("id"));
					orderInfo.setId_user(resultInfo.getInt("id_user"));
					orderInfo.setId_vendor(resultInfo.getInt("id_vendor"));
					orderInfo.setDate(resultInfo.getTimestamp("date"));
					orderInfo.setShipping_cost(resultInfo.getFloat("shipping_cost"));

					List<OrderedItem> orderedItems = new ArrayList<>();
					try (PreparedStatement psItem = connection.prepareStatement(orderedItemsQuery)) {
						psItem.setString(1, orderInfo.getId());
						try (ResultSet resultItem = psItem.executeQuery()) {
							while (resultItem.next()) {
								OrderedItem orderedItem = new OrderedItem();
								orderedItem.setId_item(resultItem.getInt("id_item"));
								orderedItem.setId_order(resultItem.getString("id_order"));
								orderedItem.setQuantity(resultItem.getInt("quantity"));
								orderedItem.setCost(resultItem.getFloat("cost"));
								orderedItems.add(orderedItem);
								orderInfo.setTotal_items_cost(orderInfo.getTotal_items_cost()
										+ (orderedItem.getCost() * orderedItem.getQuantity()));
							}
						}
					}
					userOrders.put(orderInfo, orderedItems);
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException("The server wasn't able to retreive the orders for the current user.");
		}

		return userOrders;
	}

	public void createOrder(int userID, int vendorID, float shipping_cost, List<SelectedItem> items)
			throws DatabaseException {
		String orderInfoQuery = "INSERT INTO order_info (`id`,`id_user`,`id_vendor`,`date`,`shipping_cost`) VALUES (?,?,?,?,?)";
		String orderedItemQuery = "INSERT INTO ordered_item (`id_order`,`id_item`,`quantity`,`cost`) VALUES (?,?,?,?)";

		String uuid = UUID.randomUUID().toString();
		
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		try (PreparedStatement psInfo = connection.prepareStatement(orderInfoQuery)) {
			psInfo.setString(1, uuid);
			psInfo.setInt(2, userID);
			psInfo.setInt(3, vendorID);
			psInfo.setTimestamp(4, new Timestamp(Calendar.getInstance().getTime().getTime()));
			psInfo.setFloat(5, shipping_cost);

			psInfo.executeUpdate();

			try (PreparedStatement psItem = connection.prepareStatement(orderedItemQuery)) {
				for (SelectedItem item : items) {
					psItem.setString(1, uuid);
					psItem.setInt(2, item.getItem().getId());
					psItem.setInt(3, item.getQuantity());
					psItem.setFloat(4, item.getCost());

					psItem.executeUpdate();
				}
			}
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DatabaseException("The server couldn't create a new order.");
		}
	}
}
