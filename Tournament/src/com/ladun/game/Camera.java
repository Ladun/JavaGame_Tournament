package com.ladun.game;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.game.objects.GameObject;

public class Camera {
	
	private final int SCREEN_MOVE_GAP = 15;
	
	private float offX,offZ;
	
	private String targetTag;
	private GameObject target = null;
	private boolean characterLock = false;
	
	private float cameraSensitivity = 400;
	
	private Robot robot;
	
	public Camera(GameManager gm,String tag) throws AWTException
	{
		this.robot = new Robot();
		this.targetTag = tag;
		this.target = gm.getObject(targetTag);
	}
	
	public void update(GameContainer gc,GameManager gm)
	{

		if(gc.getInput().isKey(KeyEvent.VK_SPACE) || characterLock) {
			
			focusTarget(gc,gm);
						
		}
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_Y))
			characterLock = !characterLock;
		
		if(gc.getInput().getMouseX() < SCREEN_MOVE_GAP) {
			offX -= Time.DELTA_TIME * cameraSensitivity;
		}
		else if(gc.getInput().getMouseX() > gc.getWidth()- SCREEN_MOVE_GAP) {
			offX += Time.DELTA_TIME * cameraSensitivity;
		}
		if(gc.getInput().getMouseY() < SCREEN_MOVE_GAP) {
			offZ -= Time.DELTA_TIME * cameraSensitivity;
		}
		else if(gc.getInput().getMouseY() > gc.getHeight()- SCREEN_MOVE_GAP) {
			offZ += Time.DELTA_TIME * cameraSensitivity;
		}
		
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
	
	public void focusTarget(GameContainer gc,GameManager gm) {
		//System.out.println(targetTag);
		if(target == null) {
			this.target = gm.getObject(targetTag);
			if(target == null)
				return;
		}
		float targetX = (target.getPosX() + target.getWidth() / 2) - gc.getWidth() / 2;
		float targetZ = (target.getPosZ() + target.getHeight() / 2 ) - gc.getHeight() /2;
		
		offX = targetX;
		offZ = targetZ;
		//offX -= dt * (int)(offX - targetX) * cameraSensitivity;
		//offY -= dt * (int)(offY - targetY) * cameraSensitivity;
	}
	//---------------------------------------------------------------------

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
