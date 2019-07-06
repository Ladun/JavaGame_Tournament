package com.ladun.game.Scene;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.Camera;
import com.ladun.game.GameManager;
import com.ladun.game.Map;
import com.ladun.game.Physics;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.Player;

public class GameScene extends AbstractScene{

	private Map[] maps = new Map[1];
	private int currentMapIndex = 0;
	
	
	private Camera 		camera;
	
	private float time = 0;
	
	@Override
	public boolean init(GameManager gm,boolean active) {
		// TODO Auto-generated method stub
		this.active = active;
		
		this.name = "InGame";		
		//this.objects.add(new Player(1,1,this));
		this.camera = new Camera(gm,"Player");
		
		this.maps[0] = new Map("test",true);
		return true;
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		maps[currentMapIndex].update(gc, gm, dt);
		/*
		// Object List Print--------------------------------
		time+= dt;
		if(time > 1) {
			time -= 1;
			System.out.println("---------------------------");
			for(GameObject go : objects)
				System.out.printf("%s|%4.1f|%4.1f|%4.1f|%4.1f|%c\n",go.getTag(),go.getPosX(),go.getPosY(),go.getPosZ(),go.getAngle(),go.isActive()?'T':'F');
			System.out.println("---------------------------");
		}
		//------------------------------------------------------
		*/
		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).isActive())
				objects.get(i).update(gc, gm, dt);
		}

		camera.update(gc, gm, dt);
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