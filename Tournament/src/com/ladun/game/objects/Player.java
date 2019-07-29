package com.ladun.game.objects;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.Physics;
import com.ladun.game.Point;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.components.NetworkTransform;
import com.ladun.game.components.RectCollider;
import com.ladun.game.net.Client;

public class Player extends Entity{
	
	private static float HIT_TIME = .5f;
	
	private ArrayList<Point>	clickPoints = new ArrayList<Point>(); // 차후에 큐로 바꾸기
	
	private int[] animMaxIndex = {6,1,1};
	
	private float fcos,fsin;
	private boolean readyToShoot = false;
	
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
		this.pR = pL = 20;
		this.pB = 0;
		this.pT = 30;
		
		this.maxHealth 	= 100;
		this.health 	= maxHealth;
		this.speed 		= 200;
		this.jump		= -7f;
		
		this.animSpeed = 12;
		
		this.localPlayer = localPlayer;
		this.gs = gs;
		
		this.weapon = new Weapon(this);
		
		this.addComponent(new NetworkTransform(this,localPlayer));
		this.addComponent(new RectCollider(this));
	}
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		nextHitTime += dt;
		
		if(localPlayer){		
			//System.out.println("["+offX +", " + Math.round(posY)  + ", " + offZ +"] | [" + tileX + "," + tileZ +"]," + (tileZ + (int)Math.signum(((offZ > pB) || (offZ < -pT))?offZ:0)));
		
			
			// Temporary------------------------------------------------------
			
			if(gc.getInput().isKeyDown(KeyEvent.VK_1)) {
				weapon.setType(Weapon.Type.SWORD);
				gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x13,0});
			}
			else if(gc.getInput().isKeyDown(KeyEvent.VK_2)) {
				weapon.setType(Weapon.Type.BOW);
				gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x13,1});
			}
			else if(gc.getInput().isKeyDown(KeyEvent.VK_3)) {
				weapon.setType(Weapon.Type.SPEAR);
				gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x13,2});
			}
			// Temporary End -------------------------------------------------

			
			
			// Moving ------------------------------------------------------
			if(gc.getInput().isButtonDown(MouseEvent.BUTTON3)) {
				int _x  = (int)(gc.getInput().getMouseX() + gs.getCamera().getOffX());
				int _y = (int)(gc.getInput().getMouseY() + gs.getCamera().getOffY());
				
				
				if(gc.getInput().isKey(KeyEvent.VK_SHIFT))
				{
					clickPoints.add(new Point(_x,_y));					
				}
				else {
					angle =(float) Math.toDegrees(Math.atan2(_y - getCenterZ(),  _x - getCenterX()));
					
					fcos = (float)Math.cos(Math.toRadians(angle));
					fsin = (float)Math.sin(Math.toRadians(angle));
					if(clickPoints.size() == 0) {
						clickPoints.add(new Point(_x,_y));
					}
					else if(clickPoints.size() == 1) {
						clickPoints.get(0).setX((int)(gc.getInput().getMouseX() + gs.getCamera().getOffX()));
						clickPoints.get(0).setY((int)(gc.getInput().getMouseY() + gs.getCamera().getOffY()));
					}
					else {
						clickPoints.clear();
						clickPoints.add(new Point(_x,_y));
					}
				}
				
			}
			
			if(clickPoints.size() > 0) {
				float distance = distance(clickPoints.get(0).getX() ,clickPoints.get(0).getY(),getCenterX()	,getCenterZ());
				if(distance >= speed  * dt) {
					moving(gc,dt,speed * fcos,speed * fsin);
				}
				else {
					moving(gc,dt,distance * fcos, distance *fsin);
					clickPoints.remove(0);
				}
			}
			// Moving End --------------------------------------------------
			
						
			// Jump And Gravity ----------------------------------------
			fallDistance += Physics.GRAVITY * dt;
			
			if(fallDistance > 0) { 
				
				int[][] del = {{0, (int)Math.signum(((offX > pR) || (offX < -pL))?offX:0)}, {0, (int)Math.signum(((offZ > pB) || (offZ < -pT))?offZ:0)}}; 
				float _h;
				for(int _y = 0; _y < 2; _y++) {
					for(int _x = 0; _x< 2; _x++) {
						groundHeight= gs.getHeight(tileX + del[0][_x], tileZ + del[1][_y]);
						if(groundHeight== Physics.MAX_HEIGHT) {
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
			
			
			// Attack----------------------------------------------------
			if(gc.getInput().isKeyDown(KeyEvent.VK_A)) {
				if(!weapon.isAttacking()) {
					if(weapon.getType() != Weapon.Type.BOW)	{
						attack(gm,angle);
					}
					else {
						readyToShoot = true;
					}
				}
			}
			if(gc.getInput().isButtonDown(MouseEvent.BUTTON1))
			{
				if(readyToShoot) {

					readyToShoot = false;
					int _x  = (int)(gc.getInput().getMouseX() + gs.getCamera().getOffX());
					int _y = (int)(gc.getInput().getMouseY() + gs.getCamera().getOffY());
					
					angle =(float) Math.toDegrees(Math.atan2(_y - (posZ + height/ 2) ,  _x - (posX + width / 2)	));
					weapon.setDstAngle(angle);
					attack(gm,angle);					
				}
			}
			
			// Attack End -------------------------------------------------
			
			anim += dt * animSpeed;
			if(anim >= animMaxIndex[animType]) {
				anim -=animMaxIndex[animType];
			}
			
			
			if((int)fallDistance != 0) {
				ground = false;
			}
			groundLast = ground;				
			this.AdjustPosition();	
		}
		weapon.update(gc, gm, dt);
		this.updateComponents(gc,gm,dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		
		r.setzDepth((int)(posZ + Math.abs(groundHeight)) + pT + (height - (pT + pB)) );		
		// Shadow Draw
		r.drawFillElipse((int)posX + pL +( width - (pL + pR)) /2,   (int)(posZ + groundHeight) + pT + (height - (pT + pB)) / 2, ( width - (pL + pR)) /2, (height - (pT + pB)) / 2, 0x55000000);
		/*
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
		r.drawImageTile((ImageTile)gc.getImageLoader().getImage("player"),(int)posX,(int)(posZ + posY), (int)anim, animType ,.5f,.5f,0);
		//r.drawText(tag, (int)posX, (int)posZ + height,0xff000000);
		r.drawFillRect((int)posX , (int)(posZ + posY) - 15 , (int)(64 * (health/maxHealth)), 15, 0, 0xff63c564);
		
		for(int i = 0; i < clickPoints.size();i++) {
			//r.drawRect((int)clickPoints.get(i).getX(), (int)clickPoints.get(i).getY(), 32, 32, 0, 0xfff7a87f);
			r.drawImage(gc.getImageLoader().getImage("point"), (int)clickPoints.get(i).getX() - 16, (int)clickPoints.get(i).getY() - 16 , 0);
		}
		
		weapon.render(gc, r);
		
		//RenderRect(r,2);
		this.renderComponents(gc,r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		if(other instanceof HitRange) {
			HitRange hr = (HitRange)other;
			if(!hr.getIgnoreTag().equals(tag)) {
				hit(hr.getDamage());
				
			}
		}
	}

	//---------------------------------------------------------------------------------------
	public void setWeaponType(int t) {
		switch(t){
			case 0 :
				weapon.setType(Weapon.Type.SWORD);
				break;
			case 1 :
				weapon.setType(Weapon.Type.BOW);
				break;
			case 2 :
				weapon.setType(Weapon.Type.SPEAR);
				break;
		}
	}
	
	public void attack(GameManager gm,float _angle) {

		weapon.attack(gm,gs,_angle);			
			
		if(gm != null) {
			if(localPlayer) {
				gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x12,angle});
					
			}
		}
	
	}
	
	private void moving(GameContainer gc, float dt, float dX,float dY) {
		int h = (int)Math.signum(dX);
		int v = (int)Math.signum(dY);
					
		// Moving -------------
		offX += dX * dt;
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
		offZ += dY * dt;
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


		// Moving End --------------

	}
	
	private float distance(float stX, float stY, float edX, float edY) {
		float dx = edX - stX;
		float dy = edY - stY;
		return (float)Math.sqrt(dx * dx  + dy * dy);
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
	public boolean isLocalPlayer() {
		return localPlayer;
	}
	

	//---------------------------------------------------------------------------------------
	
}
