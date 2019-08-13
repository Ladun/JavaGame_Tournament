package com.ladun.game.components;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.objects.GameObject;

public abstract class Component {

	protected String tag;
	protected GameObject parent;
	public abstract void update(GameContainer gc,GameManager gm);
	public abstract void render(GameContainer gc,Renderer r);
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}

	public GameObject getParent() {
		return parent;
	}
	public void setParent(GameObject parent) {
		this.parent = parent;
	}

}
