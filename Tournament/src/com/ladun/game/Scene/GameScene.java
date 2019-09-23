package com.ladun.game.Scene;

import java.awt.AWTException;
import java.awt.event.KeyEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.engine.audio.SoundClip;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.Camera;
import com.ladun.game.GameManager;
import com.ladun.game.Map;
import com.ladun.game.Physics;
import com.ladun.game.Item.Shop;
import com.ladun.game.net.Client;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.Interior;
import com.ladun.game.objects.Player;
import com.ladun.game.objects.UI.Button;
import com.ladun.game.objects.UI.Slider;
import com.ladun.game.objects.UI.TextBox;

public class GameScene extends AbstractScene{


	private String[] mapNames = new String[] {"waitingroom","teamwait","map1"};
	private Map currentMap;
	private int currentMapIndex = -1;
	
	
	private float barAnim = 0;
	
	private boolean inventoryView;
	
	private Camera 		camera;
	private Player localPlayer;
	
	private int round, maxRound;	
	// -----------------------------------------
	private int targetMapIndex;
	private float readyToStartTime = 0;
	private boolean gameisStart = false;// 게임 중임 표시
	private boolean inteamWaitRoom = false; // 게임 시작 바로 전에 서버로 데이터를 한 번 보내기 위해서
	// UI Contents----------------------------------
	private Button teamChooseButton;
	private Shop shop;
	private Button storeButton;
	private Button jobButton;
	private Button[] jobSelectButtons;
	
	private Slider volumeSlider;
	private Button exitButton;
	private boolean optionWindowShow;	
	
	private TextBox chatTextBox;
	//-----------------------------------------
	private boolean jobSelectButtonShow;
	private float jSBMovePercent; // JobSelectButtonMovePercent;
	
	private TeamColor teamColor = TeamColor.TEAM1;
	
	@Override
	public boolean init(GameContainer gc,GameManager gm,boolean active) {
		// TODO Auto-generated method stub
		this.gm = gm;
		this.active = active;
		this.name = "InGame";		
		//this.objects.add(new Player(1,1,this));
		try {
			this.camera = new Camera(gm,this,"Player");
		}
		catch(AWTException e0) {
			System.out.println("Camera AWTException");
			return false;
		}
		/*
		this.addObject(new TempObject(this,true));
		TempObject a = new TempObject(this,false);
		a.setPosX(100);
		a.setPosZ(100);
		
		this.addObject(a);*/
		
		shop = new Shop(this);
		//Buttons Init---------------------------------------
		teamChooseButton = new Button(208,gc.getHeight()- 92, 64 ,64,teamColor.getValue());
		storeButton = new Button(30, gc.getHeight()/2 -48,96,96,"store_button");
		jobButton = new Button(gc.getWidth() - 126, gc.getHeight()/2 -48,96,96,"job_button");

		exitButton = new Button(gc.getWidth() /2 - 38, gc.getHeight() -100,76,76, "exit_button");
		jobSelectButtons = new Button[4];
		for(int i = 0; i < jobSelectButtons.length;i++) {
			jobSelectButtons[i] =  new Button(gc.getWidth()/2, gc.getHeight()/2 + i * 64,64,64,"jobSelect_button" + (i +1));
		}

		volumeSlider =new Slider(gc.getWidth() / 2 - 100, gc.getHeight() /2 - 200, 200, 35, 20, .7f, 0xffcccccc, 0x66ffffff);
		
		chatTextBox = new TextBox(Renderer.ALLIGN_CENTER, gc.getHeight() - 150, 20, 0xff000000, 20, "채팅을 입력해주세요");
		//---------------------------------------------------
		return true;
	}

	@Override
	public void update(GameContainer gc) {
		// TODO Auto-generated method stub

		if(!gm.isLoading()) {
			if(currentMap != null)
				currentMap.update(gc, gm);
			for(int i = 0; i < objects.size();i++) {
				if(objects.get(i).isActive())
					objects.get(i).update(gc, gm);
			}		
		}
		
		mapUpdate(gc,gm);

		if(gc.getInput().isKeyDown(KeyEvent.VK_ENTER)) {
			//gm.getChatBox().addTexts("Hello this is Chat Box");
			if(chatTextBox.isChatOn()) {
				if(chatTextBox.getString().length() > 0) {
					gm.getChatBox().addTexts(gm.getNickname() + ": "+chatTextBox.getString(),TeamColor.getColor(localPlayer.getTeamNumber()));
					
					gm.getClient().send((byte)0x07, new Object[] {(char)0x03,gm.getNickname(),chatTextBox.getString(),localPlayer.getTeamNumber()});
					chatTextBox.lengthSetZero();
				}
			}
			chatTextBox.setChatOn(!chatTextBox.isChatOn());
		}		
		if(gc.getInput().isKeyDown(KeyEvent.VK_C))
			if(!isChatting())
				inventoryView = !inventoryView;
		if(gc.getInput().isKeyDown(KeyEvent.VK_ESCAPE)){
			if(chatTextBox.isChatOn()) {
				chatTextBox.setChatOn(false);
			}else {
				optionWindowShow = !optionWindowShow;
			}
		}
		
		if(optionWindowShow) {
			volumeSlider.update(gc, gm);
			exitButton.update(gc, gm);
			
			if(volumeSlider.isPressed())
			{
				for(String key : gc.getResourceLoader().getSounds().keySet())
				{
					SoundClip sc = gc.getResourceLoader().getSounds().get(key);
					sc.setVolume(volumeSlider.getPercent());
				}
			}
			
			if(exitButton.isReleased()) {
				gm.getClient().disconnect();
				currentMapIndex		= 0;
				readyToStartTime 	= 0;
				gameisStart 		= false;
				inteamWaitRoom 		= false; 
				objects.clear();
				gm.changeScene("MainMenu");
				optionWindowShow = false;
			}
		}
		if(chatTextBox.isChatOn()) {
			chatTextBox.update(gc, gm);
		}
		

		camera.update(gc, gm);
		Physics.update();
	}
	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		camera.render(r);
		if(currentMap != null)
			currentMap.render(gc, r);
		
		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).isActive()) {
				objects.get(i).render(gc, r);
			}
		}

		renderUI(gc,r);
		
	}
	//---------------------------------------------------------------------------------------------

	
	private void renderUI(GameContainer gc, Renderer r) {
		r.setzDepth(Renderer.LAYER_UI);
		r.setCamX(0);
		r.setCamY(0);
		
		if(currentMap == null)
			return;
		
		r.drawString(gm.getNickname(), Renderer.ALLIGN_RIGHT, 0, 30, 0xff000000);
		r.drawString(gm.getMoney() +"", 0, 0, 25, 0xffafca42);
		r.drawString(round + " / " + maxRound, -1 , 0, 13, 0xff207cda);
		
		renderOthers(gc,r);
		
		//r.drawImage(gc.getImageLoader().getImage("window"), gc.getWidth() / 2 - 192, gc.getHeight() /2- 240, 0);
		
		if(inventoryView)
			renderInven(gc,r);
		
		if(currentMapIndex >0) {
			// Bar-----------------------------------------------------------------------
			int barX = gc.getWidth() / 2 - 334 / 2;
			int barY = gc.getHeight() - 110;
			float playerHealthPercent = 1;
			if(localPlayer != null) {
				playerHealthPercent  =localPlayer.getHealth() / localPlayer.getMaxHealth();
			}
			float playerManaPercent = 1;
			if(localPlayer != null) {
				playerManaPercent  =localPlayer.getMana() / localPlayer.getMaxMana();
			}
			
			r.drawImage(gc.getResourceLoader().getImage("bar_frame"), barX - 3, barY, 0);
			r.drawImageTile(((ImageTile)gc.getResourceLoader().getImage("hbar")),
					barX, barY,
					0,(int)barAnim,
					0,0,(int)(334 * playerHealthPercent) ,13);
			r.drawImage(gc.getResourceLoader().getImage("bar_frame"), barX - 3, barY+ 15, 0);
			r.drawImageTile(((ImageTile)gc.getResourceLoader().getImage("mbar")),
					barX, barY+ 15,
					0,(int)barAnim,
					0,0,(int)(334 *playerManaPercent),13);
			// --------------------------------------------------------------------------
			
			for(int i =0; i < 4 ; i++) {
				
				int pX = (int)(64 * (i-2) + 29* ( i  - 1.5f));
				
				r.drawImage(gc.getResourceLoader().getImage("slot"), gc.getWidth()/2 +pX, gc.getHeight() - 80, 0);		
				
				r.drawImageTile((ImageTile)gc.getResourceLoader().getImage("key_image"),  gc.getWidth()/2 +pX, gc.getHeight() - 80,i,0, 0);
				r.drawImageTile((ImageTile)gc.getResourceLoader().getImage("skill_icon"),  gc.getWidth()/2 +pX, gc.getHeight() - 80,i,localPlayer.getWeapon().getType().ordinal(), 0);
				r.drawImage(gc.getResourceLoader().getImage("slot_black"),
						gc.getWidth()/2 + pX, gc.getHeight() - 80 , 
						0,0,59,(int)(59 * (localPlayer != null? localPlayer.getCoolDownPercent(i):1 )));
			}
		}
		
		// Option Window Render--------------------------------------------------
		if(optionWindowShow) {
			r.drawFillRect(0, 0, gc.getWidth(), gc.getHeight(), 0, 0xaa000000);
			
			r.drawString("SOUND", volumeSlider.getPosX(), volumeSlider.getPosY() - 40, 35, 0xffffffff);
			volumeSlider.render(gc, r);
			exitButton.render(gc, r);
		}
		if(chatTextBox.isChatOn()) {
			chatTextBox.render(gc, r);
		}
		
		
		if(readyToStartTime > 0) {
			r.drawImageTile((ImageTile)gc.getResourceLoader().getImage("count"), gc.getWidth()/2 - 64, gc.getHeight()/2 - 64,((int)readyToStartTime +1)% 6,0, 0);
		}
	}

	private void renderOthers(GameContainer gc, Renderer r) {
		switch(currentMapIndex) {
		case 0:
			teamChooseButton.render(gc, r);
			r.drawImage(gc.getResourceLoader().getImage("team_select"), 200, gc.getHeight() - 100, 0);
			r.drawImageTile((ImageTile)gc.getResourceLoader().getImage("map_icon"), 20, gc.getHeight() - 180, targetMapIndex < 2? 0 : targetMapIndex - 1,	0,0);
			break;
		case 1:
			if(shop.isActive())
				shop.render(gc,r);
			storeButton.render(gc, r);
			jobButton.render(gc, r);
			if(jobSelectButtonShow || jSBMovePercent != 1) {
				for(int i = 0; i < jobSelectButtons.length;i++) {
					jobSelectButtons[i].render(gc, r);
				}
			}
			break;
		case 2:
			break;
		}
	}
	
	private void renderInven(GameContainer gc,Renderer r) {
		int ltX = gc.getWidth() / 2 + 180;
		int ltY = gc.getHeight() - 405;
		
		r.drawImage(gc.getResourceLoader().getImage("inv"),ltX,ltY,0 );
		
		for(int i = 0; i < 6;i++) {
			if(localPlayer.getItems()[i] == null || localPlayer.getItems()[i].getID() == -1)
				continue;
			
			localPlayer.getItems()[i].render(gc, r,ltX + 15 + 70 * (i % 2) , ltY + 15 + 64 * (i / 2));
			
		}
		
		r.drawString("HP : " + (int)localPlayer.getHealth() + "/" + (int)localPlayer.getMaxHealth(), ltX + 30,  ltY +210, 16, 0xff7a6a3b);
		r.drawString("HP : " + (int)localPlayer.getMana() + "/" + (int)localPlayer.getMaxMana(), ltX + 30,  ltY +230, 16, 0xff7a6a3b);
		
	}
	

	private void mapUpdate(GameContainer gc, GameManager gm) {
		
		if(currentMapIndex == 0) {
			if(!optionWindowShow)
				teamChooseButton.update(gc, gm);
			
			if(readyToStartTime > 0) {
				readyToStartTime -= Time.DELTA_TIME;
				if(readyToStartTime <= 0) {
					readyToStartTime = 0;
					
					gm.getClient().send(Client.PACKET_TYPE_GAMESTATE,new Object[] {(char)0x00});
				}
			}
			
			// Set targetMapIndex --------------------------------------------------------------------
			if(localPlayer != null) {
				if(localPlayer.isMoving()) {
					int preIndex = targetMapIndex;
					if(localPlayer.getTileZ() >= 1 && localPlayer.getTileZ() <= 2 ) {
						if(localPlayer.getTileX() >= 1 && localPlayer.getTileX() <= 2) {
							targetMapIndex = 2;
						}
						else {
							targetMapIndex = 0;
						}					
					}
					else {
						targetMapIndex = 0;
					}

					if(preIndex != targetMapIndex) {
						gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x17,targetMapIndex});
					}
				}
			}
			
			// Button ---------------------------------------------------------------------------------
			if(!gm.isLoading()) {
				if(teamChooseButton.isReleased()) {
					teamChange();			
					
					gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x14,localPlayer.getTeamNumber()});
					teamColor = TeamColor.values()[localPlayer.getTeamNumber()];
					teamChooseButton.setColor(teamColor.getValue());
				}
			}
		}
		else if(currentMapIndex == 1) {
			if(!optionWindowShow) {
				if(shop.isActive())
					shop.update(gc,gm);
				storeButton.update(gc, gm);
				jobButton.update(gc, gm);
			}
			// Set targetMapIndex --------------------------------------------------------------------
			if(inteamWaitRoom) {
				if(localPlayer != null) {
					if(localPlayer.isMoving()) {
						targetMapIndex = 0;
						if(localPlayer.getTileZ() == 3 && localPlayer.getTileX() == 4 ) {
							gm.getClient().send(Client.PACKET_TYPE_GAMESTATE,new Object[] {(char)0x01});
							inteamWaitRoom = false;
						}
					}
				}
			}
			if(storeButton.isReleased()) {
				shop.setActive(!shop.isActive());
				if(shop.isActive())
					shop.settingSlot(gc, gm);
				
				/*
				gm.getAnnounce().addString("아직 구현되지 않았습니다", Announce.DEFAULT_COLOR);
				gm.getAnnounce().show(3f);
				*/
			}
			if(jobButton.isReleased()) {
				jobSelectButtonShow = !jobSelectButtonShow;
				jSBMovePercent = 0;
			}

			if(jobSelectButtonShow) {
				if(jSBMovePercent < 1) {
					jSBMovePercent += Time.DELTA_TIME* 5;
					
					if(jSBMovePercent > 1) {
						jSBMovePercent = 1;
					}
					
					float _angle = 180 / (jobSelectButtons.length - 1);
					float radius = 150 * jSBMovePercent; 
					for(int i = 0; i < jobSelectButtons.length;i++) {
						jobSelectButtons[i].setCenterX((int)(jobButton.getCenterX() + radius * Math.cos(Math.toRadians(_angle * i +90)) ));
						jobSelectButtons[i].setCenterY((int)(jobButton.getCenterY() + radius * Math.sin(Math.toRadians(_angle * i +90)) ));
					}
					
				}else {				
					for(int i = 0; i < jobSelectButtons.length;i++) {
						jobSelectButtons[i].update(gc, gm);
						
						if(jobSelectButtons[i].isReleased()) {
							localPlayer.setWeaponType(i);

							jobSelectButtonShow = !jobSelectButtonShow;
							jSBMovePercent = 0;
						}
					}
				}
				
				
			}
			else {
				if(jSBMovePercent < 1) {
					jSBMovePercent += Time.DELTA_TIME * 5;
					
					if(jSBMovePercent > 1) {
						jSBMovePercent = 1;
					}

					float _angle = 180 / (jobSelectButtons.length - 1);
					float radius = 150 * (1-jSBMovePercent); 
					for(int i = 0; i < jobSelectButtons.length;i++) {
						jobSelectButtons[i].setCenterX((int)(jobButton.getCenterX() + radius * Math.cos(Math.toRadians(_angle * i +90)) ));
						jobSelectButtons[i].setCenterY((int)(jobButton.getCenterY() + radius * Math.sin(Math.toRadians(_angle * i +90)) ));
					}
					
				}
			}
						
		}
		else {
			if(!gameisStart) {
				if(readyToStartTime > 0) {
					readyToStartTime -= Time.DELTA_TIME;
					if(readyToStartTime <= 0) {
						readyToStartTime = 0;
						gameisStart = true;
						
						localPlayer.setHiding(false);
						gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x18,0});
					}
				}
			}else {
				if(readyToStartTime > 0) {
					readyToStartTime -= Time.DELTA_TIME;
					if(readyToStartTime <= 0) {
						readyToStartTime = 0;
						gameisStart = false;
						mapLoad(gc,1);
					}
				}
			}
		}
	}
	
	
	public void mapLoad(GameContainer gc) {
		mapLoad(gc,targetMapIndex);
	}
	
	public void mapLoad(GameContainer gc,int index) {
		this.currentMapIndex = index;
		
		new Thread(() ->{ 

			while(gm.isLoading()) {}
			

			synchronized (gm) {
				gm.setLoading(true);
			}
			switch(currentMapIndex) {
			case 0:
				currentMap = new Map(mapNames[currentMapIndex]);
				break;
			case 1:
				inteamWaitRoom = true;
				currentMap = new Map(mapNames[currentMapIndex], new GameObject[] {
						new Interior("portal",249,187,80,72,14f,14,false),
						(new Interior("battle_stone",249,72,80,115,8f,8,true)).setpT(95).setpB(5),
						(new Interior("stone_bush_1",128,188,64,41,true)).setpT(19).setpB(8),
						(new Interior("stone_bush_1",398,379,64,41,true)).setpT(19).setpB(8)
				});
				currentMap.setBackgroundColor(0xff405947);
				break;
			default:
				currentMap = new Map(mapNames[currentMapIndex]);
			}
			
			float t = 0;				
			while(localPlayer == null) {
				t += Time.DELTA_TIME;
				if(t >= 5) {
					break;
				}
			}
			 
			if(t < 5) {
				this.localPlayer.setCurrentMapIndex(currentMapIndex);			
				int[] _pos = currentMap.randomSpawnPoint();
				localPlayer.setPos(_pos[0], _pos[1]);

				localPlayer.revival();
			}
			synchronized (gc) {
				camera.focusTarget(gc, gm);
			}
			gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x16,currentMapIndex});
			synchronized (gm) {
				gm.setLoading(false);
			}
		},"Map Load").start();
		
	}

	private void teamChange() {
		localPlayer.setTeamNumber((localPlayer.getTeamNumber() + 1) % 8);
	}
	
	public Player addPlayer(String name,String nickname,int tileX, int tileY,boolean isLocalPlayer, int teamNumber) {
		Player p = new Player(name,nickname,tileX, tileY, this,isLocalPlayer);
		p.setTeamNumber(teamNumber);
		this.objects.add(p);
		return p;
	}
	
	public Player addPlayer(String name,String nickname,boolean isLocalPlayer) {
		int[] pos = new int[] {0,0};
		
		long currentTime = System.currentTimeMillis();
		while(currentMap == null) {
			if((System.currentTimeMillis() - currentTime) / 1000 > 4)
				break;
		}
		pos = currentMap.randomSpawnPoint();
		
		Player p = new Player(name,nickname,pos[0], pos[1], this,isLocalPlayer);
		this.objects.add(p);
		return p;
	}
	
	
	public void removePlayer(String name) {
		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).getTag().equals(name)) {
				objects.remove(i);
				break;
			}
		}
	}
	
	//-------------------------------------------------------------------------------------
	
	public float getHeight(int tileX, int tileY) {
		if(currentMapIndex >= mapNames.length || currentMapIndex < 0)
			return Physics.MAX_HEIGHT;
		if(currentMap == null)
			return Physics.MAX_HEIGHT;
		
		return currentMap.getHeight(tileX, tileY);		
	}
	public boolean isChatting() {
		return chatTextBox.isChatOn();
	}
	
	public Player getLocalPlayer() {
		return localPlayer;
	}

	public void setLocalPlayer(Player localPlayer) {
		this.localPlayer = localPlayer;
	}

	public int getTargetMapIndex() {
		return targetMapIndex;
	}

	public void setTargetMapIndex(int targetMapIndex) {
		this.targetMapIndex = targetMapIndex;
	}

	@Override
	public int getLevelW() {
		if(currentMap == null)
			return 0;
		
		return currentMap.getLevelW();
	}

	@Override
	public int getLevelH() {
		if(currentMap == null)
			return 0;
		
		return currentMap.getLevelH();
		
	}

	public int getCurrentMapIndex() {
		return currentMapIndex;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setAllClientReady(boolean allClientReady) {
		if(allClientReady)
			readyToStartTime = 3;
		else 
			readyToStartTime = 0;
	}

	public boolean isGameisStart() {
		if(currentMapIndex < 2)
			return true;
		return gameisStart;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getMaxRound() {
		return maxRound;
	}

	public void setMaxRound(int maxRound) {
		this.maxRound = maxRound;
	}
	public void finishGame(GameContainer gc,String... finallyWin) {
		gameisStart = false;
		mapLoad(gc,0);
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < finallyWin.length; i += 2) {
			sb.setLength(0);
			sb.append("Team ");
			sb.append(finallyWin[i]);
			sb.append(": ");
			sb.append(finallyWin[i + 1]);
			gm.getAnnounce().addString(sb.toString(),TeamColor.values()[Integer.parseInt(finallyWin[i])].getValue());
		}
		gm.getAnnounce().show(7);
	}

}
