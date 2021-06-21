package it.polimi.tiw.bigbang.beans;

import java.util.List;

/* this class rapresents a generic agency that sell
		one or more items in the site */
public class Vendor {
	private int id;
	private String name;
	private int score;										//agency valutation from 0 to 5 
	private float free_limit;							//amount of money to spend for have a free shipping
	private List<ShippingRange> ranges;		//all shipping cost

	public List<ShippingRange> getRanges() {
		return ranges;
	}
	public void setRanges(List<ShippingRange> ranges) {
		this.ranges = ranges;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public float getFree_limit() {
		return free_limit;
	}
	public void setFree_limit(float free_limit) {
		this.free_limit = free_limit;
	}

}
