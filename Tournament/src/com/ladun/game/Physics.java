package com.ladun.game;

import java.util.ArrayList;

import com.ladun.game.components.CircleCollider;
import com.ladun.game.components.Collider;
import com.ladun.game.components.Collider.Type;
import com.ladun.game.components.RectCollider;

public class Physics {

	public final static float GRAVITY = 9.8f; 
	public final static float MAX_HEIGHT = -1000;
	private static ArrayList<Collider> colliders = new ArrayList<Collider>();
	
	public static void colliderComponent(Collider aabb)
	{
		colliders.add(aabb);
	}
	
	public static void update()
	{
		for(int i = 0; i < colliders.size();i++)
		{
			Collider c0 = colliders.get(i);
			if(!c0.getParent().isActive())
				continue;
			
			for(int j = i + 1; j < colliders.size();j++)
			{
				Collider c1 = colliders.get(j);
				if(!c1.getParent().isActive())
					continue;

				
				switch(c0.getType()) {
				case RECT:
					if(c1.getType() == Type.CIRCLE) {
						if(RectCircleCollision((RectCollider)c0, (CircleCollider)c1))
						{
								c0.getParent().collision(c1.getParent());
								c1.getParent().collision(c0.getParent());

						}
					}
					else if(c1.getType() == Type.RECT) {
						if(AABBCollision((RectCollider)c0, (RectCollider)c1) )
						{
								c0.getParent().collision(c1.getParent());
								c1.getParent().collision(c0.getParent());
							
						}
					}
					break;
					
				case CIRCLE:
					if(c1.getType() == Type.CIRCLE) {
						if(CircleCollision((CircleCollider)c0, (CircleCollider)c1))
						{
								c0.getParent().collision(c1.getParent());
								c1.getParent().collision(c0.getParent());

						}
					}
					else if(c1.getType() == Type.RECT) {
						if(RectCircleCollision((RectCollider)c1, (CircleCollider)c0) )
						{
								c0.getParent().collision(c1.getParent());
								c1.getParent().collision(c0.getParent());
							
						}
					}
					
					break;
					
				}
			}
		}
		colliders.clear();
	}
	
	private static boolean RectCircleCollision(RectCollider c0, CircleCollider c1) {
		int l = c0.getCenterX() - c0.getHalfWidth();
		int b = c0.getCenterZ() + c0.getHalfHeight();
		int r = c0.getCenterX() + c0.getHalfWidth();
		int t = c0.getCenterZ() - c0.getHalfHeight();
		
		

		if(Math.abs(c0.getY() - c1.getY()) <= c0.gethY() + c1.gethY()) {
			if((c1.getCenterX() >= l &&c1.getCenterX() <= r) ||(c1.getCenterZ() >= t &&c1.getCenterZ() <= b) ) {
				if(Math.abs(c0.getCenterX() - c1.getCenterX()) <= c0.getHalfWidth() + c1.getRadius()){
					if(Math.abs(c0.getCenterZ() - c1.getCenterZ()) <= c0.getHalfHeight() + c1.getRadius())	{
							return true;
					}
				}
			}
			else {
				if(InCircle(c1.getCenterX(),c1.getCenterZ(),c1.getRadius(),l,b))
					return true;
				if(InCircle(c1.getCenterX(),c1.getCenterZ(),c1.getRadius(),l,t))
					return true;
				if(InCircle(c1.getCenterX(),c1.getCenterZ(),c1.getRadius(),r,b))
					return true;
				if(InCircle(c1.getCenterX(),c1.getCenterZ(),c1.getRadius(),r,t))
					return true;
			}
		}
		
		return false;
		
	}
	
	private static boolean AABBCollision(RectCollider c0, RectCollider c1) {
		if(Math.abs(c0.getCenterX() -c1.getCenterX()) <= c0.getHalfWidth() + c1.getHalfWidth())	{
			if(Math.abs(c0.getCenterZ() - c1.getCenterZ()) <= c0.getHalfHeight() + c1.getHalfHeight())	{
				if(Math.abs(c0.getY() - c1.getY()) <= c0.gethY() + c1.gethY())
					return true;
			}
		}
		return false;
	}
	
	private static boolean CircleCollision(CircleCollider c0, CircleCollider c1) {
		
		int dx = c0.getCenterX() - c1.getCenterX();
		int dy = c0.getCenterZ() - c1.getCenterZ();
		int dr = c0.getRadius() + c1.getRadius();
		
		if(dx * dx +dy * dy < dr*dr)
			return true;
		
		
		return false;
	}
	
	private static boolean InCircle(int cx, int cy, int cr,int x,int y) {
		int dx = cx - x;
		int dy = cy - y;
		
		if( dx * dx + dy * dy <= cr * cr)
			return true;
		return false;
	}
}
