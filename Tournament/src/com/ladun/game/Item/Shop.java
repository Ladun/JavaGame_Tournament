package com.ladun.game.Item;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;

public class Shop {
	private boolean active;
	
	public Shop() {
		active = true;
	}
	
	public void update(GameContainer gc, GameManager gm) {
		
	}
	public void render(GameContainer gc, Renderer r) {
		
	}
	//---------------------------------------------

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
