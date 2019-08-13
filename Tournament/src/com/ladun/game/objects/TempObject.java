package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Scene.GameScene;
import com.ladun.game.components.CircleCollider;

public class TempObject extends GameObject{

	private int color = 0xff374529;
	private GameScene gs;
	
	public TempObject(GameScene gs) {
		this.tag = "temp";
		this.gs = gs;
		this.width = 64;
		this.height = 64;
		this.addComponent(new CircleCollider(this));
		//this.addComponent(new RectCollider(this));
	}
	@Override
	public void update(GameContainer gc, GameManager gm) {
		// TODO Auto-generated method stub
		color = 0xffff0000;
		
		this.posX = gc.getInput().getMouseX() + gs.getCamera().getOffX();
		this.posZ = gc.getInput().getMouseY() + gs.getCamera().getOffY();
		this.updateComponents(gc, gm);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		r.drawFillCircle((int)posX + (width) / 2, (int)posZ + (height) / 2, 24, color);
		//r.drawFillRect((int)posX, (int)posZ, 24, 24, 0, color);
		
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		color = 0xff000000;
	}

}
