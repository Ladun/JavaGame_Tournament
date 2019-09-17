package com.ladun.game.Item;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.objects.UI.Button;

public class ShopSlot {
	public static final int WIDTH = 200;
	public static final int HEIGHT = 125;
	
	private int posX,posY;
	private Button parchaseButton;
	private boolean active;
	
	public ShopSlot(Item item,int posX,int posY) {
		setItem(item, posX, posY);
	}
	
	public void update(GameContainer gc, GameManager gm) {
		
	}
	public void render(GameContainer gc, Renderer r) {
		r.setzDepth(Renderer.LAYER_UI);
		// background Render
		r.drawFillRect(posX , posY, WIDTH, HEIGHT, 0,0xffffffff);
		// item image Render
		r.drawFillRect(posX  + 20, posY + 20, 70, 70, 0,0xff000000);
		// Price Render
		// Buy Button Render
	}
	public void setItem(Item item,int posX, int posY) {

		active = true;
		this.posX = posX;
		this.posY = posY;
		
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}

