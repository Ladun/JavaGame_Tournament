package com.ladun.game.objects.UI;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.objects.GameObject;

public class TextBox{

	private int posX;
	private int posY;
	private int size;
	private int maxLength;
	private StringBuilder sb = new StringBuilder();
	private String defaultStr;
	
	
	private boolean chatOn;
	
	private int color;
	
	public TextBox(int x, int y,int size, int color,int maxLength,String defaultStr)
	{
		this.posX	= x;
		this.posY	= y;
		this.size 	= size;
		this.color 	= color;
		this.maxLength = maxLength;
		this.defaultStr = defaultStr;
	}
	
	public void update(GameContainer gc, GameManager gm) {
		if(chatOn) {
			if(gc.getInput().isKeyPressed()){
				if(gc.getInput().getLastCode() != KeyEvent.VK_BACK_SPACE) {
					if(sb.length() < maxLength) {
						sb.append(gc.getInput().getLastStr());
					}
				}
			}
			if(gc.getInput().isKeyDown(KeyEvent.VK_BACK_SPACE)) {
				if(sb.length() > 0)
					sb.setLength(sb.length()-1);
			}
		}
	}

	
	public void render(GameContainer gc, Renderer r) {

		if(chatOn) {
			if(sb.length() > 0)
				r.drawString(sb.toString(),posX,posY,size,color);
			else
				r.drawString(defaultStr,posX,posY,size,color);
		}
			
	}
	
	

	public boolean isChatOn() {
		return chatOn;
	}

	public void setChatOn(boolean chatOn) {
		this.chatOn = chatOn;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getString() {
		return sb.toString();
	}
}