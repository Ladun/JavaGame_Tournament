package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.game.GameManager;

public class DisplayNumber extends GameObject{

	private int number;
	private float time;
	private int color;
	private final int HIT_COLOR = 0xffff4747;
	private final int CRIT_COLOR = 0xfff68801;
	private final int HEAL_COLOR = 0xff6bff47;
	
	public DisplayNumber(int damage,float posX, float posY) {
		this.tag = "displayDamage";
		setting(damage,posX,posY);
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		time += Time.DELTA_TIME;
		posY -= Time.DELTA_TIME * 32;
		
		if(time >= 1.5f) {
			active = false;
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		r.setzDepth(Renderer.LAYER_UI);
		
		r.drawText(Integer.toString(number), (int)posX, (int)posY, 0xfff9472c);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}
	
	// ------------------------------------------------------------------------
	public void setting(int number,float posX, float posY) {
		active = true;
		this.number = number;
		this.posX = posX;
		this.posY = posY;		
		this.time = 0;
		if(number > 0) 
			color =HEAL_COLOR;		
		else if(number < 0) 
			color = HIT_COLOR;		
		else
			color = 0;
	}
	// ------------------------------------------------------------------------
	

}