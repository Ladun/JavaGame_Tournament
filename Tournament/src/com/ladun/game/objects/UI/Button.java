package com.ladun.game.objects.UI;

import java.awt.event.MouseEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.objects.GameObject;

public class Button {
	
	private int posX;
	private int posY;
	private int width;
	private int height;

	private boolean over;
	private boolean pressed;
	private boolean released;	
	
	private int color;
	private String imageName;
	
	public Button(int x, int y,int width, int height, int color)
	{
		this.width 		= width;
		this.height 	= height;
		this.posX		= x;
		this.posY		= y;
		this.imageName 	= null;
		this.color 		= color;
	}
	
	public Button(int x, int y
			,int width, int height, String buttonImageName)
	{
		this.width 		= width;
		this.height 	= height;
		this.posX		= x;
		this.posY		= y;
		this.imageName 	= buttonImageName;
	}
	
	public void update(GameContainer gc, GameManager gm) {
		over 			= isOver(gc.getInput().getMouseX(),gc.getInput().getMouseY());
		released 		= false;
		if(over)
		{
			//System.out.println(posX + ", " + posY + ", " + (width) + ", " + (height) + ", "+ gc.getInput().getMouseX()+", "+gc.getInput().getMouseY());
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
				r.drawImage(((ImageTile)gc.getResourceLoader().getImage(imageName)).getTileImage(2, 0), (int)posX,(int)posY,0);
			}
			else
			{
				if(over)
				{
					r.drawImage(((ImageTile)gc.getResourceLoader().getImage(imageName)).getTileImage(1, 0), (int)posX,(int)posY,0);
				}
				else
				{
					r.drawImage(((ImageTile)gc.getResourceLoader().getImage(imageName)).getTileImage(0, 0), (int)posX,(int)posY,0);
				}
			}
		}
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

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getCenterX() {
		return posX + width / 2;
	}

	public void setCenterX(int posX) {
		this.posX = posX - width / 2;
	}

	public int getCenterY() {
		return posY + height / 2;
	}

	public void setCenterY(int posY) {
		this.posY = posY - height / 2;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
