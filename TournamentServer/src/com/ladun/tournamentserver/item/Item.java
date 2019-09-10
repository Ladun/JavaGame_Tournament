package com.ladun.tournamentserver.item;

public class Item {
	
	public enum Type{
		EMPTY( 1), STAT_HEALTH(2), STAT_MANA(4), STAT_DAMAGE(8), CH_SKILL(16);
		
		private final int value;
		private Type(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	
	private int types;
	private int index;
	private String name;
	
	private int[] options;
	
	
	public Item(int index,int types,String name,int[] options) {
		this.index = index;
		this.types = types;
		this.name = name;
		this.options = options;
	}

	//---------------------------------------------------------------
	
	//---------------------------------------------------------------
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
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

	public int getOptionValue(Type type) {
		int index = 0;
		for(int i = 1; i < type.value; i *= 2) {
			if((types & type.value) == type.value) {
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