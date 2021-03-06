package com.ladun.game.objects.interior;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Light;
import com.ladun.game.GameManager;
import com.ladun.game.components.CircleCollider;
import com.ladun.game.components.RectCollider;
import com.ladun.game.objects.GameObject;

public class Interior extends GameObject{

	
	
	
	protected String imageName;
	protected boolean collision;
	protected boolean mirror;
	
	protected Light light;
	protected boolean lighting = false;

	public Interior(String imageName,
			float posX,float posZ,
			int width, int height,int hY,
			boolean collision) {
		this(imageName,posX,posZ,width,height,hY,collision,false);
	}
	public Interior(String imageName,
			float posX,float posZ,
			int width, int height, int hY,
			boolean collision,boolean mirror) {
		
		this.tag = "interior";
		this.imageName = imageName;
		this.collision = collision;
		this.mirror = mirror;
		this.width = width;
		this.hY = hY;
		this.height = height;

		this.posX = posX;
		this.posZ = posZ;
		
		if(collision) {
			this.addComponent(new RectCollider(this));
		}
	}
	public Interior(String imageName,
			float posX,float posZ,
			int radius,
			boolean collision) {
		this(imageName,posX,posZ,radius,collision,false);
	}
	public Interior(String imageName,
			float posX,float posZ,
			int radius,
			boolean collision,boolean mirror) {
		
		this.tag = "interior";
		this.imageName = imageName;
		this.collision = collision;
		this.mirror = mirror;
		this.width = radius;
		this.height = radius;
		this.hY = radius;

		this.posX = posX;
		this.posZ = posZ;

		if(collision) {
			this.addComponent(new CircleCollider(this));
		}
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm) {	
		this.updateComponents(gc, gm);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		r.setzDepth((int)posZ + height - pB);
		r.drawImage(gc.getResourceLoader().getImage(imageName), (int)posX	, (int)posZ,mirror,false, angle);	
		if(lighting)
			r.drawLight(light, (int)getCenterX(), (int)getCenterZ());
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
	}
	
	public Interior setLight(Light light) {
		this.light = light;
		this.lighting = true;
		
		return this;
	}

}
