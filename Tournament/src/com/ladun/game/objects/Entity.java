package com.ladun.game.objects;

import com.ladun.game.GameManager;
import com.ladun.game.Util;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.components.Collider;
import com.ladun.game.components.Collider.Type;
import com.ladun.game.components.RectCollider;

public abstract class Entity extends GameObject{
	
	protected GameScene gs;
	protected float maxHealth;
	protected float health;
	protected float speed;
	protected float jump;
	protected float fallDistance;
	
	protected float groundHeight; // 오브젝트가 서있는 땅의 높이
	
	protected int tileX,tileZ;
	protected float offX,offZ;
	

	protected float anim = 0;
	protected float animSpeed;
	protected int animType = 0;
	
	protected boolean ground = false;
	protected boolean groundLast = false;
	protected boolean moving = false;
	protected boolean hiding = false;

	protected float[] actionCoolDownTime;
	
	protected float nextHitTime = 0;
	protected static final float HIT_TIME = .3f; 
	
	protected int currentMapIndex;
	
	public abstract void hit(float damage);
	public abstract void revival();
	
	public void collision(GameObject other) {
		
		 if(other.getTag().equalsIgnoreCase("interior")) {
			 Collider myC 		= (Collider) this.getCollider();
			 Collider otherC 	= (Collider) other.getCollider();

			 switch(myC.getType()) {
			 case CIRCLE:
				 if(otherC.getType() == Type.RECT) {
					 //TODO : if RECT-CIRCLE collision, move object non-collision position
					 
				 }
				 else if(otherC.getType() == Type.CIRCLE) {

				 }
				 break;
			 case RECT:
				 if(otherC.getType() == Type.RECT) {
					 
					 if(Math.abs(myC.getLastCenterX() - otherC.getLastCenterX()) < ((RectCollider)myC).getHalfWidth() + ((RectCollider)otherC).getHalfWidth()){
						if(myC.getCenterZ() < otherC.getCenterZ()) {
							int distance 	= (((RectCollider)myC).getHalfHeight() + ((RectCollider)otherC).getHalfHeight()) - (otherC.getCenterZ() - myC.getCenterZ());
							offZ 			-= distance;
							posZ 			-= distance;
							myC.setCenterZ(myC.getCenterZ() - distance);
							fallDistance 	= 0;
							ground 			= true;
						}
						if(myC.getCenterZ() > otherC.getCenterZ()){
							int distance 	= (((RectCollider)myC).getHalfHeight() + ((RectCollider)otherC).getHalfHeight()) - (myC.getCenterZ() - otherC.getCenterZ());
							offZ 			+= distance;
							posY 			+= distance;
							((RectCollider)myC).setCenterZ(myC.getCenterZ() + distance);
							fallDistance 	= 0;
						}
					}
					else {
						if(myC.getCenterX() < otherC.getCenterX()) {
							int distance 	= (((RectCollider)myC).getHalfWidth() + ((RectCollider)otherC).getHalfWidth()) - (otherC.getCenterX() - ((RectCollider)myC).getCenterX());
							offX 			-= distance;
							posX 			-= distance;
							moving 			= false;
							myC.setCenterX(myC.getCenterX() - distance);
						}
						if(myC.getCenterX() > otherC.getCenterX()) {
							int distance 	= (((RectCollider)myC).getHalfWidth() + ((RectCollider)otherC).getHalfWidth()) - (((RectCollider)myC).getCenterX() - otherC.getCenterX());
							offX 			+= distance;
							posX 			+= distance;
							moving 			= false;
							myC.setCenterX(myC.getCenterX() + distance);
						}
					}
				 }
				 else if(otherC.getType() == Type.CIRCLE) {
					 
				 }
				 break;
			 }
		}
	}
	
	protected void AdjustPosition()
	{
		//Final position
		if(offZ > GameManager.TS / 2)
		{
			tileZ++;
			offZ -= GameManager.TS;
		}
		if(offZ <  -GameManager.TS / 2)
		{
			tileZ--;
			offZ += GameManager.TS;
		}
		
		if(offX > GameManager.TS / 2)
		{
			tileX++;
			offX -= GameManager.TS;
		}
		if(offX <  -GameManager.TS / 2)
		{
			tileX--;
			offX += GameManager.TS;
		}
		//--

		posX = tileX * GameManager.TS + offX;
		posZ = tileZ * GameManager.TS + offZ;
	}

	public float getCenterX() {
		return posX + pL + (width - pL - pR) / 2;
	}
	public float getCenterZ() {
		return posZ + pT + (height - pT - pB) / 2;		
	}

	public float getAnim() {
		return anim;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public void setAnim(float anim) {
		this.anim = anim;
	}

	public int getAnimType() {
		return animType;
	}

	public boolean isMoving() {
		return moving;
	}

	public boolean isHiding() {
		return hiding;
	}

	public void setHiding(boolean hiding) {
		this.hiding = hiding;
	}

	public void setAnimType(int animType) {
		this.animType = animType;
	}
	
	public int getCurrentMapIndex() {
		return currentMapIndex;
	}

	public void setCurrentMapIndex(int currentMapIndex) {
		this.currentMapIndex = currentMapIndex;
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileZ() {
		return tileZ;
	}

	public void setPos(int tileX,int tileZ) {
		this.tileX = tileX;
		this.tileZ =tileZ;
		this.offX = 0;
		this.offZ = 0;
		AdjustPosition();
	}
	public abstract float getCoolDownPercent(int i) ;
}
