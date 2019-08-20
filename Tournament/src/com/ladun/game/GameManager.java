package com.ladun.game;

import java.awt.event.KeyEvent;

import com.ladun.engine.AbstractGame;
import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.Scene.AbstractScene;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.Scene.MainMenuScene;
import com.ladun.game.net.Client;
import com.ladun.game.objects.GameObject;

public class GameManager extends AbstractGame {

	public static final int TS = 64;
	
	private Client client;
	
	private boolean loading;
	private float loadingAnim;	
	
	private AbstractScene[] scenes = new AbstractScene[2];
	private int 			sceneInedx = 0;
	
	private Announce announce;
	private ChatBox chatBox;
	@Override
	public boolean init(GameContainer gc) {
		// TODO Auto-generated method stub
		gc.getRenderer().setAmbientColor(-1);
		gc.getImageLoader().ImageLoad();
		announce = new Announce();
		
		if(!addScene(gc,new MainMenuScene(),true)) {
			return false;
		}
		if(!addScene(gc,new GameScene())) {
			return false;
		}
		
		client = new Client("localhost",8192,gc,this);
		//client.connect();
		chatBox = new ChatBox();
		return true;
	}

	@Override
	public void update(GameContainer gc) {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < scenes.length;i++) {
			if(scenes[i].isActive())
				scenes[i].update(gc);
		}
		
		announce.update(gc, this);
		chatBox.update(gc, this);
		

		if(gc.getInput().isKeyDown(KeyEvent.VK_ENTER)) {
			chatBox.addTexts("Hello this is Chat Box");
		}
		
		

		if(loading) {
			loadingAnim+= Time.DELTA_TIME * 16;
			if(loadingAnim >= 16)
				loadingAnim = 0;
		}
	}
	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub

		for(int i = 0 ; i < scenes.length;i++) {
			if(scenes[i].isActive())
				scenes[i].render(gc, r);
		}
		
		r.setzDepth(Renderer.LAYER_UI);
		r.setCamX(0);
		r.setCamY(0);
		if(loading) {
			r.drawImageTile((ImageTile)gc.getImageLoader().getImage("loading"), gc.getWidth() - 150, gc.getHeight() - 150, (int)loadingAnim, 0, 0);
		}
		announce.render(gc, r);
		chatBox.render(gc, r);
		
	}

	@Override
	public void dispose() {
		client.disconnect();
		
	}
	
	
	private boolean addScene(GameContainer gc,AbstractScene scene) {
		return addScene(gc,scene,false);
	}
	
	private boolean addScene(GameContainer gc,AbstractScene scene,boolean active) {
		scenes[sceneInedx] = scene;
		sceneInedx++;
		
		if(!scene.init(gc,this,active)) {
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
	
	public Announce getAnnounce() {
		return announce;
	}

	public ChatBox getChatBox() {
		return chatBox;
	}

	public Client getClient() {
		return client;
	}

	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	public static void main(String[] args) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.setWidth((int)(1280));
		gc.setHeight((int)(720));
		gc.setScale(1f );
		gc.start();
	}
}
