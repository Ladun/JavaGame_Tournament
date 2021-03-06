package com.ladun.game.Item;

import java.awt.Color;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;

public class Tooltip {
	
	public enum Type {State, Shop};
	
	public class Content{
		public String str;
		public int color;
		
		public Content(String str, int color) {
			this.str =str;
			this.color =color;
		}
	}
	private Type type;
	private boolean active;
	private Content[] content;
	private int posX,posY;
	private int contentID;
	
	public void update(GameContainer gc, GameManager gm) {
		posX = gc.getInput().getMouseX() + 20;
		posY = gc.getInput().getMouseY() + 20;
	}
	
	public void render(GameContainer gc, Renderer r) {
		
		if(content == null || content.length == 0)
			return;
		r.drawFillRect(posX, posY,  200, 20 * content.length  + 30, 0, 0xffaaaaaa);

		r.drawString(content[0].str, posX + 5, posY + 5, 20, content[0].color);
		for(int i = 1; i < content.length; i++) {
			r.drawString(content[i].str, posX+ 15, posY + i * 20 +15,20, content[i].color);
		}
	}

	public void setting(Content[] content, int id,Type type) {
		this.content = content;
		contentID =id;
		this.type = type;
	}

	public int getContentID() {
		return contentID;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
