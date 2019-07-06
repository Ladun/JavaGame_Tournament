package com.ladun.game.objects.projectile;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.objects.GameObject;

public class Bullet extends Projectile {

	public Bullet(float posX, float posY, float posZ, float angle,float speed, float damage) {
		super(posX,posY,posZ,angle,speed, damage);
		this.tag = "bullet";
		
		this.width = 40;
		this.height = 10;
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
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		r.drawFillRect((int)posX, (int)posZ, width, height, angle, 0xff385610);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}

}
