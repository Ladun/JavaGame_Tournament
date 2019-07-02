package com.ladun.game;

import java.util.ArrayList;

import com.ladun.game.components.Collider;

public class Physics {

	public final static float GRAVITY = 9.8f; 
	public final static float MAX_HEIGHT = -1000;
	private static ArrayList<Collider> aabbList = new ArrayList<Collider>();
	
	public static void addAABBComponent(Collider aabb)
	{
		aabbList.add(aabb);
	}
	
	public static void update()
	{
		for(int i = 0; i < aabbList.size();i++)
		{
			for(int j = i + 1; j < aabbList.size();j++)
			{
				Collider c0 = aabbList.get(i);
				Collider c1 = aabbList.get(j);
				
				if(Math.abs(c0.getCenterX() - c1.getCenterX()) < c0.getHalfWidth() + c1.getHalfWidth())
				{
					if(Math.abs(c0.getCenterY() - c1.getCenterY()) < c0.getHalfHeight() + c1.getHalfHeight())
					{
						c0.getParent().collision(c1.getParent());
						c1.getParent().collision(c0.getParent());
					}
				}
			}
		}
		aabbList.clear();
	}
	
}
