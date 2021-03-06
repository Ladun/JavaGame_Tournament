package com.ladun.game;

import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;

public class Announce {

	public static final int DEFAULT_COLOR = 0xff000000;
	private final int SIZE = 30;

	private float time;
	private ArrayList<Content> contents = new ArrayList<Content>();
	
	public void update(GameContainer gc, GameManager gm) {
		if(time <= 0)
			return;
		
		time -= Time.DELTA_TIME;
		if(time <= 0) {
			contents.clear();
		}
	}
	
	public void render(GameContainer gc, Renderer r) {
		if(time <= 0)
			return;
		
		//r.drawFillRect(gc.getWidth() / 2 - 100, gc.getHeight() /2 - 50, 200, 100, 0, 0xff9facf1);
		for(int i =0 ;i < contents.size();i++) {
			r.drawString(contents.get(i).str,Renderer.ALLIGN_CENTER, 50+ i * (SIZE + 4),SIZE ,contents.get(i).color);
		}
		
	}
	
	public void addString(String str,int color) {
		if(time > 0 ) {
			time = 0;
			contents.clear();
		}
		
		contents.add(new Content(str, color));
	}
	public void show(float time) {
		this.time = time;		
	}
	
	class Content{
		public String str;
		public int color;
		
		public Content(String str, int color) {
			this.str = str;
			this.color = color;
		}
	}
	
}
