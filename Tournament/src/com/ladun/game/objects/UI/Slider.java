package com.ladun.game.objects.UI;

import java.awt.event.MouseEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;

public class Slider{
	
	private int			preMousePosX = 0;

	private int posX;
	private int posY;
	private int width;
	private int height;
	
	private int backgroundColor;	
	private Button 		slider;
	
	private boolean pressed;
	
	public Slider( int x, int y,
			int width, int height,int sliderWidth,float percent,
			int sliderColor, int backgroundColor)
	{
		this.width 				= width;
		this.height 			= height;
		this.posX				= x;
		this.posY				= y;
		this.slider 			= new Button( (int)(x + (width - sliderWidth) * percent) , y,sliderWidth,height, sliderColor);

		this.backgroundColor = backgroundColor;
	}

	public void update(GameContainer gc,GameManager gm) {
		slider.update(gc, gm);
		pressed = false;
		if(slider.isPressed())
		{
			int p = (gc.getInput().getMouseX() - preMousePosX);
			if(inSlider(slider.getPosX() + p))
			{
				slider.setPosX(slider.getPosX() + p);
			}
			else {
				if(slider.getPosX() < posX)
				{
					slider.setPosX(posX);
				}
				else if(slider.getPosX() > posX + width - slider.getWidth())
				{
					slider.setPosX(posX + width - slider.getWidth());
				}
			}
		}
		if(gc.getInput().isButtonDown(MouseEvent.BUTTON1)) {
			if(inSlider(gc.getInput().getMouseX()- slider.getWidth() / 2) &&
					gc.getInput().getMouseY() >= posY && gc.getInput().getMouseY() <= posY + height){
				slider.setPosX(gc.getInput().getMouseX() - slider.getWidth() / 2);
				pressed = true;
			}
		}
		preMousePosX = gc.getInput().getMouseX();
	}


	public void render(GameContainer gc, Renderer r) {		
		r.drawFillRect(posX, posY, width, height, 0, backgroundColor);
		slider.render(gc, r);
	}
	
	public float getPercent()
	{
		float percent = (slider.getPosX() - posX)/(float)(width - slider.getWidth());

		if(percent > 1)
			percent = 1;
		if(percent < 0)
			percent = 0;
		//System.out.println(slider.getPosX() + ":" + posX + ":" + width +":"+slider.getWidth());
		//System.out.println(percent);
		return percent;
	}
	
	public boolean isPressed()
	{
		return slider.isPressed() || pressed;
	}

	boolean inSlider(float x)
	{
		return x >= posX & x <= posX + width - slider.getWidth(); 
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}
}
