package com.ladun.game.components;

import com.ladun.game.objects.GameObject;

public abstract class Collider extends Component{
	
	public enum Type{
		RECT, CIRCLE
	}
	
	protected Type type;
	
	public Collider(GameObject parent)
	{
		this.parent = parent;
		
	}
	
	public float getAngle() {
		return parent.getAngle();
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}