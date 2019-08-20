package com.ladun.game;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;

public class ChatBox {
	
	private final int MAX_HEIGHT = 5;
	
	private String[] texts;
	private float[] timers;
	private int head;	
	
	private boolean chatting;
	
	public ChatBox() {
		texts = new String[MAX_HEIGHT];
		timers = new float[MAX_HEIGHT];
	}
	
	public void update(GameContainer gc, GameManager gm){
		for(int i = 0; i < MAX_HEIGHT;i++) {
			if(timers[i] > 0) {
				timers[i] -= Time.DELTA_TIME;
				if(timers[i] < 0) {
					timers[i] = 0;
				}
			}
		}
		
		
		
	}
	
	public void render(GameContainer gc, Renderer r) {
		for(int i = 1; i <= MAX_HEIGHT; i++) {
			int index = (head - i) < 0 ? MAX_HEIGHT + (head - i) : head - i;
			if(texts[index] != null && texts[index].length() > 0) {
				if(timers[index] > 0)
					r.drawString(texts[index], 20, gc.getHeight() - 200 - 30* (i -  1), 16, 0xffff9447);
			}
		}
	}
	
	public void addTexts(String text) {
		texts[head] = text;
		timers[head] =7;
		head = (head + 1) % MAX_HEIGHT;
		
	}
}
