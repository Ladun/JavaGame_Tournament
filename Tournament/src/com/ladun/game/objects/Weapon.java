package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.net.Client;
import com.ladun.game.objects.projectile.Bullet;

public class Weapon extends GameObject{
	public enum Type{
		SWORD,BOW
	}
	
	private Type type;
	private GameObject parent;
	
	private float anim;
	private int animType;
	private boolean attacking = false;
	
	private String imageName;
	private float xPivot;
	private float yPivot;
	
	public Weapon(GameObject parent) {
		this.parent = parent;
		setType(Type.SWORD);
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		posX = parent.posX;
		posZ = parent.posZ + parent.posY - parent.height * .3f;
		angle = parent.angle;
		
		if(Math.abs(angle) > 90) {
			angle = (angle + 180) % 360;
			posX -= parent.width / 4;
		}
		else {
			posX += parent.width / 4;
			
		}
		
		if(attacking) {
			anim+= dt * 28;
			if(anim > 6)
				attacking= false;
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		if(attacking)
			r.drawImageTile((ImageTile)gc.getImageLoader().getImage("weapon_attack"),(int)posX - parent.width / 2,(int)(posZ ),(int)anim,0,.5f, .45f,0);
		else
			r.drawImage(gc.getImageLoader().getImage(imageName),(int)posX,(int)(posZ ),xPivot, yPivot,0);
		
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}
	
	public void Attack(GameManager gm, GameScene gs) {
		switch(type) {
		case SWORD:
			attacking = true;
			anim = 0;
			break;
		case BOW:
			Shoot(gm,gs);
			break;
		}
		
	}
	
	public void Shoot(GameManager gm,GameScene gs) {
		Bullet bullet = (Bullet)gs.getInactiveObject("bullet");
		if(bullet == null) {
			gs.addObject(new Bullet(posX,posY,posZ,angle,1000,1));
		}
		else {
			bullet.setting(posX, posY, posZ, angle, 1000, 1);
		}
		
		if(gm != null) {
			if(parent instanceof Player)
				if(((Player)parent).isLocalPlayer()) {
					gm.getClient().send(Client.PACKET_TYPE_OBJECTSPAWN,new Object[] {"bullet"});
				}
		}
	}
	//---------------------------------------------------------
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
		switch(type) {
		case SWORD:
			imageName = "sword";
			xPivot = .5f;
			yPivot = .84f;
			break;
		case BOW:
			imageName = "bow";
			xPivot = .5f;
			yPivot = .84f;
			break;
		}
		
	}

}
