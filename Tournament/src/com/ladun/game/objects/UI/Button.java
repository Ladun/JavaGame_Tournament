package com.ladun.game.objects.UI;

import java.awt.event.MouseEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.objects.GameObject;

public class Button extends GameObject{

	private boolean over;
	private boolean pressed;
	private boolean released;	
	
	private int color;
	private String imageName;
	
	public Button(float x, float y,int width, int height, int color)
	{
		this.tag 		= "button";
		this.width 		= width;
		this.height 	= height;
		this.posX		= x;
		this.posY		= y;
		this.imageName 	= null;
		this.color 		= color;
	}
	
	public Button(float x, float y
			,int width, int height, String buttonImageName)
	{
		this.tag 		= "button";
		this.width 		= width;
		this.height 	= height;
		this.posX		= x;
		this.posY		= y;
		this.imageName 	= buttonImageName;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm) {
		over 			= isOver(gc.getInput().getMouseX(),gc.getInput().getMouseY());
		released 		= false;
		if(over)
		{
			if(gc.getInput().isButtonDown(MouseEvent.BUTTON1))
				pressed 	= true;
			else if(pressed && gc.getInput().isButtonUp(MouseEvent.BUTTON1)){
				pressed 	= false;
				released 	= true;
			}
		}
		if(pressed && gc.getInput().isButtonUp(MouseEvent.BUTTON1)){
			pressed 		= false;
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.setzDepth(Renderer.LAYER_UI);

		if(imageName == null) {
			if(pressed)
			{
				r.drawFillRect((int)posX, (int)posY, width, height, 0, color - 0x00151515);
			}
			else
			{
				if(over)
				{
					r.drawFillRect((int)posX, (int)posY, width, height, 0, color + 0x00151515);
				}
				else
				{
					r.drawFillRect((int)posX, (int)posY, width, height, 0, color);
				}
			}
		}
		else {
			if(pressed)
			{
				r.drawImage(((ImageTile)gc.getImageLoader().getImage(imageName)).getTileImage(2, 0), (int)posX,(int)posY,0);
			}
			else
			{
				if(over)
				{
					r.drawImage(((ImageTile)gc.getImageLoader().getImage(imageName)).getTileImage(1, 0), (int)posX,(int)posY,0);
				}
				else
				{
					r.drawImage(((ImageTile)gc.getImageLoader().getImage(imageName)).getTileImage(0, 0), (int)posX,(int)posY,0);
				}
			}
		}
	}

	@Override
	public void collision(GameObject other) {

		
	}

	
	boolean isOver(float x, float y)
	{
		return x >= posX && x <= posX + width && y >= posY && y <= posY + height;
	}

	public boolean isPressed() {
		return pressed;
	}

	public boolean isReleased() {
		return released;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

}
