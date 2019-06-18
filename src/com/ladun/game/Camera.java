package com.ladun.game;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.objects.GameObject;

public class Camera {
	
	private float offX,offY;
	
	private String targetTag;
	private GameObject target = null;
	
	private float cameraSensitivity = 20;
	
	public Camera(GameManager gm,String tag)
	{
		this.targetTag = tag;
		this.target = gm.getObject(targetTag);
	}
	
	public void update(GameContainer gc,GameManager gm,float dt)
	{
		if(target == null)
			return;
		
		float targetX = (target.getPosX() + target.getWidth() / 2) - gc.getWidth() / 2;
		float targetY = (target.getPosY() + target.getHeight() / 2) - gc.getHeight() /2;
		
		offX = targetX;
		offY = targetY;
		
		//offX -= dt * (int)(offX - targetX) * cameraSensitivity;
		//offY -= dt * (int)(offY - targetY) * cameraSensitivity;
		
		
		
		if(offX < 0) offX = 0;
		if(offX >gm.getActiveScene().getLevelW() * GameManager.TS- gc.getWidth()) offX = gm.getActiveScene().getLevelW()* GameManager.TS - gc.getWidth();
		if(offY < 0) offY = 0;
		if(offY > gm.getActiveScene().getLevelH()* GameManager.TS - gc.getHeight()) offY = gm.getActiveScene().getLevelH()* GameManager.TS- gc.getHeight();


		
	}
	
	public void render(Renderer r)
	{
		r.setCamX((int)offX);
		r.setCamY((int)offY);
	}

	public float getOffX() {
		return offX;
	}

	public void setOffX(float offX) {
		this.offX = offX;
	}

	public float getOffY() {
		return offY;
	}

	public void setOffY(float offY) {
		this.offY = offY;
	}

	public String getTargetTag() {
		return targetTag;
	}

	public void setTargetTag(String targetTag) {
		this.targetTag = targetTag;
	}

	public GameObject getTarget() {
		return target;
	}

	public void setTarget(GameObject target) {
		this.target = target;
	}
	
}
