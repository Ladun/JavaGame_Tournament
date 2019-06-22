package com.ladun.game.objects;

import java.awt.event.KeyEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Physics;
import com.ladun.game.Scene.GameScene;

public class Player extends Entity{
	
	

	public Player(int tileX,int tileZ,GameScene gs) {
		this.tag = "Player";
		
		
		this.tileX 	= tileX;
		this.tileZ 	= tileZ;
		this.offX 	= 0;
		this.offZ 	= 0;
		this.posX 	= tileX * GameManager.TS;
		this.posZ 	= tileZ * GameManager.TS;
		this.width 	= 64;
		this.height	= 64;
		this.hY = 50;
		this.pR = pL = 17;
		this.pB = 4;
		this.pT = 32;
		
		this.maxHealth 	= 100;
		this.health 	= maxHealth;
		this.speed 		= 500;
		this.jump		= -3.5f;
		
		this.gs = gs;
	}
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		nextHitTime += dt;
		
		int h= gc.getInput().isKey(KeyEvent.VK_RIGHT)? 1:gc.getInput().isKey(KeyEvent.VK_LEFT)?-1:0;
		int v = gc.getInput().isKey(KeyEvent.VK_DOWN)? 1:gc.getInput().isKey(KeyEvent.VK_UP)?-1:0;
		
		// Moving -------------
		if(h != 0)
		{
			offX += dt * speed* h;
			if(gs.getCollision(tileX + h, tileZ)||
			   gs.getCollision(tileX + h, tileZ + (int)Math.signum(((offZ > pB) | (offZ < -pT)?offZ:0)))){	
				
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
			if(gs.getCollision(tileX														, tileZ + v) ||
			   gs.getCollision(tileX + (int)Math.signum(((offX > pR) | (offX < -pL)?offX:0)), tileZ + v)){	

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
		// Moving End --------------
		
		// Jump And Gravity -------------------
		fallDistance += Physics.GRAVITY * dt;
		
		if(fallDistance > 0) {
			if(posY > gs.getHeight(tileX, tileZ))
			{
				fallDistance = 0;
				posY = gs.getHeight(tileX, tileZ);
				ground = true;
			}
		}
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_SPACE))
		{
			if(ground)
				fallDistance = jump;
		}
		
		posY += fallDistance;
		// Jump And Gravity End ---------------
		
		
		if((int)fallDistance != 0) {
			ground = false;
		}
		groundLast = ground;		
		this.AdjustPosition();
		this.updateComponents(gc,gm,dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		// Shadow Draw
		r.drawFillElipse((int)posX + pL +( width - (pL + pR)) /2,   (int)posZ + pT + (height - (pT + pB)) / 2, ( width - (pL + pR)) /2, (height - (pT + pB)) / 2, 0x55000000);
		
		
		// Basic Physics Draw
		r.drawFillRect((int)posX + pL,   (int)(posZ + posY) + pT,    width - (pL + pR),    height - (pT + pB),0xffffffff);
		r.drawFillRect((int)posX + pL+(width - (pL + pR)) / 4,   (int)(posZ + posY)+ pT + (height - (pT + pB)) /2 -hY,   (width - (pL + pR)) / 2,   hY, 0xff30b2c9);
	
		//RenderRect(r,2);
		this.renderComponents(gc,r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}
	
	//---------------------------------------------------------------------------------------
	private void RenderRect(Renderer r,int type) {
		switch(type){
		case 1:
			r.drawFillRect((int)posX, (int)(posZ + posY),1,1, 0xffffffff);
			r.drawFillRect((int)posX + width, (int)(posZ + posY) + height,1,1, 0xffffffff);
			break;
		case 2:
			r.drawRect((int)posX, (int)(posZ + posY), width, height, 0xffffffff);
		}
			
	}

	//---------------------------------------------------------------------------------------
}
