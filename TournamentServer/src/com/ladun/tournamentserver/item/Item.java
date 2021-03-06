package com.ladun.tournamentserver.item;

public class Item {
	
	public enum Type{
		EMPTY( 1), CH_SKILL(2),STAT_HEALTH(4), STAT_MANA(8), STAT_DAMAGE(16), STAT_MOVESPEED(32), STAT_DEFENCE(64),STAT_HEALTHREGE(128), STAT_MANAREGE(256);
		
		
		private final int value;
		private Type(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	
	private int types;
	private int price;
	private int id;
	private String name;
	
	private int[] options;
	
	
	public Item(int id, int price, int types,String name,int[] options) {
		this.id = id;
		this.price = price;
		this.types = types;
		this.name = name;
		this.options = options;
	}

	//---------------------------------------------------------------
	
	//---------------------------------------------------------------
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	
	public int getTypes() {
		return types;
	}

	public void setTypes(int types) {
		this.types = types;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getOptions() {
		return options;
	}

	public void setOptions(int[] options) {
		this.options = options;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getOptionValue(Type type) {
		int index = 0;
		for(int i = 2; i < type.value; i *= 2) {
			if((types & i) != 0) {
				index++;
			}
		}
		if(index < options.length)
			return options[index];
		return 0;
	}
	
	public boolean hasType(Type type) {
		return (types & type.getValue()) == type.getValue();
	}
	
}
