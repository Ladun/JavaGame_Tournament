package com.ladun.game.Item;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageTile;

public class Item {
	public static final int IMAGE_SIZE = 54;
	
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

	private int id;
	private int price;
	private int types;
	private String name;
	
	private int[] options;
	
	public Item() {
		id = -1;
		price = 0;
		types = Type.EMPTY.getValue();
		name = "EMPTY";
		options = new int[1];
	}
	
	public Item(int id, int price,int types,String name,int[] options) {
		this.id = id;
		this.price = price;
		this.types = types;
		this.name = name;
		this.options = options;
	}
	
	public void render(GameContainer gc, Renderer r, int posX, int posY) {
		if(id != -1) {
			r.drawImageTile((ImageTile)gc.getResourceLoader().getImage("items"), posX, posY, id % 16 , id /16 , 0);
		}
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
	
	public int typeCount() {
		int count = 0;
		for(int i = 1; i <= types; i *= 2) {
			if((types & i) != 0) {
				count++;
			}
		}

		return count;
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
		return (types & type.getValue()) != 0;
	}
	
}
