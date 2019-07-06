package com.ladun.game.components;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Physics;
import com.ladun.game.objects.GameObject;

public class RectCollider extends Component{
	
	private int centerX,centerY;
	private int halfWidth, halfHeight;
	private int lastCenterX, lastCenterY;
	
	public RectCollider(GameObject parent)
	{
		this.parent = parent;
		this.tag = "aabb";
		
	}
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		lastCenterX = centerX;
		lastCenterY = centerY;

		halfWidth = ((parent.getWidth() - parent.getpL() - parent.getpR())/ 2);
		halfHeight = ((parent.getHeight() -parent.getpB() - parent.getpT())/2);
		centerX = (int)(parent.getPosX()) + halfWidth + parent.getpL();
		centerY = (int)(parent.getPosY()) + halfHeight + parent.getpT();
		

		Physics.addAABBComponent(this);
	}
	@Override
	public void render(GameContainer gc,  Renderer r) {
		// TODO Auto-generated method stub
		//r.drawRect(centerX - halfWidth, centerY - halfHeight, halfWidth * 2, halfHeight * 2, 0xff000000);
		
	}
	
	public float getAngle() {
		return parent.getAngle();
	}
	public int getLastCenterX() {
		return lastCenterX;
	}
	public void setLastCenterX(int lastCenterX) {
		this.lastCenterX = lastCenterX;
	}
	public int getLastCenterY() {
		return lastCenterY;
	}
	public void setLastCenterY(int lastCenterY) {
		this.lastCenterY = lastCenterY;
	}
	public int getCenterX() {
		return centerX;
	}
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}
	public int getCenterY() {
		return centerY;
	}
	public void setCenterY(int centerY) {
		this.centerY = centerY;
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