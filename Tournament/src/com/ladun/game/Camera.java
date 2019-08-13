package com.ladun.game;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.objects.GameObject;

public class Camera {
	
	private float offX,offZ;
	
	private String targetTag;
	private GameObject target = null;
	
	private float cameraSensitivity = 20;
	
	public Camera(GameManager gm,String tag)
	{
		this.targetTag = tag;
		this.target = gm.getObject(targetTag);
	}
	
	public void update(GameContainer gc,GameManager gm)
	{
		if(target == null) {
			this.target = gm.getObject(targetTag);
			return;
		}

		float targetX = (target.getPosX() + target.getWidth() / 2) - gc.getWidth() / 2;
		float targetZ = (target.getPosZ() + target.getHeight() / 2 ) - gc.getHeight() /2;
		
		offX = targetX;
		offZ = targetZ;
		
		//offX -= dt * (int)(offX - targetX) * cameraSensitivity;
		//offY -= dt * (int)(offY - targetY) * cameraSensitivity;
		
		
		if(gm.getActiveScene().getLevelW() * GameManager.TS < gc.getWidth()) {
			offX =-( gc.getWidth() - gm.getActiveScene().getLevelW() * GameManager.TS )/2;
		}else {
			if(offX >gm.getActiveScene().getLevelW() * GameManager.TS- gc.getWidth()) offX = gm.getActiveScene().getLevelW()* GameManager.TS - gc.getWidth();
			if(offX < 0) offX = 0;
		}
		if(gm.getActiveScene().getLevelH() * GameManager.TS < gc.getHeight()) {
			offZ =-( gc.getHeight() - gm.getActiveScene().getLevelH() * GameManager.TS )/2;		
		}else {
			if(offZ > gm.getActiveScene().getLevelH()* GameManager.TS - gc.getHeight()) offZ = gm.getActiveScene().getLevelH()* GameManager.TS- gc.getHeight();
			if(offZ < 0) offZ = 0;
		}

		
	}
	
	public void render(Renderer r)
	{
		r.setCamX((int)offX);
		r.setCamY((int)offZ);
	}

	public float getOffX() {
		return offX;
	}

	public void setOffX(float offX) {
		this.offX = offX;
	}

	public float getOffY() {
		return offZ;
	}

	public void setOffY(float offY) {
		this.offZ = offY;
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
