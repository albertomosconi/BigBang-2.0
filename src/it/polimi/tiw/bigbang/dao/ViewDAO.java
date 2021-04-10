package it.polimi.tiw.bigbang.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public class ViewDAO {
	private Connection con;

	public ViewDAO(Connection connection) {
		this.con = connection;
	}
	
	public void createView(int userId, int itemId) throws SQLException {
		
		String query = "INSERT into view (id_user, id_item, date) VALUES(?,?,?)";
		try(PreparedStatement pstatement = con.prepareStatement(query);){
			pstatement.setInt(1, userId);
			pstatement.setInt(2, itemId);
			pstatement.setTimestamp(3, new Timestamp(Calendar.getInstance().getTime().getTime()));
			pstatement.executeUpdate();
		}
	}

}
