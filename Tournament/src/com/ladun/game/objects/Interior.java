package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.components.CircleCollider;
import com.ladun.game.components.RectCollider;

public class Interior extends GameObject{

	
	private boolean animation;
	private float anim;
	private float animSpeed;
	private int animMaxIndex;
	
	private String imageName;
	private boolean collision;
	
	public Interior(String imageName,
			float posX,float posY,
			int width, int height,boolean collision) {
		
		this.tag = "interior";
		this.imageName = imageName;
		this.collision = collision;
		this.width = width;
		this.height = height;

		this.posX = posX;
		this.posY = posY;
		
		if(collision) {
			this.addComponent(new RectCollider(this));
		}
	}
	public Interior(String imageName,
			float posX,float posY,
			int radius,boolean collision) {
		
		this.tag = "interior";
		this.imageName = imageName;
		this.collision = true;
		this.width = radius;
		this.height = radius;

		this.posX = posX;
		this.posY = posY;

		if(collision) {
			this.addComponent(new CircleCollider(this));
		}
	}
	public Interior(String imageName,
			float posX,float posY,
			int width, int height,
			float animSpeed,int animMaxIndex,boolean collision) {

		this.tag = "interior";
		this.imageName = imageName;
		this.collision = false;
		
		this.posX = posX;
		this.posY = posY;
		
		
		this.animSpeed =animSpeed;
		this.animMaxIndex = animMaxIndex;
		this.animation = true;
		if(collision) {
			this.addComponent(new RectCollider(this));
		}
	}
	public Interior(String imageName,
			float posX,float posY,
			int radius,
			float animSpeed,int animMaxIndex,boolean collision) {

		this.tag = "interior";
		this.imageName = imageName;
		this.collision = false;
		
		this.posX = posX;
		this.posY = posY;
		
		
		this.animSpeed =animSpeed;
		this.animMaxIndex = animMaxIndex;
		this.animation = true;
		if(collision) {
			this.addComponent(new CircleCollider(this));
		}
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		
		if(animation) {
			anim += Time.DELTA_TIME * animSpeed;
			if(anim >= animMaxIndex)
				anim -= animMaxIndex;
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		r.setzDepth((int)posY + height);
		if(!animation) 
			r.drawImage(gc.getImageLoader().getImage(imageName), (int)posX	, (int)posY, angle);
		else{
			r.drawImageTile((ImageTile)gc.getImageLoader().getImage(imageName), (int)posX, (int)posY, (int)anim, 0, 0);
		}
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}

}
