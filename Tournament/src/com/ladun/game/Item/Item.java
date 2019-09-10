package com.ladun.game.Item;

public class Item {
	
	public enum Type{EMPTY, STAT_HEALTH, STAT_MANA, STAT_DAMAGE, CH_SKILL}
	
	private Type type;
	private int index;
	
	
	public Item(Type type,int index) {
		this.type = type;
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
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
}
