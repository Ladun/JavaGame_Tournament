package com.ladun.game.Scene;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.engine.gfx.Image;
import com.ladun.game.GameManager;

public class LoadingScene extends AbstractScene{

	private Image logo;
	
	private float 		time = 0;
	
	private boolean 	fading;
	private float 		fadingPercent	= 0;
	private float		fadingTime		= 2f;
	
	private boolean 	callImageLoadFunc = false;
	
	@Override
	public boolean init(GameContainer gc, GameManager gm, boolean active) {
		
		this.gm = gm;
		this.active = active;		
		this.name = "LoadingScene";	
		
		logo = new Image("/Logo.png");
		
		this.fadingPercent	= 1; 
		this.fadingTime = (float)Time.DELTA_TIME / fadingTime; // Time.DELTA_TIME * (1 / fadingTime)
		return true;
	}

	@Override
	public void update(GameContainer gc) {
		time += Time.DELTA_TIME;
		if(time >= 3.5f) {
			if(fadingPercent < 1) {
				fadingPercent += fadingTime;
				if(fadingPercent >= 1 ) {
					fadingPercent = 1;
				}
			}else {
				if(time >= 6f)
					gm.changeScene("MainMenu");
			}
		}
		else if(time >= 1) {
			if(!callImageLoadFunc) {
				callImageLoadFunc = true;
				ResourceLoad(gc);
			}

			if(fadingPercent > 0) {
				fadingPercent -= fadingTime;
				if(fadingPercent < 0) {
					fadingPercent = 0;

				}
			}
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		float alpha = 255 * fadingPercent;
		
				
		int color = ((int)alpha << 24 | 0x00000000);

		r.drawImage(logo,gc.getWidth() / 2 - 128,gc.getHeight() / 2 - 128,0);
		r.drawFillRect(0, 0, gc.getWidth(), gc.getHeight(),0, color);
	}
	
	private void ResourceLoad(GameContainer gc) {
		new Thread(() ->{
			
			gc.getResourceLoader().ImageLoad();
			gc.getResourceLoader().SoundLoad();
			
		},"ResourceLoad-Thread").start();
	}

	@Override
	public int getLevelW() {
		
		return 0;
	}

	@Override
	public int getLevelH() {
		return 0;
	}

}
