package com.ladun.game.objects;

import com.ladun.game.GameManager;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.net.Client;

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
