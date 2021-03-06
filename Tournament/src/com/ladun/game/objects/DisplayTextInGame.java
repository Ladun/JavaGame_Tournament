package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.game.GameManager;

public class DisplayTextInGame extends GameObject{
	
	public static final int HIT_COLOR = 0xffff4747;
	public static final int CRIT_COLOR = 0xfff68801;
	public static final int HEAL_COLOR = 0xff6bff47;

	private String text;
	private float time;
	private int color;
	
	public DisplayTextInGame(String text,float posX, float posY,int color) {
		this.tag = "displayTextInGame";
		setting(text,posX,posY,color);
	}
	public DisplayTextInGame(int number,float posX, float posY,int color) {
		this.tag = "displayTextInGame";
		setting(number,posX,posY,color);
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm) {
		
		time += Time.DELTA_TIME;
		posY -= Time.DELTA_TIME * 32;
		
		if(time >= 1.5f) {
			active = false;
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		r.setzDepth(Renderer.LAYER_UI);
		
		r.drawText(text, (int)posX, (int)posY, color);
	}

	@Override
	public void collision(GameObject other) {
		
		
	}
	
	// ------------------------------------------------------------------------
	public void setting(int number, float posX,float posY,int color) {	
		setting(Integer.toString(number),posX,posY,color);
	}
	public void setting(String text,float posX, float posY,int color) {
		active = true;
		this.text = text;
		this.posX = posX;
		this.posY = posY;		
		this.time = 0;
		this.color = color;	
	}
	// ------------------------------------------------------------------------
	

}
