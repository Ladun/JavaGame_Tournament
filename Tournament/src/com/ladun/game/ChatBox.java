package com.ladun.game;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;

public class ChatBox {
	
	private final int MAX_HEIGHT = 5;
	
	private String[] texts;
	private float[] timers;
	private int[] colors;
	private int head;	
	
	private boolean chatting;
	
	public ChatBox() {
		texts = new String[MAX_HEIGHT];
		timers = new float[MAX_HEIGHT];
		colors = new int[MAX_HEIGHT];
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
				if(timers[index] > 0) {
					r.drawFillRect(20, gc.getHeight() - 220 - 30* (i -  1),
							gc.getWindow().getG().getFontMetrics(gc.getWindow().getG().getFont().deriveFont((float)25)).stringWidth(texts[index]) + 10, 25, 0, 0x77ffffff);
					r.drawString(texts[index], 20, gc.getHeight() - 220 - 30* (i -  1), 25, colors[index]);//0xffff9447);
				}
			}
		}
	}
	
	public void addTexts(String text,int color) {
		texts[head] = text;
		timers[head] =7;
		colors[head] = color;
		head = (head + 1) % MAX_HEIGHT;
		
	}
}
