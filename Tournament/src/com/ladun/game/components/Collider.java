package com.ladun.game.components;

import com.ladun.game.objects.GameObject;

public abstract class Collider extends Component{
	
	public enum Type{
		RECT, CIRCLE
	}
	
	protected int hY;
	protected int y;
	protected int lastCenterX, lastCenterZ;
	protected int centerX,centerZ;
	
	protected Type type;
	
	public Collider(GameObject parent)
	{
		this.parent = parent;
		
	}
	
	public float getAngle() {
		return parent.getAngle();
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	public int gethY() {
		return hY;
	}

	public int getY() {
		return y;
	}

}
