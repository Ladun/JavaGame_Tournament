package com.ladun.game.objects;

import java.awt.event.KeyEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Physics;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.components.NetworkTransform;

public class Player extends Entity{
	
	private boolean localPlayer;

	public Player(String tag,int tileX,int tileZ,GameScene gs,boolean localPlayer) {
		this.tag = tag;
		
		
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
		this.jump		= -7f;
		
		this.localPlayer = localPlayer;
		this.gs = gs;
		
		this.addComponent(new NetworkTransform(this,localPlayer));
	}
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
		if(localPlayer){		
			//System.out.println("["+offX +", " + Math.round(posY)  + ", " + offZ +"] | [" + tileX + "," + tileZ +"]," + (tileZ + (int)Math.signum(((offZ > pB) || (offZ < -pT))?offZ:0)));
			nextHitTime += dt;
			
			int h= gc.getInput().isKey(KeyEvent.VK_RIGHT)? 1:gc.getInput().isKey(KeyEvent.VK_LEFT)?-1:0;
			int v = gc.getInput().isKey(KeyEvent.VK_DOWN)? 1:gc.getInput().isKey(KeyEvent.VK_UP)?-1:0;
			
			if(gc.getInput().isKey(KeyEvent.VK_SHIFT))
				speed = 50;
			else
				speed = 500;
			
			// Moving -------------
			if(h != 0)	{
				offX += dt * speed* h;
				if((gs.getHeight(tileX + h, tileZ) < Math.round(posY)  )||
				   (gs.getHeight(tileX + h, tileZ + (int)Math.signum(((offZ > pB) || (offZ < -pT))?offZ:0)) < Math.round(posY)  )){	
					
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
			if(v != 0)	{				
				offZ += dt * speed * v;
				if((gs.getHeight(tileX														, tileZ + v) < Math.round(posY)  )||
				   (gs.getHeight(tileX + (int)Math.signum(((offX > pR) || (offX < -pL))?offX:0), tileZ + v) < Math.round(posY)  )){	
	
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
				
				int[][] del = {{0, (int)Math.signum(((offX > pR) || (offX < -pL))?offX:0)}, {0, (int)Math.signum(((offZ > pB) || (offZ < -pT))?offZ:0)}}; 
				for(int _y = 0; _y < 2; _y++) {
					for(int _x = 0; _x< 2; _x++) {
						groundHeight = gs.getHeight(tileX + del[0][_x], tileZ + del[1][_y]);
						if(groundHeight == Physics.MAX_HEIGHT) {
							groundHeight = 0;
							continue;
						}
						
						if(Math.round(posY) >= groundHeight)
						{
							posY = groundHeight;
							fallDistance = 0;
							ground = true;
						}					
					}				
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
			
			if(gc.getInput().isKey(KeyEvent.VK_T))
				angle -= dt * 360;
		}

		this.updateComponents(gc,gm,dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		/*
		r.setzDepth((int)(Math.abs(groundHeight) ));		
		// Shadow Draw
		r.drawFillElipse((int)posX + pL +( width - (pL + pR)) /2,   (int)(posZ + groundHeight) + pT + (height - (pT + pB)) / 2, ( width - (pL + pR)) /2, (height - (pT + pB)) / 2, 0x55000000);

		r.setzDepth((int)(Math.abs(posY) ));
		// PosX PosZ Position Draw
		r.drawFillRect((int)posX + pL,   (int)(posZ) + pT,    width - (pL + pR),    height - (pT + pB),angle,0x55000000);
		*/
		
		/*
		// Basic Physics Bound Draw
		r.drawFillRect((int)posX + pL,   (int)(posZ + posY) + pT,    width - (pL + pR),    height - (pT + pB),angle,0xffffffff);
		r.drawFillRect((int)posX + pL+(width - (pL + pR)) / 4,   (int)(posZ + posY)+ pT + (height - (pT + pB)) /2 -hY,   (width - (pL + pR)) / 2,   hY,angle, 0xff30b2c9);
		 */

		r.setzDepth((int)(Math.abs(posY) ));
		r.drawImage(gc.getImageLoader().getImage("HOS"), (int)posX, (int)(posZ + posY), angle);
		r.drawText(tag, (int)posX, (int)posZ + height,0xff000000);
		
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
			r.drawFillRect((int)posX, (int)(posZ + posY),1,1,0, 0xffffffff);
			r.drawFillRect((int)posX + width, (int)(posZ + posY) + height,1,1,0, 0xffffffff);
			break;
		case 2:
			r.drawRect((int)posX, (int)(posZ + posY), width, height,angle, 0xffffffff);
		}
			
	}

	//---------------------------------------------------------------------------------------
}
