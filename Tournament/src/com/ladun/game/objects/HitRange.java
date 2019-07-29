package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.components.CircleCollider;
import com.ladun.game.components.RectCollider;

public class HitRange extends GameObject{

	public enum Type{ RECT,CIRCLE}
	
	private GameObject parent;
	private Type type;
	
	public HitRange(GameObject parent,Type t) {
		this.tag = "hitRange";
		this.parent = parent;
		this.active = false;
		type = t;
		
		switch(t) {
		case RECT:
			this.addComponent(new RectCollider(this));
			break;
		case CIRCLE:
			this.addComponent(new CircleCollider(this));			
			break;
		}
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		this.updateComponents(gc, gm, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		
		
	}
	public void active(float x,float y)
	{
		this.posX 		= x;
		this.posZ 		= y;
		this.active 	= true;
	}
	
	public void active(float x,float y,int width, int height)
	{
		this.active(x,y);
		this.width 		= width;
		this.height		= height; 
	}
	
	public GameObject getParent() {
		return parent;
	}
	
}