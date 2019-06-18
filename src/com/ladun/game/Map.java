package com.ladun.game;

import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Image;

public class Map {
	
	private static final int SPAWN_COLOR = 0xff68f38a;

	private String name;
	
	private boolean active;
	
	private boolean load;
	private boolean[] collisions;
	private int levelW,levelH;
	private ArrayList<SpawnPoint> spawnPoints = new ArrayList<SpawnPoint>();
	
	public Map(String name) {
		this(name,false);
	}
	
	public Map(String name,boolean active) {
		this.active = active;
		loadLevel(new Image("/" + name + "_collision.png"));
	}
	
	public void update(GameContainer gc, GameManager gm, float dt) {
		
	}
	public void render(GameContainer gc, Renderer r) {
		RenderCollision(r);
	}
	
	private void RenderCollision(Renderer r) {
		int ts = GameManager.TS;
		/*
		for(int y = 0; y < levelH; y++) {
			for(int x = 0; x < levelW;x++) {
				if(!collisions[x + y * levelW])
					r.drawFillRect(x * ts, y*ts, ts, ts, 0xffbd91ed);
			}
		}*/
		for(int y = 0; y < levelH; y++) {
			for(int x = 0; x < levelW;x++) {
				if(collisions[x + y * levelW])
					r.drawRect(x * ts, y*ts, ts, ts, 0xffa6ec90);
			}
		}
	}
	
	private void loadLevel(Image collisionImage) {
		load  = true;
		levelW 				= collisionImage.getW();
		levelH 				= collisionImage.getH();
		collisions 			= new boolean[levelW * levelH];
		
		boolean[] flag		= new boolean[levelW * levelH];

		for (int y = 0; y < levelH; y++) {
			for (int x = 0; x < levelW; x++) {
				if(!flag[x + y * levelW]){
					flag[x + y * levelW] = true;
					if (collisionImage.getP()[x + y * levelW] == SPAWN_COLOR){
						spawnPoints.add(new SpawnPoint(x,y));
					}
					else if (collisionImage.getP()[x + y * levelW] == 0xff000000) {
						collisions[x + y * levelW] = true;
					} else {
						collisions[x + y * levelW] = false;
					}
				}
			}
		}
	}	

	//------------------------------------------------------------------------------------------------------------------------
	
	public boolean getCollision(int x, int y) {
		if (x < 0 || x >= levelW || y < 0 || y >= levelH)
			return true;
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
	
	
	class SpawnPoint{
		private boolean spawned;
		private int x,y;
		
		public SpawnPoint(int x, int y) {
			this.spawned = false;
			this.x = x;
			this.y = y;
		}

		public boolean isSpawned() {
			return spawned;
		}

		public void setSpawned(boolean spawned) {
			this.spawned = spawned;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
	
	
}
