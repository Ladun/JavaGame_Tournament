package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.game.GameManager;
import com.ladun.game.Util;
import com.ladun.game.Scene.GameScene;

public class Weapon extends GameObject{
	public enum Type{
		SWORD,BOW,SPEAR
	}
	private final static float LIMIT_TIME = 1/10f; 
	
	private Type type;
	private Entity parent;
	
	private float damage;
	
	private float deltaAngle;
	private float deltaX;
	private float deltaZ;
	private float attackTime;
	private boolean attacking = false;
	
	private float srcAngle = 0;
	private float dstAngle = 0;
	private float time = 0;
	
	private String imageName;
	private float xPivot;
	private float yPivot;
	private float distanceToParent = 24;

	private boolean mirror;

	private HitRange hitRange;
	
	
	
	public Weapon(Entity parent) {
		this.parent = parent;
		width = 64;
		height = 64;
		setType(Type.BOW);
		
		hitRange = new HitRange(this,HitRange.Type.CIRCLE,damage,parent.tag);
		srcAngle = parent.angle;
		dstAngle = parent.angle;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		time += Time.DELTA_TIME;
		positionSetting();
		
		if(attacking) {
			attackFunction();
		}
		
		if(hitRange.isActive())
			hitRange.update(gc, gm);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		r.drawImage(gc.getImageLoader().getImage(imageName),(int)(posX + deltaX),(int)(posZ + deltaZ),xPivot,yPivot,mirror,false,angle + deltaAngle + (mirror? 180 : 0));
		
		r.drawRect((int)posX, (int)posZ, 2,2, 0, 0xffff0000);
		
		
		if(hitRange.isActive())
			hitRange.render(gc, r);;
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
	}
	
	// -------------------------------------------------------------------------
	public void positionSetting() {
		
		dstAngle = parent.angle;
		
		if(time >= LIMIT_TIME) {
			time -= LIMIT_TIME;
			srcAngle = dstAngle;
		}
		
		angle = Util.lerp(srcAngle,dstAngle,time / LIMIT_TIME);
		
		
		
		
		posX = parent.posX + parent.width / 2 - width * xPivot + (float)(distanceToParent * Math.cos(Math.toRadians(angle)));
		posZ = parent.posZ + parent.posY + parent.height / 2 - height * yPivot + (float)(distanceToParent* Math.sin(Math.toRadians(angle)));
		
		if(Math.abs(angle) > 90) {
			mirror = true;
		}
		else {
			mirror = false;
		}
	}
	public void attack(GameManager gm, GameScene gs,float _angle) {
		switch(type) {
		case SPEAR:
		case SWORD:
			attacking = true;
			break;
		case BOW:
			Shoot(gm,gs,_angle);
			break;
		}
		attackTime = 0;
		
	}
	
	private void attackFunction() {
		attackTime += Time.DELTA_TIME;
		
		switch(type) {
		case SWORD:
			if(attackTime >= 0.2f) {
				deltaAngle = 0;
				attacking = false;
				hitRange.setActive(false);
			}
			else {
				
				if(mirror) {
					deltaAngle -= Time.DELTA_TIME * 975;		
					float co = (float)Math.cos(Math.toRadians(deltaAngle +90 +angle));
					float si = (float)Math.sin(Math.toRadians(deltaAngle +90 +angle));

					hitRange.active(posX + width * xPivot - 8 + 48 * co , posZ  + height * yPivot  - 8+  48* si , 16, 16);		
				}
				else {
					deltaAngle += Time.DELTA_TIME * 975;
					float co = (float)Math.cos(Math.toRadians(deltaAngle -90 +angle));
					float si = (float)Math.sin(Math.toRadians(deltaAngle -90 +angle));

					hitRange.active(posX + width * xPivot - 8 + 48 * co , posZ  + height * yPivot  - 8+  48* si , 16, 16);
				}
				
			}
			break;
		case SPEAR:
			if(attackTime >= .2f) {
				deltaX = 0;
				deltaZ = 0;
				attacking = false;
				hitRange.setActive(false);
			}
			else {
				float co = (float)Math.cos(Math.toRadians(angle));
				float si = (float)Math.sin(Math.toRadians(angle));
				
				deltaX += Time.DELTA_TIME * 500 * co;
				deltaZ += Time.DELTA_TIME * 500 * si;

				hitRange.active(posX +deltaX + width * xPivot - 8  +64 * co, posZ + deltaZ + height * yPivot  - 8 + 64 * si, 16, 16);
			}
			
			break;
		}
		
	}
	
	public void Shoot(GameManager gm,GameScene gs, float _angle) {
		Projectile bullet = (Projectile)gs.getInactiveObject("bullet");
		float dX = (parent.getWidth() * 0.75f)* (float)Math.cos(Math.toRadians(_angle))  - 16;
		float dZ =  (parent.getHeight() * 0.75f )* (float)Math.sin(Math.toRadians(_angle)) ;
		
		if(bullet == null) {
			gs.addObject(new Projectile(posX + dX,posY,posZ + dZ,_angle,1000,10));
		}
		else {
			bullet.setting(posX + dX, posY, posZ + dZ, _angle, 1000, 10);
		}
	}
	// -------------------------------------------------------------------------
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
		switch(type) {
		case SWORD:
			imageName = "sword";
			width = 64;
			height = 64;
			xPivot = .5f;
			yPivot = .84f;
			distanceToParent = 18;
			break;
		case BOW:
			imageName = "bow";
			width = 64;
			height = 64;
			xPivot = .5f;
			yPivot = .5f;
			distanceToParent = 24;
			break;

		case SPEAR:
			imageName = "spear";
			width = 128;
			height = 64;
			xPivot = .5f;
			yPivot = .5f;
			distanceToParent = 24;
			break;
		}
		
		
	}
	
	public void setDstAngle(float dstAngle) {
		this.dstAngle = dstAngle;
	}

	public boolean isAttacking() {
		return attacking;
	}

}
