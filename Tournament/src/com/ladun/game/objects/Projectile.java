package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.components.CircleCollider;

public class Projectile extends GameObject{

	public enum Type{ ARROW, STONE};
	
	private Type type;
	private int anim = 0;
	
	private float xPivot;
	private float yPivot;
	
	private float speed;
	private float damage;
	
	private float s; //sin;
	private float c; //cos;
	
	private int tileX,tileZ;
	private float offX,offZ;
	
	private float time;
	
	public Projectile(float posX, float posY, float posZ, float angle,float speed, float damage) {
		this.tag = "projectile";		
		setting(posX,posY,posZ,angle,speed,damage);
		
		this.width = 64;
		this.height = 64;
		setType(Type.ARROW);
		
		this.addComponent(new CircleCollider(this));
	}

	@Override
	public void update(GameContainer gc, GameManager gm) {
		offX += speed * c * Time.DELTA_TIME;
		offZ += speed * s * Time.DELTA_TIME;
		
		time += Time.DELTA_TIME;
		if(time > 3) {
			this.active = false;
		}
		
		this.AdjustPosition();
		
		this.updateComponents(gc, gm);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		r.setzDepth((int)(posZ + Math.abs(posY) + height));
		//r.drawFillRect((int)posX, (int)posZ, width, height, angle, 0xaff385610);
		//r.drawFillRect((int)(posX ), (int)(posZ ), tileZ, tileX, c, color);
		r.drawImageTile((ImageTile)gc.getImageLoader().getImage("projectile"), (int)(posX ), (int)(posZ ), anim, 0, xPivot, yPivot, angle);
		
		//r.drawRect((int)(posX + width * xPivot), (int)(posZ + height * yPivot), 3, 3, 0, 0xffff0000);
		
		this.renderComponents(gc, r);
	}


	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		if(other instanceof Entity) {
			this.active = false;
		}
	}
	//-------------------------------------------------------------------------------------------------------
	public void setting(float posX, float posY,float posZ,float angle, float speed, float damage) {
		
		this.tileX = (int) (posX / GameManager.TS);
		this.tileZ = (int) (posZ / GameManager.TS);
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
	private void AdjustPosition()	{
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
	private void setType(Type type) {
		switch(type) {
		case ARROW:
			pR = 13;
			pL = 42;
			pT = 28;
			pB = 28;
			anim = 0;
			break;
		case STONE:
			pR = 14;
			pL = 14;
			pT = 14;
			pB = 14;
			anim = 1;
			break;
		}

		xPivot = (pL + (width - pL -pR) / 2f)/ width;
		yPivot = (pT + (height - pT -pB) / 2f)/ height;
		
	}
	//-------------------------------------------------------------------------------------------------------


	public float getDamage() {
		return damage;
	}

	
}
