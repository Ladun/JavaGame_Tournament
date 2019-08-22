package com.ladun.game.Scene;

import java.awt.AWTException;
import java.awt.event.KeyEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.Time;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.Camera;
import com.ladun.game.ChatBox;
import com.ladun.game.GameManager;
import com.ladun.game.Map;
import com.ladun.game.Physics;
import com.ladun.game.net.Client;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.Interior;
import com.ladun.game.objects.Player;
import com.ladun.game.objects.UI.Button;

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
	// Buttons----------------------------------
	private Button teamChooseButton;
	private Button storeButton;
	//-----------------------------------------
	private TeamColor teamColor = TeamColor.TEAM1;
	
	@Override
	public boolean init(GameContainer gc,GameManager gm,boolean active) {
		// TODO Auto-generated method stub
		this.gm = gm;
		this.active = active;
		
		this.name = "InGame";		
		//this.objects.add(new Player(1,1,this));
		try {
			this.camera = new Camera(gm,"Player");
		}
		catch(AWTException e0) {
			System.out.println("Camera AWTException");
			return false;
		}
		//this.addObject(new TempObject(this));
		//Buttons Init---------------------------------------
		teamChooseButton = new Button(208,gc.getHeight()- 92, 64 ,64,teamColor.getValue());
		storeButton = new Button(30, gc.getHeight()/2 - 84,128,128,"store_button");
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
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_C))
			inventoryView = !inventoryView;

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
		
		r.drawString(round + " / " + maxRound, -1 , 0, 13, 0xff207cda);
		
		renderOthers(gc,r);
		
		//r.drawImage(gc.getImageLoader().getImage("window"), gc.getWidth() / 2 - 192, gc.getHeight() /2- 240, 0);
		
		if(inventoryView)
			renderInven(gc,r);
		
		
		int barX = gc.getWidth() / 2 - 334 / 2;
		int barY = gc.getHeight() - 110;
		float playerHealthPercent = 1;
		if(localPlayer != null) {
			playerHealthPercent  =localPlayer.getHealth() / localPlayer.getMaxHealth();
		}
		
		r.drawImage(gc.getImageLoader().getImage("bar_frame"), barX - 3, barY, 0);
		r.drawImageTile(((ImageTile)gc.getImageLoader().getImage("hbar")),
				barX, barY,
				0,(int)barAnim,
				0,0,(int)(334 * playerHealthPercent) ,13);
		r.drawImage(gc.getImageLoader().getImage("bar_frame"), barX - 3, barY+ 15, 0);
		r.drawImageTile(((ImageTile)gc.getImageLoader().getImage("mbar")),
				barX, barY+ 15,
				0,(int)barAnim,
				0,0,(int)(334 ),13);
		for(int i =0; i < 4 ; i++) {
			
			int pX = (int)(64 * (i-2) + 29* ( i  - 1.5f));
			
			r.drawImage(gc.getImageLoader().getImage("slot"), gc.getWidth()/2 +pX, gc.getHeight() - 80, 0);		
			
			r.drawImageTile((ImageTile)gc.getImageLoader().getImage("key_image"),  gc.getWidth()/2 +pX, gc.getHeight() - 80,i,0, 0);
			r.drawImage(gc.getImageLoader().getImage("slot_black"),
					gc.getWidth()/2 + pX, gc.getHeight() - 80 , 
					0,0,59,(int)(59 * (localPlayer != null? localPlayer.getCoolDownPercent(i):1 )));
		}
		
		
		if(readyToStartTime > 0) {
			r.drawImageTile((ImageTile)gc.getImageLoader().getImage("count"), gc.getWidth()/2 - 64, gc.getHeight()/2 - 64,((int)readyToStartTime +1)% 6,0, 0);
		}
	}

	private void renderOthers(GameContainer gc, Renderer r) {
		switch(currentMapIndex) {
		case 0:
			teamChooseButton.render(gc, r);
			r.drawImage(gc.getImageLoader().getImage("team_select"), 200, gc.getHeight() - 100, 0);
			r.drawImageTile((ImageTile)gc.getImageLoader().getImage("map_icon"), 20, gc.getHeight() - 180, targetMapIndex < 2? 0 : targetMapIndex - 1,	0,0);
			break;
		case 1:
			storeButton.render(gc, r);
			break;
		case 2:
			break;
		}
	}
	
	private void renderInven(GameContainer gc,Renderer r) {
		int ltX = gc.getWidth() / 2 + 180;
		int ltY = gc.getHeight() - 309;
		
		r.drawImage(gc.getImageLoader().getImage("inv"),ltX,ltY,0 );
		
		for(int i = 0; i < 6;i++) {
			int itemIndex = localPlayer.getItems()[i].getIndex();
			
			r.drawImageTile((ImageTile)(gc.getImageLoader().getImage("items")),
					ltX + 15 + 70 * (i % 2) , ltY + 15 + 64 * (i / 2),
					itemIndex % 5,itemIndex / 5,
					0);
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
	
	private void mapUpdate(GameContainer gc, GameManager gm) {
		
		if(currentMapIndex == 0) {
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
			storeButton.update(gc, gm);
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
	
	public Player addPlayer(String name,int tileX, int tileY,boolean isLocalPlayer) {
		Player p = new Player(name,tileX, tileY, this,isLocalPlayer);
		this.objects.add(p);
		return p;
	}
	
	public Player addPlayer(String name,boolean isLocalPlayer) {
		int[] pos;
		if(currentMap == null) {
			pos = new int[] {0,0};
		}
		else{
			pos = currentMap.randomSpawnPoint();
		}
		Player p = new Player(name,pos[0], pos[1], this,isLocalPlayer);
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
