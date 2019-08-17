package com.ladun.game.components;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Physics;
import com.ladun.game.objects.GameObject;

public class RectCollider extends Collider{

	private int centerX,centerZ;
	private int halfWidth, halfHeight; // xy
	private int lastCenterX, lastCenterZ;
	
	public RectCollider(GameObject parent) {
		super(parent);
		this.tag = "RectCollider";
		this.type = Type.RECT;
	}

	@Override
	public void update(GameContainer gc, GameManager gm) {
		lastCenterX = centerX;
		lastCenterZ = centerZ;

		halfWidth = ((parent.getWidth() - parent.getpL() - parent.getpR())/ 2);
		halfHeight = ((parent.getHeight() -parent.getpB() - parent.getpT())/2);
		centerX = (int)(parent.getPosX()) + halfWidth + parent.getpL();
		centerZ = (int)(parent.getPosZ()) + halfHeight + parent.getpT();
		

		Physics.colliderComponent(this);
	}
	@Override
	public void render(GameContainer gc,  Renderer r) {
		// TODO Auto-generated method stub
		//r.drawRect(centerX - halfWidth, centerZ - halfHeight, halfWidth * 2, halfHeight * 2,0, 0xffa6ec90);
		
	}
	//-----------------------------------------------

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
	public int getHalfWidth() {
		return halfWidth;
	}
	public void setHalfWidth(int halfWidth) {
		this.halfWidth = halfWidth;
	}
	public int getHalfHeight() {
		return halfHeight;
	}
	public void setHalfHeight(int halfHeight) {
		this.halfHeight = halfHeight;
	}
}
