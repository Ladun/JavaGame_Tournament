package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.components.CircleCollider;

public class TempObject extends GameObject{

	private int color = 0xff374529;
	private boolean chaseMouse;
	private GameScene gs;
	
	public TempObject(GameScene gs,boolean chaseMouse) {
		this.tag = "temp";
		this.gs = gs;
		this.width = 64;
		this.height = 64;
		this.chaseMouse = chaseMouse;
		this.addComponent(new CircleCollider(this));
		//this.addComponent(new RectCollider(this));
	}
	@Override
	public void update(GameContainer gc, GameManager gm) {
		
		color = 0xffff0000;
		if(chaseMouse) {
			this.posX = gc.getInput().getMouseX() + gs.getCamera().getOffX();
			this.posZ = gc.getInput().getMouseY() + gs.getCamera().getOffY();
		}
		this.updateComponents(gc, gm);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		
		r.drawFillCircle((int)posX + (width) / 2, (int)posZ + (height) / 2, 24, color);
		//r.drawFillRect((int)posX, (int)posZ, 24, 24, 0, color);
		
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		
		color = 0xff000000;
	}

}
