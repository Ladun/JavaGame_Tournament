package com.ladun.game;

import com.ladun.engine.AbstractGame;
import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.Scene.AbstractScene;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.Scene.MainMenuScene;
import com.ladun.game.net.Client;
import com.ladun.game.objects.GameObject;

public class GameManager extends AbstractGame {

	public static final int TS = 64;
	
	private Client client;
	
	private AbstractScene[] scenes = new AbstractScene[2];
	private int 			sceneInedx = 0;
	

	@Override
	public boolean init(GameContainer gc) {
		// TODO Auto-generated method stub
		gc.getRenderer().setAmbientColor(-1);
		
		
		if(!addScene(new MainMenuScene(),true)) {
			return false;
		}
		if(!addScene(new GameScene())) {
			return false;
		}
		
		client = new Client("localhost",8192,this);
		client.connect();
		
		return true;
	}

	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < scenes.length;i++) {
			if(scenes[i].isActive())
				scenes[i].update(gc,this,dt);
		}
	}
	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub

		for(int i = 0 ; i < scenes.length;i++) {
			if(scenes[i].isActive())
				scenes[i].render(gc, r);
		}
		
	}

	@Override
	public void dispose() {
		client.disconnect();		
	}
	
	
	private boolean addScene(AbstractScene scene) {
		return addScene(scene,false);
	}
	
	private boolean addScene(AbstractScene scene,boolean active) {
		scenes[sceneInedx] = scene;
		sceneInedx++;
		
		if(!scene.init(this,active)) {
			//System.out.println("Scene Init Failed");
			return false;
		}
		return true;
	}
	
	public void changeScene(String name) {
		for(int i = 0 ; i < scenes.length;i++) {
			if(scenes[i].isActive())
				scenes[i].setActive(false);
		}
		for(int i = 0 ; i < scenes.length;i++) {
			if(scenes[i].getName().equals(name))
				scenes[i].setActive(true);
		}
	}
	
	//-----------------------------------------------------------------------------------
	
	public AbstractScene getActiveScene() {
		for(int i = 0 ; i  < scenes.length;i++)	{
			if(scenes[i].isActive())
				return scenes[i];
		}
		return null;
	}
	public AbstractScene getScene(String name) {
		for(int i = 0 ; i  < scenes.length;i++)	{
			if(scenes[i].getName().equals(name))
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
	
	
	public Client getClient() {
		return client;
	}

	public static void main(String[] args) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.setWidth(960 );
		gc.setHeight(720 );
		gc.setScale(1f );
		gc.start();
	}
}