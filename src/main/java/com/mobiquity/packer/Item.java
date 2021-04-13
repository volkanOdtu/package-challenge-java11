package com.mobiquity.packer;

public class Item {
	private int index;
	private double weight ,price;
	  
	public Item(int index ,double weight ,double price) {
		  this.index = index;
		  this.weight = weight;
		  this.price = price;
	 }
	 
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) { this.index = index; }

	public double getWeight() { return weight; }

	public void setWeight(double weight) { this.weight = weight; }

	public double getPrice() { return price; }

	public void setPrice(double price) { this.price = price; }
}
