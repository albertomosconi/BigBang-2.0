package it.polimi.tiw.bigbang.beans;

import java.sql.Timestamp;

/* this class is used to save an action of a client:
		after a search for an item, this can be visualized
		whith all its detailed information.
		When a client login in the site, in the home page he can see the last 5 item
		visualized. Because of this all visualized item are saved in the DB whith the moment
		(date and time) of the action */
public class View {
	private int user_id;			//client that visualized the item
	private int item_id;			//witch item is visualized
	private Timestamp date;		//when visualized

	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getItem_id() {
		return item_id;
	}
	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}




}
