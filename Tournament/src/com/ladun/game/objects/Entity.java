package com.ladun.game.objects;

import com.ladun.game.GameManager;
import com.ladun.game.Scene.GameScene;

public abstract class Entity extends GameObject{
	
	protected GameScene gs;
	protected float maxHealth;
	protected float health;
	protected float speed;
	protected float jump;
	protected float fallDistance;
	
	protected float groundHeight; // ������Ʈ�� ���ִ� ���� ����
	
	protected int tileX,tileZ;
	protected float offX,offZ;
	
	protected boolean ground = false;
	protected boolean groundLast = false;
	protected boolean moving = false;
	
	protected float nextHitTime = 0;
	
	protected void AdjustPosition()
	{
		//Final position
		if(offZ > GameManager.TS / 2)
		{
			tileZ++;
			offZ -= GameManager.TS;
		}
		if(offZ <  -GameManager.TS / 2)
		{
			tileZ--;
			offZ += GameManager.TS;
		}
		
		if(offX > GameManager.TS / 2)
		{
			tileX++;
			offX -= GameManager.TS;
		}
		if(offX <  -GameManager.TS / 2)
		{
			tileX--;
			offX += GameManager.TS;
		}
		//--

		posX = tileX * GameManager.TS + offX;
		posZ = tileZ * GameManager.TS + offZ;
	}
}