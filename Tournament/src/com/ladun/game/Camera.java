package com.ladun.game;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.objects.GameObject;
import com.ladun.game.util.Util;

public class Camera {
	
	private final int SCREEN_MOVE_GAP = 15;
	private final int SHAKE_DISTANCE = 30;
	
	private float offX,offZ;
	
	private String targetTag;
	private GameObject target = null;
	private boolean characterLock = false;
	private boolean mouseLock = false;
	
	private boolean cameraShaking;	
	private float shakeX, shakeY;
	private float targetShakeX, targetShakeY;
	private float lastShakeX, lastShakeY;
	private float shakePercent = 0;	
	private float shakeAngle = 0;
	private float moveDistance = SHAKE_DISTANCE;
	private float shakeTime = .3f;
	
	private float cameraSensitivity = 400;
	
	private GameScene gs;
	private Robot robot;
	
	public Camera(GameManager gm,GameScene gs,String tag) throws AWTException
	{
		this.robot = new Robot();
		this.targetTag = tag;
		this.target = gm.getObject(targetTag);
		this.gs = gs;
	}
	
	public void update(GameContainer gc,GameManager gm)
	{
		if((gc.getInput().isKey(KeyEvent.VK_SPACE) &&!gs.isChatting())|| characterLock) {			
			focusTarget(gc,gm);						
		}
		
		if(!gs.isChatting()) {
			if(gc.getInput().isKeyDown(KeyEvent.VK_Y))
				characterLock = !characterLock;
			if(gc.getInput().isKeyDown(KeyEvent.VK_L))
				mouseLock = !mouseLock;
		}		
		if(mouseLock) {
			lockTheMouse(gc,gm);
		}	

		// Camera Position Setting
		positionClamp(gc,gm);
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_SPACE))
			cameraShake();

		if(cameraShaking) {
			shakePercent += Time.DELTA_TIME * 25;
			if(shakePercent >= 1) {
				lastShakeX = targetShakeX;
				lastShakeY = targetShakeY;
				shakePercent = 0;
				setShakePos();
			}			
			shakeX = Util.lerp(lastShakeX, targetShakeX, shakePercent);
			shakeY = Util.lerp(lastShakeY, targetShakeY, shakePercent);
			if(moveDistance <= 1) {
				cameraShaking = false;
				shakeX = 0;
				shakeY = 0;
			}
		}
	}
	
	public void render(Renderer r)
	{
		r.setCamX((int)(offX + shakeX));
		r.setCamY((int)(offZ + shakeY));
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
	private void setShakePos() {
		shakeAngle = shakeAngle + 180 + new Random().nextInt(91) - 45;
		targetShakeX = moveDistance * (float)Math.cos(shakeAngle * Math.toRadians(shakeAngle));
		targetShakeY = moveDistance * (float)Math.sin(shakeAngle * Math.toRadians(shakeAngle));
		
		moveDistance *= 0.5f;
	}
	
	private void positionClamp(GameContainer gc, GameManager gm) {
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
	private void lockTheMouse(GameContainer gc,GameManager gm) {
		/*
		System.out.println("Mosue Pos : " + gc.getInput().getMouseDeltaX() + ", " +gc.getInput().getMouseDeltaY() + 
				" MosueInfo Pos : " + MouseInfo.getPointerInfo().getLocation().x + ", " + MouseInfo.getPointerInfo().getLocation().y+
				" Window Pos : " + gc.getWindow().getFrame().getLocation().x + ", " + gc.getWindow().getFrame().getLocation().y + 
				" Other : " + gc.getWindow().getFrame().getInsets().top+", "+ gc.getWindow().getFrame().getInsets().bottom);
		*/
		if(MouseInfo.getPointerInfo().getLocation().x  + gc.getInput().getMouseDeltaX()< gc.getWindow().getFrame().getLocation().x + gc.getWindow().getFrame().getInsets().left) {
			robot.mouseMove(gc.getWindow().getFrame().getLocation().x + gc.getWindow().getFrame().getInsets().left,
					        MouseInfo.getPointerInfo().getLocation().y);
		}
		else if(MouseInfo.getPointerInfo().getLocation().x + gc.getInput().getMouseDeltaX() > gc.getWindow().getFrame().getLocation().x + gc.getWindow().getFrame().getInsets().left + gc.getWidth()) {
			robot.mouseMove(gc.getWindow().getFrame().getLocation().x + gc.getWindow().getFrame().getInsets().left + gc.getWidth(), 
							MouseInfo.getPointerInfo().getLocation().y);
		}
		if(MouseInfo.getPointerInfo().getLocation().y + gc.getInput().getMouseDeltaY()< gc.getWindow().getFrame().getLocation().y + gc.getWindow().getFrame().getInsets().top) {
			robot.mouseMove(MouseInfo.getPointerInfo().getLocation().x ,
							gc.getWindow().getFrame().getLocation().y + gc.getWindow().getFrame().getInsets().top);
		}
		else if(MouseInfo.getPointerInfo().getLocation().y  + gc.getInput().getMouseDeltaY()>gc.getWindow().getFrame().getLocation().y + gc.getWindow().getFrame().getInsets().top + gc.getHeight()) {
			robot.mouseMove(MouseInfo.getPointerInfo().getLocation().x ,
					gc.getWindow().getFrame().getLocation().y + gc.getWindow().getFrame().getInsets().top + gc.getHeight());
		}
		// Camera Move by Mouse
		if(!characterLock) {
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
		}	
	}
	
	public void cameraShake() {
		cameraShaking = true;
		moveDistance = SHAKE_DISTANCE;
		shakePercent = 0;
		setShakePos();
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
		this.target = null;
	}

	public GameObject getTarget() {
		return target;
	}

	public void setTarget(GameObject target) {
		this.target = target;
	}
	
}
