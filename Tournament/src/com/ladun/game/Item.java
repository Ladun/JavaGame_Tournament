package com.ladun.game;

public class Item {
	
	public enum Type{ADD_STAT,CH_SKILL}
	
	private Type type;
	private int index;
	
	
	public Item(int index) {
		this.index = index;
	}

	//---------------------------------------------------------------
	
	//---------------------------------------------------------------
	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}
	
}