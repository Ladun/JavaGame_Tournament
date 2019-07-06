package com.ladun.game.objects.projectile;

import com.ladun.game.GameManager;
import com.ladun.game.objects.GameObject;

public abstract class Projectile extends GameObject{

	protected float speed;
	protected float damage;
	
	protected float s; //sin;
	protected float c; //cos;
	
	protected int tileX,tileZ;
	protected float offX,offZ;
	
	protected float time;
	
	public Projectile(float posX, float posY, float posZ, float angle,float speed, float damage) {
		this.speed = speed;
		this.damage = damage;

		this.tileX = (int) (posX / GameManager.TS);
		this.tileZ = (int) (posX / GameManager.TS);
		this.offX = posX - tileX * GameManager.TS;
		this.offZ = posZ - tileZ * GameManager.TS;
		
		this.posY = posY;
		this.angle = angle;
		this.s = (float)Math.sin(Math.toRadians(angle));
		this.c = (float)Math.cos(Math.toRadians(angle));
	}

	
	public void setting(float posX, float posY,float posZ,float angle, float speed, float damage) {
		
		this.tileX = (int) (posX / GameManager.TS);
		this.tileZ = (int) (posX / GameManager.TS);
		this.offX = posX - tileX * GameManager.TS;
		this.offZ = posZ - tileZ * GameManager.TS;
		this.posY = posY;
		
		this.angle = angle;
		this.s = (float)Math.sin(Math.toRadians(angle));
		this.c = (float)Math.cos(Math.toRadians(angle));
		
		this.speed = speed;
		this.damage = damage;
		this.time = 0;
		this.active = true;
	}
	
	protected void AdjustPosition()	{
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
	
	
}