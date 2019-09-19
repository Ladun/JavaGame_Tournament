package com.ladun.game.objects;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.Physics;
import com.ladun.game.Point;
import com.ladun.game.Item.Item;
import com.ladun.game.Item.Item.Type;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.Scene.TeamColor;
import com.ladun.game.components.NetworkTransform;
import com.ladun.game.components.RectCollider;
import com.ladun.game.components.Rigidbody;
import com.ladun.game.net.Client;
import com.ladun.game.util.Util;

public class Player extends Entity{
	
	
	private ArrayList<Point>	clickPoints = new ArrayList<Point>(); // 차후에 큐로 바꾸기
	private Item[] items = new Item[6];
	
	private int[] animMaxIndex = {6,1,1};
	
	private float fcos,fsin;
	private boolean readyToShoot = false;
	private int attackIndex = 0;	

	//--------------------------------------
	private int maxMana;
	private int mana;
	//--------------------------------------
	private float reduceAttack_A_Cool = 1f;
	
	private float skill_hideTime = 0;
	private float skill_bowTime = 0;
	
	private Weapon weapon;
	//--------------------------------------	
	private boolean localPlayer;
	private int teamNumber;
	private String nickname;
	//--------------------------------------
	

	public Player(String tag,String nickname,int tileX,int tileZ,GameScene gs,boolean localPlayer) {
		
		this.tag = tag;
		this.nickname = nickname;
		
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
		
		this.moveSpeed 		= 200;
		this.jump		= -7f;		
		this.animSpeed = 12;
		
		this.gs = gs;
		this.gm = gs.getGm();
		
		this.localPlayer = localPlayer;
		
		this.weapon = new Weapon(this);
		this.maxHealth 	= 100;
		revival();
				
		
		for(int i =0;i < 6;i++) {
			items[i] = new Item();
		}
		
		
		this.addComponent(new NetworkTransform(this,localPlayer));
		this.addComponent(new RectCollider(this));
		this.addComponent(new Rigidbody(this));
		
		if(localPlayer) {
			gs.setLocalPlayer(this);
			
			actionCoolDownTime = new float[4];
		}
	}
	@Override
	public void update(GameContainer gc, GameManager gm) {
		if(currentMapIndex != gs.getCurrentMapIndex())
			return;

		if(gs.isGameisStart()) {

			timePassAttackObject();
			
			if(localPlayer){	
				
				
				//if(gc.getInput().isKeyDown(KeyEvent.VK_L))
				//	System.out.println("["+offX +", " + Math.round(posY)  + ", " + offZ +"] | [" + tileX + "," + tileZ +"]," + (tileZ + (int)Math.signum(((offZ > pB) || (offZ < -pT))?offZ:0)));
				
				
				for(int i = 0; i < 4; i ++) {
					if(actionCoolDownTime[i] > 0 ) {
						actionCoolDownTime[i] -= Time.DELTA_TIME;
						if(actionCoolDownTime[i]  < 0)
							actionCoolDownTime[i]  = 0;
					}
				}
				buffTimer();
				
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
					if(distance >= getMoveSpeed()  * Time.DELTA_TIME) {
						moving(getMoveSpeed() * fcos,getMoveSpeed() * fsin);
					}
					else {
						moving(distance * fcos, distance * fsin);
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
				if(currentMapIndex >= 2) {
					if(!gs.isChatting()) {
						if(gc.getInput().isKeyDown(KeyEvent.VK_A)) {
							if(actionCoolDownTime[0]  <= 0) {
								attackIndex = 0;
								
								if(!weapon.isAttacking()) {
									if(weapon.getType() != Weapon.Type.BOW)	{
										actionCoolDownTime[0] = weapon.getCoolDown(attackIndex) * reduceAttack_A_Cool;
										attack(gc,gm,(int)getCenterX(),(int)getCenterZ(),angle,0);
										
										if(skill_hideTime > 0) {
											hiding = false;
											gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x18,0});
											actionCoolDownTime[3] = 4;
										}
									}
									else {
										readyToShoot = true;
									}
								}	
							}
	
						}
						else if(gc.getInput().isKeyDown(KeyEvent.VK_S)) {
							if(actionCoolDownTime[1] <= 0) {
								attackIndex = 1;
								switch(weapon.getType()) {
								case SWORD:
									
									break;
								case BOW:
									readyToShoot = true;	
									break;
								case SPEAR:
									
									break;
								case DAGGER:
									
									break;
								}
							}
						}
						else if(gc.getInput().isKeyDown(KeyEvent.VK_D)) {
							if(actionCoolDownTime[2] <= 0) {
								attackIndex = 2;
							}
						}
						else if(gc.getInput().isKeyDown(KeyEvent.VK_F)) {
							if(actionCoolDownTime[3] <= 0) {
								attackIndex = 3;
								switch(weapon.getType()) {
								case SWORD:
									
									break;
								case BOW:
									skill_bowTime = 5;
									reduceAttack_A_Cool = .5f;
									actionCoolDownTime[attackIndex] = weapon.getCoolDown(attackIndex);
									
									break;
								case SPEAR:
									
									break;
								case DAGGER:
									skill_hideTime= 10;
									hiding = true;
									gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x18,1});
									actionCoolDownTime[attackIndex] = weapon.getCoolDown(attackIndex);
									break;
								}
							}
						}
					}
					

					if(gc.getInput().isButtonDown(MouseEvent.BUTTON1)) {
						if(readyToShoot) {
							System.out.println(reduceAttack_A_Cool);
							if(attackIndex == 0)
								actionCoolDownTime[attackIndex] = weapon.getCoolDown(attackIndex) * reduceAttack_A_Cool;
							else
								actionCoolDownTime[attackIndex] = weapon.getCoolDown(attackIndex);
							
							readyToShoot = false;
							int _x  = (int)(gc.getInput().getMouseX() + gs.getCamera().getOffX());
							int _y = (int)(gc.getInput().getMouseY() + gs.getCamera().getOffY());
							
							angle =(float) Math.toDegrees(Math.atan2(_y - (posZ + height/ 2) ,  _x - (posX + width / 2)	));
							weapon.setDstAngle(angle);
							attack(gc,gm,(int)getCenterX(),(int)getCenterZ(),angle,attackIndex);					
						}
					}
				}
				// Attack End -------------------------------------------------
				
				if(moving) {
					anim += Time.DELTA_TIME * animSpeed;
					if(anim >= animMaxIndex[animType]) {
						anim -=animMaxIndex[animType];
					}
				}
				else {
					anim = 0;
				}
				
				
				if((int)fallDistance != 0) {
					ground = false;
				}
				groundLast = ground;				
				this.AdjustPosition();	
			}
		}
		if(currentMapIndex > 0)
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
			r.drawImage(gc.getResourceLoader().getImage("point"), (int)clickPoints.get(i).getX() - 16, (int)clickPoints.get(i).getY() - 16 , 0);
		}
		r.drawImageTile((ImageTile)gc.getResourceLoader().getImage("player"),(int)posX,(int)(posZ + posY), (int)anim, animType ,.5f,.5f,false,false,0,!hiding? 1f:0.3f);
		// HP Bar
		r.drawFillRect((int)posX-10,  (int)(posZ + posY) - 13, 10, 13, 0, TeamColor.values()[teamNumber].getValue());
		r.drawFillRect((int)posX + 6, (int)(posZ + posY) - 13 , (int)(64 * (health/getMaxHealth())), 13, 0, 0xff63c564);
		

		if(currentMapIndex > 0)
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
				if(!hr.getAttackerTag().equals(tag)) {
					hit(hr.getDamage(),hr.getAttackerTag(),other.hashCode());
					System.out.println(Math.toDegrees(Math.atan2(hr.getParent().getCenterZ() - getCenterZ() ,hr.getParent().getCenterX() - getCenterX() ) )- angle % 360);
					
				}
			}
			else if(other instanceof Projectile) {
				hit(((Projectile)other).getDamage(),((Projectile)other).getAttackerTag(),other.hashCode());
			}
		}
	}
	
	public void hit(float damage,String attackerTag) {
		if(damage == 0)
			return;
		hitMain(damage,attackerTag);
	}
	
	@Override
	public void hit(float damage,String attackerTag,int hashcode) {
		if(damage == 0)
			return;
		
		if(!isExistAttackObject(hashcode)) {
			attackObjects.add(new AttackObject(hashcode));
			hitMain(damage,attackerTag);
		}
	}
	
	private void hitMain(float damage,String attackerTag) {
		health -= damage;

		if(localPlayer)
			gs.getGm().getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x15,(int)health,(char)0x01,attackerTag});
		
		Player _p = (Player)gm.getObject(attackerTag);
		if(_p != null) {
			float _angle = (float)Math.toDegrees(Math.atan2(posZ - _p.getPosZ(), posX - _p.getPosX()));
			((Rigidbody)findComponent("rigidbody")).addPower(_p.getWeapon().getKnockback(), _angle);
		}
		//----------------------------------------------------------------------------------------
		DisplayTextInGame displayDamage = (DisplayTextInGame)gs.getInactiveObject("displayDamage");		
		if(displayDamage == null) {
			gs.addObject(new DisplayTextInGame((int)-damage, getCenterX(), posZ + posY));
		}
		else {
			displayDamage.setting((int)-damage,getCenterX(), posZ + posY);
		}
		//----------------------------------------------------------------------------------------
		
		if(health <= 0) {

			gm.getChatBox().addTexts(_p.getNickname() +" kill " + this.nickname,0xff000000);
			
			active =false;
		}
	}
	
	@Override
	public void revival() {
		this.health = getMaxHealth();

		if(localPlayer)
			gs.getGm().getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x15,(int)health,(char)0x00,null});
	
		this.active = true;
	}

	//---------------------------------------------------------------------------------------
	
	private void buffTimer() {

		if(skill_bowTime > 0) {
			skill_bowTime -= Time.DELTA_TIME;
			if(skill_bowTime <= 0) {
				reduceAttack_A_Cool = 1;
				skill_bowTime = 0;						
			}
		}
		if(skill_hideTime > 0) {
			skill_hideTime -= Time.DELTA_TIME;
			if(skill_hideTime <= 0) {
				hiding = false;
				gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x18,0});
				skill_hideTime = 0;						
			}
		}
		
	}

	public void setWeaponType(int t) {
		switch(t){
			case 0 :
				weapon.setType(Weapon.Type.SWORD);
				maxHealth = 180;
				break;
			case 1 :
				weapon.setType(Weapon.Type.BOW);
				maxHealth = 100;
				break;
			case 2 :
				weapon.setType(Weapon.Type.SPEAR);
				maxHealth = 135;
				break;
			case 3:
				weapon.setType(Weapon.Type.DAGGER);
				maxHealth = 100;
				break;
		}
		
		revival();
		if(localPlayer)
			gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x13,t});
	}
	
	public void attack(GameContainer gc,GameManager gm,int posX,int posZ,float _angle,int attackIndex) {

		weapon.attack(gc, gm, gs, posX, posZ, _angle, attackIndex);			

		if(localPlayer) {
			
			if(gm != null) {
				gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x12,posX,posZ,angle,attackIndex});
					
			}
		}
	
	}
	
	private void moving( float dX,float dY) {
		addPosX((float)(dX * Time.DELTA_TIME));
		addPosZ((float)(dY * Time.DELTA_TIME));

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
	public int emptyItemSlot() {
		for(int i =0; i < 6; i++) {
			if(items[i].getID() == -1) {
				return i;
			}
		}
		return -1;
	}
	//---------------------------------------------------------------------------------------
	
	public int getItemStat(Item.Type statType) {
		int itemStat = 0;
		for(int i = 0; i < 6; i++) {
			if(items[i] == null || items[i].getID() == -1)
				continue;
			if(items[i].hasType(statType)) {
				itemStat += items[i].getOptionValue(statType);
				
			}
		}
		return itemStat;
	}
	
	public float getMoveSpeed() {
		return moveSpeed + getItemStat(Item.Type.STAT_MOVESPEED);
	}
	
	public float getMaxMana() {
		return maxMana + getItemStat(Item.Type.STAT_MANA);
	}
	
	public float getCoolDownPercent(int i) {
		if(i >= 4) 
			return 1;
		
		return actionCoolDownTime[i] / weapon.getCoolDown(i);
	}
	public boolean isLocalPlayer() {
		return localPlayer;
	}
	public int getTeamNumber() {
		return teamNumber;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}
	@Override
	public void setPos(int tileX,int tileY) {
		super.setPos(tileX, tileY);
		clickPoints.clear();
	}
	public Weapon getWeapon() {
		return weapon;
	}
	@Override
	public float getMaxHealth() {
		return maxHealth + getItemStat(Item.Type.STAT_HEALTH);
	}
	
	@Override 
	public void addPosX(float _x) {
		int h = (int)Math.signum(_x);
		offX += _x;
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
	}
	@Override 
	public void addPosZ(float _z) {
		int v = (int)Math.signum(_z);
		offZ += _z;
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

	}


	//---------------------------------------------------------------------------------------
	
}
