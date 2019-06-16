package com.ladun.game.objects;

import java.awt.event.KeyEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;

public class Player extends Entity{

	public Player(int posX,int posY) {
		this.tag = "Player";
		this.posX = posX;
		this.posZ = posY;
		this.width = 30;
		this.height= 50;
		
		this.maxHealth = 100;
		this.health = maxHealth;
		this.speed = 500;
	}
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		int h = gc.getInput().isKey(KeyEvent.VK_DOWN)? 1 :  gc.getInput().isKey(KeyEvent.VK_UP)? -1 : 0;
		int v = gc.getInput().isKey(KeyEvent.VK_RIGHT)? 1 :  gc.getInput().isKey(KeyEvent.VK_LEFT)? -1 : 0;
		
		posX += v * speed * dt;
		posZ += h * speed * dt;
			
		
	
		this.updateComponents(gc,gm,dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		r.drawFillRect((int)posX,(int)posZ,width,height,0xffffffff);
	
		this.renderComponents(gc,r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}

}
