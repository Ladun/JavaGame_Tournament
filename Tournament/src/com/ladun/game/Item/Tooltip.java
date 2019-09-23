package com.ladun.game.Item;

import java.awt.Color;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;

public class Tooltip {
	
	public class Content{
		public String str;
		public int color;
		
		public Content(String str, int color) {
			this.str =str;
			this.color =color;
		}
	}
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

		r.drawString(content[0].str, posX+ 10, posY -5, 20, content[0].color);
		for(int i = 1; i < content.length; i++) {
			r.drawString(content[i].str, posX+ 10, posY + i * 18 +15,20, content[i].color);
		}
	}

	public void setting(Content[] content, int id) {
		this.content = content;
		contentID =id;
	}

	public int getContentID() {
		return contentID;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}