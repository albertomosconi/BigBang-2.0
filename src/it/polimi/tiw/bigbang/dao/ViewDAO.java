package it.polimi.tiw.bigbang.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import it.polimi.tiw.bigbang.exceptions.DatabaseException;

public class ViewDAO {
	private Connection con;

	public ViewDAO(Connection connection) {
		this.con = connection;
	}
	
	public void createOneViewByUserIdAndItemId(int userId, int itemId) throws DatabaseException {
		
		String query = "INSERT into view (id_user, id_item, date) VALUES(?,?,?)";
		
		try {
			con.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		try(PreparedStatement pstatement = con.prepareStatement(query);){
			pstatement.setInt(1, userId);
			pstatement.setInt(2, itemId);
			pstatement.setTimestamp(3, new Timestamp(Calendar.getInstance().getTime().getTime()));
			pstatement.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				con.rollback();
				con.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DatabaseException("Unable to create a view in DB");
		}
	}

}
