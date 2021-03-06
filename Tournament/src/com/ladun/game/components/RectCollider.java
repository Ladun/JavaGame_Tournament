package com.ladun.game.components;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Physics;
import com.ladun.game.objects.GameObject;

public class RectCollider extends Collider{

	private int halfWidth, halfHeight; // xy
	
	public RectCollider(GameObject parent) {
		super(parent);
		this.tag = "rectCollider";
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
		y = (int)parent.getPosY();
		hY = parent.gethY();

		Physics.colliderComponent(this);
	}
	@Override
	public void render(GameContainer gc,  Renderer r) {
		
		//r.setzDepth(Renderer.LAYER_UI - 1);
		if(GameManager.DEBUG)
			r.drawRect(centerX - halfWidth, centerZ - halfHeight + y, halfWidth * 2, halfHeight * 2,0, 0xffa6ec90);
		
	}
	//-----------------------------------------------

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
