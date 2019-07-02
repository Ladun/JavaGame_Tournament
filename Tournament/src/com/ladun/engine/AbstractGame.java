package com.ladun.engine;

public abstract class AbstractGame {

	public abstract boolean init(GameContainer gc);
	public abstract void update(GameContainer gc,float dt);
	public abstract void render(GameContainer gc, Renderer r);
	public abstract void dispose();
}
