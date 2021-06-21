package it.polimi.tiw.bigbang.beans;

import java.util.LinkedHashMap;
import java.util.Map;

/* this class rapresents a generic item whit its standard information
		and the addiction of all selling info: such as witch vendors
		sell it and the price for each one  */

public class ExtendedItem {
	private Item item;													//standard Item info 
	private LinkedHashMap<Vendor,Price> value; 	//vendor whith his price

	public int getId() {
		return item.getId();
	}
	public String getName() {
		return item.getName();
	}
	public String getDescription() {
		return item.getDescription();
	}
	public String getCategory() {
		return item.getCategory();
	}
	public String getPicture() {
		return item.getPicture();
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Map<Vendor, Price> getValue() {
		return value;
	}
	public void setValue(LinkedHashMap<Vendor, Price> value) {
		this.value = value;
	}
}
