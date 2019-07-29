package com.ladun.game.components;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Physics;
import com.ladun.game.objects.GameObject;

public class CircleCollider extends Collider{

	private int centerX,centerZ;
	private int radius;
	private int lastCenterX, lastCenterZ;
	
	
	public CircleCollider(GameObject parent) {
		super(parent);
		this.tag = "circleCollider";
		this.type = Type.CIRCLE;
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		lastCenterX = centerX;
		lastCenterZ = centerZ;

		radius = ((parent.getWidth() - parent.getpL() - parent.getpR())/ 2);
		centerX = (int)(parent.getPosX()) + ((parent.getWidth() - parent.getpL() - parent.getpR())/ 2) + parent.getpL();
		centerZ = (int)(parent.getPosZ()) + ((parent.getHeight() -parent.getpB() - parent.getpT())/2) + parent.getpT();
		

		Physics.colliderComponent(this);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		r.drawCircle(centerX, centerZ, radius, 0xffa6ec90);
		
	}
	//---------------------------------------------------------------
	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterZ() {
		return centerZ;
	}

	public void setCenterZ(int centerZ) {
		this.centerZ = centerZ;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getLastCenterX() {
		return lastCenterX;
	}

	public void setLastCenterX(int lastCenterX) {
		this.lastCenterX = lastCenterX;
	}

	public int getLastCenterZ() {
		return lastCenterZ;
	}

	public void setLastCenterZ(int lastCenterZ) {
		this.lastCenterZ = lastCenterZ;
	}

}