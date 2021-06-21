package it.polimi.tiw.bigbang.beans;

/* this class track the association between an item, and the price for
		a specific vendor */
public class Price {
	private float price;
	private int idItem;
	private int idVendor;

	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getIdItem() {
		return idItem;
	}
	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}
	public int getIdVendor() {
		return idVendor;
	}
	public void setIdVendor(int idVendor) {
		this.idVendor = idVendor;
	}
}
