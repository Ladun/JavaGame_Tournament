package com.ladun.game.objects.projectile;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.components.CircleCollider;
import com.ladun.game.objects.Entity;
import com.ladun.game.objects.GameObject;

public class Bullet extends Projectile {
	
	public enum Type{ ARROW, STONE};
	
	private Type type;
	private int anim = 0;
	
	private float xPivot;
	private float yPivot;

	public Bullet(float posX, float posY, float posZ, float angle,float speed, float damage) {
		super(posX,posY,posZ,angle,speed, damage);
		this.tag = "bullet";
		
		this.width = 64;
		this.height = 64;
		Setting(Type.ARROW);
		
		this.addComponent(new CircleCollider(this));
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		offX += speed * c * dt;
		offZ += speed * s * dt;
		
		time += dt;
		if(time > 3) {
			this.active = false;
		}
		
		this.AdjustPosition();
		
		this.updateComponents(gc, gm, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		r.setzDepth((int)(posZ + Math.abs(posY) + height));
		//r.drawFillRect((int)posX, (int)posZ, width, height, angle, 0xff385610);
		r.drawImageTile((ImageTile)gc.getImageLoader().getImage("projectile"), (int)posX, (int)posZ, anim, 0, xPivot, yPivot, angle);
		
		r.drawRect((int)(posX + width * xPivot), (int)(posZ + height * yPivot), 1, 1, 0, 0xffff0000);
		
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		if(other instanceof Entity)
		{
			((Entity)other).Hit(damage);
			this.active = false;
		}
	}
	
	
	private void Setting(Type type) {
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

}
