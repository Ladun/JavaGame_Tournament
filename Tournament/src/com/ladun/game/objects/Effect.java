package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;

public class Effect extends GameObject{
	
	private float anim;
	private int animType;
	private int animMax;
	private float animTime;
	
	private String imageName;
	
	public Effect(String imageName, int animType, float posX,float posY, float posZ,float angle) {
		this.tag = "effect";
		setting(imageName,animType,posX,posY,posZ,angle);
	}

	@Override
	public void update(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		anim += Time.DELTA_TIME * animMax * animTime;
		if(anim >= animMax) {
			anim -= animMax;
			active =false;
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		r.drawImageTile((ImageTile)gc.getResourceLoader().getImage(imageName), (int)posX, (int)(posY + posZ), (int)anim, animType,angle);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}
	
	//------------------------------------------------------
	public void setting(String imageName, int animType,float posX,float posY, float posZ,float angle) {
		this.active = true;
		this.imageName = imageName;
		this.animType = animType;
		this.animMax = 4;
		this.animTime = 1 / .3f;
		this.anim = 0;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.angle = angle;
	}
	//------------------------------------------------------
	

}