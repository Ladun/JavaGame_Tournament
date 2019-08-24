package com.ladun.engine;

public abstract class AbstractGame {

	private GameContainer gc;
	
	public abstract boolean init( GameContainer gc);
	public abstract void update();
	public abstract void render( Renderer r);
	public abstract void dispose();
}
