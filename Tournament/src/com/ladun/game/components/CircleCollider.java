package com.ladun.game.components;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Physics;
import com.ladun.game.objects.GameObject;

public class CircleCollider extends Collider{

	private int radius;
	
	
	public CircleCollider(GameObject parent) {
		super(parent);
		this.tag = "circleCollider";
		this.type = Type.CIRCLE;
	}

	@Override
	public void update(GameContainer gc, GameManager gm) {
		
		
		positionUpdate(gc, gm);

		Physics.colliderComponent(this);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		if(GameManager.DEBUG)
			r.drawCircle(centerX, centerZ + y, radius, 0xffa6ec90);
		
	}
	//---------------------------------------------------------------

	public void positionUpdate(GameContainer gc, GameManager gm) {
		lastCenterX = centerX;
		lastCenterZ = centerZ;

		radius = ((parent.getWidth() - parent.getpL() - parent.getpR())/ 2);
		hY = radius;
		centerX = (int)(parent.getPosX()) + ((parent.getWidth() - parent.getpL() - parent.getpR())/ 2) + parent.getpL();
		centerZ = (int)(parent.getPosZ()) + ((parent.getHeight() -parent.getpB() - parent.getpT())/2) + parent.getpT();
		y = (int)parent.getPosY();
	}
	
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

}
