package com.ladun.game;

import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;

public class Announce {

	
	private float time;
	private String str;
	
	public void update(GameContainer gc, GameManager gm) {
		if(time <= 0)
			return;
		
		time -= Time.DELTA_TIME;
	}
	
	public void render(GameContainer gc, Renderer r) {
		if(time <= 0)
			return;
		
		//r.drawFillRect(gc.getWidth() / 2 - 100, gc.getHeight() /2 - 50, 200, 100, 0, 0xff9facf1);
		r.drawString(str,-1, 50,50,0xff000000);
	}
	
	public void Announce(String str, float time) {
		this.str = str;
		this.time = time;
	}
	public void Announce(String str) {
		Announce(str, 5);
	}
	
}