package com.ladun.game;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;

public class Map {

	private boolean active;
	
	private boolean load;
	private boolean[] collisions;
	private int levelW,levelH;
	
	public void update(GameContainer gc, GameManager gm, float dt) {
		
	}
	public void render(GameContainer gc, Renderer r) {
		
	}
	
	public void loadLevel() {
		
	}	
	
	//------------------------------------------------------------
	
	public boolean getCollisions(int x, int y) {
		return collisions[y * levelW + x];
	}
	public int getLevelW() {
		return levelW;
	}
	public int getLevelH() {
		return levelH;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
