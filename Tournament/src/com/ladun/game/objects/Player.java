package com.ladun.game.objects;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.Physics;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.components.NetworkTransform;
import com.ladun.game.components.RectCollider;
import com.ladun.game.net.Client;
import com.ladun.game.objects.projectile.Bullet;

public class Player extends Entity{
	
	private int anim = 0;
	private int animType = 0;
	private int[] animMaxIndex = {1,1,1};
	
	
	private Weapon weapon;
	
	private boolean localPlayer;

	public Player(String tag,int tileX,int tileZ,GameScene gs,boolean localPlayer) {
		this.tag = tag;
		
		
		this.tileX 	= tileX;
		this.tileZ 	= tileZ;
		this.offX 	= 0;
		this.offZ 	= 0;
		this.posX 	= tileX * GameManager.TS;
		this.posZ 	= tileZ * GameManager.TS;
		this.width 	= GameManager.TS;
		this.height	= GameManager.TS;
		
		this.hY = 50;
		this.pR = pL = 17;
		this.pB = 4;
		this.pT = 32;
		
		this.maxHealth 	= 100;
		this.health 	= maxHealth;
		this.speed 		= 200;
		this.jump		= -7f;
		
		this.localPlayer = localPlayer;
		this.gs = gs;
		
		this.weapon = new Weapon(this);
		
		this.addComponent(new NetworkTransform(this,localPlayer));
		this.addComponent(new RectCollider(this));
	}
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
		if(localPlayer){		
			//System.out.println("["+offX +", " + Math.round(posY)  + ", " + offZ +"] | [" + tileX + "," + tileZ +"]," + (tileZ + (int)Math.signum(((offZ > pB) || (offZ < -pT))?offZ:0)));
			nextHitTime += dt;
			
			int h= gc.getInput().isKey(KeyEvent.VK_D)? 1:gc.getInput().isKey(KeyEvent.VK_A)?-1:0;
			int v = gc.getInput().isKey(KeyEvent.VK_S)? 1:gc.getInput().isKey(KeyEvent.VK_W)?-1:0;
			
			if(gc.getInput().isKey(KeyEvent.VK_SHIFT))
				speed = 50;
			else
				speed = 200;
			
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
			
			// Jump And Gravity ----------------------------------------
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
			// Jump And Gravity End ------------------------------------
			
			angle =(float) Math.toDegrees(Math.atan2(
							gc.getInput().getMouseY() - (posZ + height/ 2 - gs.getCamera().getOffY()), 
							gc.getInput().getMouseX() - (posX + width / 2- gs.getCamera().getOffX())
							));
			
			
			if(gc.getInput().isButtonDown(MouseEvent.BUTTON1)) {
				//Shoot(gm);
				weapon.Attack();
			}if(gc.getInput().isButtonDown(MouseEvent.BUTTON2)) {
				Shoot(gm);
			}
			
			
			if((int)fallDistance != 0) {
				ground = false;
			}
			groundLast = ground;				
			this.AdjustPosition();
			
			

			weapon.update(gc, gm, dt);
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
		
		
		r.setzDepth((int)(posZ + Math.abs(posY) + height));
		//r.drawImage(gc.getImageLoader().getImage("player"), (int)posX, (int)(posZ + posY), angle);
		r.drawImageTile((ImageTile)gc.getImageLoader().getImage("player"),(int)posX,(int)(posZ + posY),0,0,.5f,.5f,0);
		r.drawText(tag, (int)posX, (int)posZ + height,0xff000000);
		
		
		
		weapon.render(gc, r);
		
		//RenderRect(r,2);
		this.renderComponents(gc,r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}

	//---------------------------------------------------------------------------------------
	public void Shoot(GameManager gm) {
		Bullet bullet = (Bullet)gs.getInactiveObject("bullet");
		if(bullet == null) {
			gs.addObject(new Bullet(posX,posY,posZ,angle,1000,1));
		}
		else {
			bullet.setting(posX, posY, posZ, angle, 1000, 1);
		}
		
		if(gm != null) {
			if(localPlayer) {
				gm.getClient().send(Client.PACKET_TYPE_OBJECTSPAWN,new Object[] {"bullet"});
			}
		}
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
