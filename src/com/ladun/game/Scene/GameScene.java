package com.ladun.game.Scene;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Map;
import com.ladun.game.objects.Player;

public class GameScene extends AbstractScene{

	private Map[] maps = new Map[1];
	private int currentMapIndex = 0;
	
	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		this.name = "GameScene";
		
		this.objects.add(new Player(100,100));
		
		
		maps[0] = new Map();
		return true;
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
		
		for(int i = 0; i < objects.size();i++) {
			objects.get(i).update(gc, gm, dt);
		}
		maps[currentMapIndex].update(gc, gm, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		for(int i = 0; i < objects.size();i++) {
			objects.get(i).render(gc, r);
		}
		maps[currentMapIndex].render(gc, r);
		
	}

	@Override
	public int getLevelW() {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < maps.length;i++)		{
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
