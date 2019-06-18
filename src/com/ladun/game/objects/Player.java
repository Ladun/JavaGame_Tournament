package com.ladun.game.objects;

import java.awt.event.KeyEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Scene.GameScene;

public class Player extends Entity{
	
	

	public Player(int tileX,int tileZ) {
		this.tag = "Player";
		
		
		this.tileX = tileX;
		this.tileZ = tileZ;
		this.offX = 0;
		this.offZ = 0;
		this.posX = tileX * GameManager.TS;
		this.posZ = tileZ * GameManager.TS;
		this.width = 64;
		this.height= 64;
		this.pL =  4;
		this.pR = 17;
		this.pT = 10;
		this.pB = 7;
		
		this.maxHealth = 100;
		this.health = maxHealth;
		this.speed = 500;
	}
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		nextHitTime += dt;
		
		int h= gc.getInput().isKey(KeyEvent.VK_RIGHT)? 1:gc.getInput().isKey(KeyEvent.VK_LEFT)?-1:0;
		int v = gc.getInput().isKey(KeyEvent.VK_DOWN)? 1:gc.getInput().isKey(KeyEvent.VK_UP)?-1:0;
		
		if(gc.getInput().isKey(KeyEvent.VK_SPACE))
			speed = 20;
		else
			speed = 500;
		
		//Left and Right
		if(h != 0)
		{
			offX += dt * speed* h;
			if(((GameScene)gm.getActiveScene()).getCollision(tileX + h, tileZ)||
					((GameScene)gm.getActiveScene()).getCollision(tileX + h, tileZ + (int)Math.signum(((offZ > pB) | (offZ < -pT)?offZ:0)))){	
				
				if(h == 1) {
					if(offX > pR)
						offX = pR;
				}
				else {
					if(offX < -pL)
						offX = -pL;
				}
			}
			moving = true;
		}
		if(v != 0)
		{				
			offZ += dt * speed * v;
			if(((GameScene)gm.getActiveScene()).getCollision(tileX, tileZ+v) ||
					((GameScene)gm.getActiveScene()).getCollision(tileX+ (int)Math.signum(((offX > pR) | (offX < -pL)?offX:0)), tileZ+v)){	

				if(v == 1) {
					if(offZ > pB)
						offZ = pB;
				}
				else {
					if(offZ < -pT)
						offZ = -pT;
				}
		
			}
			moving = true;
		}
		
		if(h == 0 && v == 0)
		{
			moving = false;
		}
		
		//End of Left and Right
	
		this.AdjustPosition();
		this.updateComponents(gc,gm,dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		r.drawFillRect((int)posX + pL ,(int)posZ + pT,width - (pL + pR),height - (pT + pB),0xffffffff);
		r.drawFillRect((int)posX, (int)posZ,1,1, 0xffffffff);
		r.drawFillRect((int)posX + width, (int)posZ + height,1,1, 0xffffffff);
	
		this.renderComponents(gc,r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}

}
