package com.ladun.game.Scene;

import java.awt.event.KeyEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.audio.SoundClip;
import com.ladun.engine.gfx.Image;
import com.ladun.game.Announce;
import com.ladun.game.GameManager;
import com.ladun.game.net.Client.ServerState;
import com.ladun.game.objects.UI.Button;
import com.ladun.game.objects.UI.Slider;
import com.ladun.game.objects.UI.TextBox;

public class MainMenuScene extends AbstractScene{

	private Button startButton;
	private Button optionButton;
	private Button exitButton;
	
	private Image backgroundImage;	
	//-------------------------------------------------------------
	private TextBox[] textBoxs;
	

	private Slider volumeSlider;
	private boolean optionWindowShow;
	
	
	@Override
	public boolean init(GameContainer gc,GameManager gm, boolean active) {
		
		this.gm = gm;
		this.active = active;
		this.name = "MainMenu";
		
		backgroundImage = new Image("/UI/mainScene/mainScene.png");
		
		textBoxs = new TextBox[2];
		textBoxs[0] = new TextBox(Renderer.ALLIGN_CENTER,302,25,0xffffffff,21,"(xxx.xxx.xxx.xxx:port)");
		textBoxs[1] = new TextBox(Renderer.ALLIGN_CENTER,337,25,0xffffffff,8,"별명 입력(최대 8자)");
		startButton = new Button(gc.getWidth() /2 - 38, gc.getHeight() -340,76,76, "start_button");
		optionButton = new Button(gc.getWidth() /2 - 38, gc.getHeight() -220,76,76, "option_button");
		exitButton = new Button(gc.getWidth() /2 - 38, gc.getHeight() -100,76,76, "exit_button");
		
		volumeSlider =new Slider(gc.getWidth() / 2 - 100, gc.getHeight() /2 - 200, 200, 35, 20, 0.9300098f, 0xffcccccc, 0x66ffffff);
		
		return true;
	}

	@Override
	public void update(GameContainer gc) {
		
		
		
		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).isActive())
				objects.get(i).update(gc, gm);
		}

		optionButton.update(gc, gm);
		if(optionWindowShow) {
			volumeSlider.update(gc, gm);
			
			if(volumeSlider.isPressed())
			{
				for(String key : gc.getResourceLoader().getSounds().keySet())
				{
					SoundClip sc = gc.getResourceLoader().getSounds().get(key);
					sc.setVolume(volumeSlider.getPercent());
				}
			}
		}else {
			startButton.update(gc, gm);
			exitButton.update(gc, gm);
			for(TextBox t : textBoxs)
				t.update(gc, gm);
		}
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_ENTER)) {
			if(textBoxs[1].getString().length() == 0) {
				// 별명을 입력을 안 함
				gm.getAnnounce().addString("별명을 입력해주십시오", Announce.DEFAULT_COLOR);
				gm.getAnnounce().show(5);
			}else {
				gm.setNickname(textBoxs[1].getString());
				// 서버로 접속
				if(gm.getClient().setHost(textBoxs[0].getString().trim()))
					ConnectToServer(gc,gm);
				else {
					// 주소의 형식이 잘못 됨
					gm.getAnnounce().addString("잘못된 주소 형식입니다", Announce.DEFAULT_COLOR);
					gm.getAnnounce().show(5);
				}
			}
			
		}
		if(textBoxs[0].isChatOn() || textBoxs[1].isChatOn()) {
			if(gc.getInput().isKeyDown(KeyEvent.VK_UP)) {
				textBoxs[0].setChatOn(true);
				textBoxs[1].setChatOn(false);
				
			}
			else if(gc.getInput().isKeyDown(KeyEvent.VK_DOWN)) {
				textBoxs[0].setChatOn(false);
				textBoxs[1].setChatOn(true);
			}
		}

		if(startButton.isReleased()) {
			if(!textBoxs[0].isChatOn()) {
				if(textBoxs[1].isChatOn()) {
					textBoxs[0].setChatOn(false);
					textBoxs[1].setChatOn(false);					
				}else {
					textBoxs[0].setChatOn(!textBoxs[0].isChatOn());
					textBoxs[1].setChatOn(false);
				}
			}else {
				textBoxs[0].setChatOn(false);
				textBoxs[1].setChatOn(false);
			}
		}
		if(optionButton.isReleased()) {
			optionWindowShow = !optionWindowShow;
		}
		if(exitButton.isReleased()) {
			System.exit(0);
		}
		
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		r.drawMap(backgroundImage, gc.getWidth(), gc.getHeight());
		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).isActive())
				objects.get(i).render(gc, r);
		}
		
		if(textBoxs[0].isChatOn()) {
			r.drawString("IP와 별명을 적고 Enter를 눌러주세요. 방향키로 입력창 바꿀 수 있습니다", Renderer.ALLIGN_CENTER, 260, 30, 0xff000000);
			r.drawFillRect(0,300,gc.getWidth(),30,0,0x88000000);
			r.drawFillRect(0,335,gc.getWidth(),30,0,0x55000000);
		}
		else if(textBoxs[1].isChatOn()) {
			r.drawString("IP와 별명을 적고 Enter를 눌러주세요. 방향키로 입력창 바꿀 수 있습니다", Renderer.ALLIGN_CENTER, 260, 30, 0xff000000);
			r.drawFillRect(0,300,gc.getWidth(),30,0,0x55000000);
			r.drawFillRect(0,335,gc.getWidth(),30,0,0x88000000);
		}

		
		startButton.render(gc, r);
		exitButton.render(gc, r);
		for(TextBox t : textBoxs)
			t.render(gc, r);
		if(optionWindowShow) {
			r.drawFillRect(0, 0, gc.getWidth(), gc.getHeight(), 0, 0xaa000000);
			
			r.drawString("SOUND", volumeSlider.getPosX(), volumeSlider.getPosY() - 40, 35, 0xffffffff);
			volumeSlider.render(gc, r);
		}
		optionButton.render(gc,r);
	}

	//-------------------------------------------------------------------------------
	private void ConnectToServer(GameContainer gc,GameManager gm) {
		if(gm.isLoading())
			return;
		
		gm.setLoading(true);
		new Thread(new Runnable() {
			double stTime = System.currentTimeMillis();
			

			@Override
			public void run() {
				if(!gm.getClient().connect()) {
					gm.getAnnounce().addString("잘못된 주소 형식입니다", Announce.DEFAULT_COLOR);
					gm.getAnnounce().show(5);
					gm.setLoading(false);
					return;
				}
				
				double nowTime = 0; 
				
				while(!gm.getClient().isServerRespond()) {
					//System.out.println(gm.getClient().isServerRespond());
					synchronized (gm) {
						nowTime = (System.currentTimeMillis() - stTime) / 1000; 
						if(nowTime >= 5|| gm.getClient().isServerRespond()) {
							break;
						}
					
					}
					
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}

				synchronized (gm) {
					if(gm.getClient().getServerState() != ServerState.GAME_START) {
						if(nowTime>= 5) {
							
							//System.out.println("Server is Closed");
							gm.getClient().disconnect();
							gm.getAnnounce().addString("서버가 닫혔습니다",Announce.DEFAULT_COLOR);
							gm.getAnnounce().addString("Server is Closed",Announce.DEFAULT_COLOR);
							gm.getAnnounce().show(5);
						}
						else {
							gm.changeScene("InGame");
							((GameScene)gm.getScene("InGame")).mapLoad(gc,0);
							
							gm.getItemDatabase().init(gm);
						}
					}
					gm.setLoading(false);
				}
				
			}
			
		},"Connecting-Thread").start();;
	}
	
	//-------------------------------------------------------------------------------
	@Override
	public int getLevelW() {
		
		return 0;
	}

	@Override
	public int getLevelH() {
		
		return 0;
	}

}
