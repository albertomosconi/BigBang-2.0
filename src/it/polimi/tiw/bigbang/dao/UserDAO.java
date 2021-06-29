package it.polimi.tiw.bigbang.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.bigbang.beans.User;
import it.polimi.tiw.bigbang.exceptions.DatabaseException;

public class UserDAO {
	private Connection con;

	public UserDAO(Connection connection) {
		this.con = connection;
	}

	public User checkCredentials(String email, String pwd) throws DatabaseException {
		String query = "SELECT  id, name, surname, email, address FROM user  WHERE email = ? AND password =?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, email);
			pstatement.setString(2, pwd);

			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					User user = new User();
					user.setId(result.getInt("id"));
					user.setName(result.getString("name"));
					user.setSurname(result.getString("surname"));
					user.setEmail(result.getString("email"));
					user.setAddress(result.getString("address"));
					return user;
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException("could not check user credentials.");
		}
	}

	public void createUser(String name, String surname, String email, String pwd,String address) throws DatabaseException {

		String query = "INSERT INTO user (name,surname,email,password, address) VALUES (?,?,?,?,?)";
		
		try {
			con.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		try (PreparedStatement preparedStatement = con.prepareStatement(query);) {
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, surname);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, pwd);
			preparedStatement.setString(5, address);
			preparedStatement.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				con.rollback();
				con.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DatabaseException("could not create a new user!");
		}

	}

}
