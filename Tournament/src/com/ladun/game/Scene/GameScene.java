package com.ladun.game.Scene;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.Camera;
import com.ladun.game.GameManager;
import com.ladun.game.Map;
import com.ladun.game.Physics;
import com.ladun.game.objects.Player;

public class GameScene extends AbstractScene{

	private Map[] maps = new Map[1];
	private int currentMapIndex = 0;
	
	
	private Camera 		camera;
	
	
	@Override
	public boolean init(GameManager gm,boolean active) {
		// TODO Auto-generated method stub
		this.active = active;
		
		this.name = "InGame";		
		//this.objects.add(new Player(1,1,this));
		this.camera = new Camera(gm,"Player");
		//this.addObject(new TempObject(this));
		
		this.maps[0] = new Map("map1",true);
		return true;
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		maps[currentMapIndex].update(gc, gm, dt);

		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).isActive())
				objects.get(i).update(gc, gm, dt);
		}

		camera.update(gc, gm, dt);
		Physics.update();
	}
	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		camera.render(r);
		maps[currentMapIndex].render(gc, r);
		
		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).isActive())
				objects.get(i).render(gc, r);
		}
		
		
		r.setzDepth(Renderer.LAYER_UI);
		r.setCamX(0);
		r.setCamY(0);
		r.drawImage(((ImageTile)gc.getImageLoader().getImage("bar")).getTileImage(0, 0), gc.getWidth() / 2 - 334 / 2, gc.getHeight() - 115, 0,0,334,13);
		r.drawImage(((ImageTile)gc.getImageLoader().getImage("bar")).getTileImage(0, 1), gc.getWidth() / 2 - 334 / 2, gc.getHeight() - 100, 0,0,334,13);
		for(int i =1; i <=2 ; i++) {
			r.drawImage(gc.getImageLoader().getImage("slot"), gc.getWidth()/2 + (int)(64 * (i-1) + 26 * ( i - .5f)), gc.getHeight() - 80, 0);
			r.drawImage(gc.getImageLoader().getImage("slot"), gc.getWidth()/2 - (int)(64 * i + 26 * ( i - .5f)), gc.getHeight() - 80, 0);
		}
	}
	
	
	public Player addPlayer(String name,int tileX, int tileY,boolean isLocalPlayer) {
		Player p = new Player(name,tileX, tileY, this,isLocalPlayer);
		this.objects.add(p);
		return p;
	}
	public Player addPlayer(String name,boolean isLocalPlayer) {
		int[] pos = maps[currentMapIndex].RandomSpawnPoint();
		Player p = new Player(name,pos[0], pos[1], this,isLocalPlayer);
		this.objects.add(p);
		return p;
	}
	public void removePlayer(String name) {
		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).getTag().equals(name)) {
				objects.remove(i);
				break;
			}
		}
	}
	
	//-------------------------------------------------------------------------------------
	
	public float getHeight(int tileX, int tileY) {
		if(currentMapIndex >= maps.length || currentMapIndex < 0)
			return Physics.MAX_HEIGHT;
		if(maps[currentMapIndex] == null)
			return Physics.MAX_HEIGHT;
		
		return maps[currentMapIndex].getHeight(tileX, tileY);		
	}
	
	public boolean getCollision(int tileX,int tileY) {
		if(currentMapIndex >= maps.length || currentMapIndex < 0)
			return true;
		if(maps[currentMapIndex] == null)
			return true;
		
		return maps[currentMapIndex].getCollision(tileX, tileY);
	}
	
	@Override
	public int getLevelW() {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < maps.length;i++) {
			if(maps[i].isActive())
				return maps[i].getLevelW();
		}
		return 0;
	}

	@Override
	public int getLevelH() {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < maps.length;i++)		{
			if(maps[i].isActive())
				return maps[i].getLevelH();
		}
		return 0;
		
	}

	public Camera getCamera() {
		return camera;
	}
}
