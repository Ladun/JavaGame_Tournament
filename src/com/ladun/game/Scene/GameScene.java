package com.ladun.game.Scene;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.Camera;
import com.ladun.game.GameManager;
import com.ladun.game.Map;
import com.ladun.game.objects.Player;

public class GameScene extends AbstractScene{

	private Map[] maps = new Map[1];
	private int currentMapIndex = 0;
	
	
	private Camera 		camera;
	@Override
	public boolean init(GameManager gm,boolean active) {
		// TODO Auto-generated method stub
		this.active = active;
		
		this.name = "GameScene";		
		this.objects.add(new Player(1,1));
		this.camera = new Camera(gm,"Player");
		
		this.maps[0] = new Map("test",true);
		return true;
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		maps[currentMapIndex].update(gc, gm, dt);
		
		for(int i = 0; i < objects.size();i++) {
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
			objects.get(i).render(gc, r);
		}
	}
	
	
	//-------------------------------------------------------------------------------------
	
	public boolean getCollision(int x,int y) {
		if(currentMapIndex >= maps.length || currentMapIndex < 0)
			return true;
		if(maps[currentMapIndex] == null)
			return true;
		
		return maps[currentMapIndex].getCollision(x, y);
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
}
