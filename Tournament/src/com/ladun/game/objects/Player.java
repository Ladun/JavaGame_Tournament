package com.ladun.game.objects;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.Item;
import com.ladun.game.Physics;
import com.ladun.game.Point;
import com.ladun.game.Util;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.components.NetworkTransform;
import com.ladun.game.components.RectCollider;
import com.ladun.game.net.Client;

public class Player extends Entity{
	
	
	private ArrayList<Point>	clickPoints = new ArrayList<Point>(); // 차후에 큐로 바꾸기
	private Item[] items = new Item[6];
	
	private int[] animMaxIndex = {6,1,1};
	
	private float fcos,fsin;
	private boolean readyToShoot = false;
	
	
	
	private Weapon weapon;
	//--------------------------------------	
	private boolean localPlayer;
	private int teamNumber;
	//--------------------------------------
	

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
		
		this.speed 		= 200;
		this.jump		= -7f;		
		this.animSpeed = 12;
		
		this.gs = gs;
		
		this.localPlayer = localPlayer;
		this.maxHealth 	= 100;
		
		this.weapon = new Weapon(this);
		this.maxHealth 	= 100;
		revival();
				
		
		for(int i =0;i < 6;i++) {
			items[i] = new Item(0);
		}
		
		
		this.addComponent(new NetworkTransform(this,localPlayer));
		this.addComponent(new RectCollider(this));
		
		if(localPlayer) {
			gs.setLocalPlayer(this);
			
			actionCoolDownTime = new float[4];
		}
	}
	@Override
	public void update(GameContainer gc, GameManager gm) {
		if(currentMapIndex != gs.getCurrentMapIndex())
			return;
		
		if(!gs.isGameisStart())
			return;
		
		nextHitTime += Time.DELTA_TIME;
		
		if(localPlayer){	
			if(gc.getInput().isKeyDown(KeyEvent.VK_L))
				System.out.println("["+offX +", " + Math.round(posY)  + ", " + offZ +"] | [" + tileX + "," + tileZ +"]," + (tileZ + (int)Math.signum(((offZ > pB) || (offZ < -pT))?offZ:0)));
			for(int i = 0; i < 4; i ++) {
				if(actionCoolDownTime[i] > 0 ) {
					actionCoolDownTime[i] -= Time.DELTA_TIME;
					if(actionCoolDownTime[i]  < 0)
						actionCoolDownTime[i]  = 0;
				}
			}
			
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
				
				if(clickPoints.size() == 0) {
					angle =(float) Math.toDegrees(Math.atan2(_y - getCenterZ(),  _x - getCenterX()));
					
					fcos = (float)Math.cos(Math.toRadians(angle));
					fsin = (float)Math.sin(Math.toRadians(angle));
					clickPoints.add(new Point(_x,_y));
				}
				else {					
					if(gc.getInput().isKey(KeyEvent.VK_SHIFT))
					{
						clickPoints.add(new Point(_x,_y));					
					}
					else {
						angle =(float) Math.toDegrees(Math.atan2(_y - getCenterZ(),  _x - getCenterX()));
						
						fcos = (float)Math.cos(Math.toRadians(angle));
						fsin = (float)Math.sin(Math.toRadians(angle));
						if(clickPoints.size() == 1) {
							clickPoints.get(0).setX(_x);
							clickPoints.get(0).setY(_y);
						}
						else {
							clickPoints.clear();
							clickPoints.add(new Point(_x,_y));
						}
					}
				}
				
			}
			
			if(clickPoints.size() > 0) {
				float distance = Util.distance(clickPoints.get(0).getX() ,clickPoints.get(0).getY(),getCenterX()	,getCenterZ());
				if(distance >= speed  * Time.DELTA_TIME) {
					moving(gc,speed * fcos,speed * fsin);
				}
				else {
					moving(gc,distance * fcos, distance *fsin);
					clickPoints.remove(0);
					if(clickPoints.size() > 0) {
						angle =(float) Math.toDegrees(Math.atan2(clickPoints.get(0).getY() - getCenterZ(),  clickPoints.get(0).getX() - getCenterX()));
						fcos = (float)Math.cos(Math.toRadians(angle));
						fsin = (float)Math.sin(Math.toRadians(angle));
						
					}
				}

				moving = true;
			}
			else			{
				moving = false;
			}
			// Moving End --------------------------------------------------
			
			// jump(gc);
			
			
			// Attack----------------------------------------------------
			if(actionCoolDownTime[0] <= 0) {
				if(gc.getInput().isKeyDown(KeyEvent.VK_A)) {
					if(!weapon.isAttacking()) {
						if(weapon.getType() != Weapon.Type.BOW)	{
							actionCoolDownTime[0] = .5f;
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
	
						actionCoolDownTime[0] = .5f;
						readyToShoot = false;
						int _x  = (int)(gc.getInput().getMouseX() + gs.getCamera().getOffX());
						int _y = (int)(gc.getInput().getMouseY() + gs.getCamera().getOffY());
						
						angle =(float) Math.toDegrees(Math.atan2(_y - (posZ + height/ 2) ,  _x - (posX + width / 2)	));
						weapon.setDstAngle(angle);
						attack(gm,angle);					
					}
				}
			}
			// Attack End -------------------------------------------------
			
			anim += Time.DELTA_TIME * animSpeed;
			if(anim >= animMaxIndex[animType]) {
				anim -=animMaxIndex[animType];
			}
			
			
			if((int)fallDistance != 0) {
				ground = false;
			}
			groundLast = ground;				
			this.AdjustPosition();	
		}
		weapon.update(gc, gm);
		this.updateComponents(gc,gm);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		if(currentMapIndex != gs.getCurrentMapIndex())
			return;
		if(!localPlayer)
			if(hiding)
				return;
		
		
		
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
		for(int i = 0; i < clickPoints.size();i++) {
			//r.drawRect((int)clickPoints.get(i).getX(), (int)clickPoints.get(i).getY(), 32, 32, 0, 0xfff7a87f);
			r.drawImage(gc.getImageLoader().getImage("point"), (int)clickPoints.get(i).getX() - 16, (int)clickPoints.get(i).getY() - 16 , 0);
		}
		r.drawImageTile((ImageTile)gc.getImageLoader().getImage("player"),(int)posX,(int)(posZ + posY), (int)anim, animType ,.5f,.5f,0);
		//r.drawText(tag, (int)posX, (int)posZ + height,0xff000000);
		r.drawFillRect((int)posX , (int)(posZ + posY) - 15 , (int)(64 * (health/maxHealth)), 15, 0, 0xff63c564);
		
		
		weapon.render(gc, r);
		
		//RenderRect(r,2);
		this.renderComponents(gc,r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		super.collision(other);
		if(localPlayer) {
			if(other instanceof HitRange) {
				HitRange hr = (HitRange)other;
				if(!hr.getIgnoreTag().equals(tag)) {
					hit(hr.getDamage());
					
				}
			}
			else if(other instanceof Projectile) {
				hit(((Projectile)other).getDamage());
			}
		}
	}
	@Override
	public void hit(float damage) {
		if(nextHitTime >= HIT_TIME) {
			nextHitTime = 0;
			health -= damage;

			if(localPlayer)
				gs.getGm().getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x15,(int)health,(char)0x01});
			//----------------------------------------------------------------------------------------
			DisplayNumber displayDamage = (DisplayNumber)gs.getInactiveObject("displayDamage");		
			if(displayDamage == null) {
				gs.addObject(new DisplayNumber((int)-damage, getCenterX(), posZ + posY));
			}
			else {
				displayDamage.setting((int)-damage,getCenterX(), posZ + posY);
			}
			//----------------------------------------------------------------------------------------
			
			if(health <= 0) {
				active =false;
			}
		}
	}
	@Override
	public void revival() {
		this.health = maxHealth;

		if(localPlayer)
			gs.getGm().getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x15,(int)health,(char)0x00});
	
		this.active = true;
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

		if(localPlayer) {
			
			if(gm != null) {
				gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x12,angle});
					
			}
		}
	
	}
	
	private void moving(GameContainer gc, float dX,float dY) {
		int h = (int)Math.signum(dX);
		int v = (int)Math.signum(dY);
					
		// Moving -------------
		offX += dX * Time.DELTA_TIME;
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
		offZ += dY * Time.DELTA_TIME;
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
	
	private void jump(GameContainer gc) {

		
		fallDistance += Physics.GRAVITY * Time.DELTA_TIME;
		
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
		
		
	}
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
	
	public Item[] getItems() {
		return items;
	}
	//---------------------------------------------------------------------------------------
	public float getCoolDownPercent(int i) {
		if(i >= 4) 
			return 1;
		
		switch(i) {
		case 0:
			return actionCoolDownTime[0] / .5f;
		case 1:
			return actionCoolDownTime[1] / .5f;
		case 2:
			return actionCoolDownTime[2] / .5f;
		case 3:
			return actionCoolDownTime[3] / .5f;
		}
		return 1;
	}
	public boolean isLocalPlayer() {
		return localPlayer;
	}
	public int getTeamNumber() {
		return teamNumber;
	}
	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}
	@Override
	public void setPos(int tileX,int tileY) {
		super.setPos(tileX, tileY);
		clickPoints.clear();
	}
	

	//---------------------------------------------------------------------------------------
	
}
