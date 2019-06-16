package com.ladun.game;

import java.util.ArrayList;

import com.ladun.engine.AbstractGame;
import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.Scene.AbstractScene;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.objects.GameObject;

public class GameManager extends AbstractGame {

	public static final int TS = 16;
	
	
	private AbstractScene[] scenes = new AbstractScene[1];
	private int 			sceneInedx = 0;
	
	private Camera 		camera;

	@Override
	public boolean init(GameContainer gc) {
		// TODO Auto-generated method stub
		gc.getRenderer().setAmbientColor(-1);
		

		if(!addScene(new GameScene())) {
			return false;
		}
		
		return true;
	}

	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < scenes.length;i++) {
			scenes[i].update(gc,this,dt);
		}
	}
	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub

		for(int i = 0 ; i < scenes.length;i++) {
			scenes[i].render(gc, r);
		}
	}
	
	private boolean addScene(AbstractScene scene) {
		if(!scene.init()) {
			//System.out.println("Scene Init Failed");
			return false;
		}
		
		scenes[sceneInedx] = scene;
		sceneInedx++;
		return true;
	}
	
	public AbstractScene getActiveScene() {
		for(int i = 0 ; i  < scenes.length;i++)	{
			if(scenes[i].isActive())
				return scenes[i];
		}
		return null;
	}
	
	public GameObject getObject(String tag) {
		for(int s = 0 ; s < scenes.length;s++) {		
			if(!scenes[s].isActive())
				continue;
			
			return scenes[s].getObject(tag);
		}		
		return null;
	}
	
	
	public static void main(String[] args) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.setWidth(960);
		gc.setHeight(720);
		gc.setScale(1f);
		gc.start();
	}
}
