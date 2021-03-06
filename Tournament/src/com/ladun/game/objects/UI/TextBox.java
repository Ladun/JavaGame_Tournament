package com.ladun.game.objects.UI;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.game.GameManager;
import com.ladun.game.components.Component;

public class TextBox{
	
	private static final float BACKSPACE_INTERVAL = .05f;
	private static final float BACKSPACE_FIRST_INTERVAL = .3f;

	private int posX;
	private int posY;
	private int size;
	private int maxLength;
	private StringBuilder sb = new StringBuilder();
	private String defaultStr;
	
	private float backspaceTime = 0;
	private boolean lastStillPressed = false;
	
	private float time;	
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
				if(gc.getInput().getLastCode() != KeyEvent.VK_BACK_SPACE && gc.getInput().getLastCode() != KeyEvent.VK_ENTER) {
					if(sb.length() < maxLength) {
						char c = gc.getInput().getLastStr();
						if(c == 3) {
							//TODO: Ctrl + C
						}
						else if(c == 22) {
							sb.append(getClipBoardData());
						}
						else
							sb.append(gc.getInput().getLastStr());
					}
				}
			}
			if(gc.getInput().isKeyDown(KeyEvent.VK_BACK_SPACE)) {
				backspaceTime = 0;
				if(sb.length() > 0)
					sb.setLength(sb.length()-1);
			}
			else if(gc.getInput().isKey(KeyEvent.VK_BACK_SPACE)) {
				if(lastStillPressed) {
					if(backspaceTime >= BACKSPACE_INTERVAL) {
						if(sb.length() > 0)
							sb.setLength(sb.length()-1);
						backspaceTime = 0;
					}
				}
				else {
					if(backspaceTime >= BACKSPACE_FIRST_INTERVAL) {
						backspaceTime = 0;
						lastStillPressed = true;
					}
				}
			}
			else if(gc.getInput().isKeyUp(KeyEvent.VK_BACK_SPACE)) {
				lastStillPressed = false;
			}
			time += Time.DELTA_TIME;
			if(time >= 0.8f)
				time -= 0.8f;
			backspaceTime += Time.DELTA_TIME;
		}
	}

	
	public void render(GameContainer gc, Renderer r) {

		if(chatOn) {
			if(sb.length() > 0) {
				r.drawString(sb.toString(),posX,posY,size,color);
				if(time >= 0.4f) {
					r.setzDepth(Renderer.LAYER_UI);
					r.drawFillRect(
							gc.getWidth()/2 +gc.getWindow().getG().getFontMetrics(gc.getWindow().getG().getFont().deriveFont((float)size)).stringWidth(sb.toString()) /2 ,
							posY, 1, size, 0, color);
				}
			}
			else
				r.drawString(defaultStr,posX,posY,size,color);
		}
			
	}
	
	private String getClipBoardData() {
		 try {
			return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return "";
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
	
	public void lengthSetZero() {
		sb.setLength(0);
	}

	public String getString() {
		return sb.toString().trim();
	}
}