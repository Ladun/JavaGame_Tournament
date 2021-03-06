package com.ladun.game.Scene;

import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.objects.GameObject;

public abstract class AbstractScene {
	
	protected String 	name;
	protected boolean 	active;	
	protected GameManager gm;
	
	protected ArrayList<GameObject> objects = new ArrayList<GameObject>();

	public abstract boolean init(GameContainer gc,GameManager gm,boolean active);
	public abstract void update(GameContainer gc);
	public abstract void render(GameContainer gc, Renderer r);
	public abstract int getLevelW();
	public abstract int getLevelH();
	

	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public GameObject getInactiveObject(String tag) {
		for (GameObject go : objects) {
			if(!go.isActive())
				if(go.getTag().equals(tag))
					return go;
		}
		return null;
	}
	
	public void addObject(GameObject obj) {
		
		objects.add(obj);
	}
	
	public GameObject getObject(String tag) {
		for(int i = 0; i < objects.size();i++) {
			
			if(objects.get(i).getTag().equals(tag)) {
				return objects.get(i);
			}
		}
		return null;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public GameManager getGm() {
		return gm;
	}
}
