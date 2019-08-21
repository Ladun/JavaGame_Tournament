package com.ladun.game.components;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.game.GameManager;
import com.ladun.game.objects.GameObject;

public class Rigidbody extends Component{

	private float power;
	private float angle;
	
	
	private float c,s;
	
	public Rigidbody(GameObject parent) {
		this.tag = "rigidbody";
		this.parent = parent;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		if(power != 0) {
			parent.addPosX((float)(power * c * Time.DELTA_TIME));
			parent.addPosZ((float)(power * s * Time.DELTA_TIME));
		}
		if(power > 0) {
			power -= 500 * Time.DELTA_TIME;
			if(power < 0) {
				power = 0;
			}
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void addPower(float power) {
		
	}
	public void addPower(float power, float angle) {
		this.power = power;
		this.angle = angle;
		this.c = (float)Math.cos(Math.toRadians(angle));
		this.s = (float)Math.sin(Math.toRadians(angle));
	}
	public void addForce(float x, float y) {
		
	}

}