package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;

public class DisplayDamage extends GameObject{

	private int damage;
	private float time;
	
	public DisplayDamage(int damage,float posX, float posY) {
		this.tag = "displayDamage";
		this.damage = damage;
		this.posX = posX;
		this.posY = posY;
		this.time = 0;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		time += dt;
		posY -= dt * 32;
		
		if(time >= 1.5f) {
			active = false;
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		r.setzDepth(Renderer.LAYER_UI);
		
		r.drawText(Integer.toString(damage), (int)posX, (int)posY, 0xff000000);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}
	
	// ------------------------------------------------------------------------
	public void setting(int damage,float posX, float posY) {
		active = true;
		this.damage = damage;
		this.posX = posX;
		this.posY = posY;		
		this.time = 0;
	}
	// ------------------------------------------------------------------------
	

}