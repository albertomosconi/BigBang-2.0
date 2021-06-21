package it.polimi.tiw.bigbang.beans;

/* this class rapresents the shipping policy of a vendor */
public class ShippingRange {

	private int id;				//vendor id
	private int min;			//min amount of money to have a specific shipping cost
	private int max;			//max 					""						""						""
	private float cost;		//the shipping cost for this range

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}



}
