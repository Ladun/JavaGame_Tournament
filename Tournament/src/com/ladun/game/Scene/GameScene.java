package com.ladun.game.Scene;

import java.awt.event.KeyEvent;

import javax.crypto.spec.GCMParameterSpec;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.Camera;
import com.ladun.game.GameManager;
import com.ladun.game.Map;
import com.ladun.game.Physics;
import com.ladun.game.net.Client;
import com.ladun.game.objects.Entity;
import com.ladun.game.objects.Player;
import com.ladun.game.objects.UI.Button;

public class GameScene extends AbstractScene{

	private Map[] maps = new Map[3];
	private int currentMapIndex = 0;
	
	
	private float barAnim = 0;
	
	private boolean inventoryView;
	
	private Camera 		camera;
	private Player localPlayer;
	
	// -----------------------------------------
	private int targetMapIndex;
	private boolean allClientReady = false;
	private float readyToStartTime = 5;
	// Buttons----------------------------------
	private Button teamChooseButton;
	//-----------------------------------------
	private TeamColor teamColor = TeamColor.TEAM1;
	
	@Override
	public boolean init(GameContainer gc,GameManager gm,boolean active) {
		// TODO Auto-generated method stub
		this.active = active;
		
		this.name = "InGame";		
		//this.objects.add(new Player(1,1,this));
		this.camera = new Camera(gm,"Player");
		//this.addObject(new TempObject(this));

		this.maps[0] = new Map("waitingroom");
		this.maps[1] = new Map("teamwait");
		this.maps[2] = new Map("map1");
		
		//Buttons Init---------------------------------------
		teamChooseButton = new Button(30,gc.getHeight()/2 - 70, 100 ,50,teamColor.getValue());
		//---------------------------------------------------
		return true;
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		//maps[currentMapIndex].update(gc, gm, dt);

		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).isActive())
				objects.get(i).update(gc, gm, dt);
		}		
		
		if(currentMapIndex == 0) {
			teamChooseButton.update(gc, gm, dt);
			
			if(allClientReady) {
				if(readyToStartTime - dt >0) {
					readyToStartTime -= dt;
				}
				else {
					// TODO: Map Change And Send Packet(
					readyToStartTime = 5;
					allClientReady = false;
					
					gm.getClient().send(Client.PACKET_TYPE_GAMESTATE,new Object[] {(char)0x00});
				}
			}
			else {
				readyToStartTime = 5;
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
					
					if(preIndex != targetMapIndex)
						gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x17,targetMapIndex});
				}
			}
			
			// Button ---------------------------------------------------------------------------------
			if(teamChooseButton.isReleased()) {
				teamChange();			
				
				gm.getClient().send(Client.PACKET_TYPE_VALUECHANGE,new Object[] {(char)0x14,localPlayer.getTeamNumber()});
				teamColor = TeamColor.values()[localPlayer.getTeamNumber()];
				teamChooseButton.setColor(teamColor.getValue());
			}
		}
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_C))
			inventoryView = !inventoryView;

		camera.update(gc, gm, dt);
		Physics.update();
	}
	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		camera.render(r);
		maps[currentMapIndex].render(gc, r);
		
		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).isActive()) {
				if(objects.get(i) instanceof Entity) {
					if(((Entity)objects.get(i)).getCurrentMapIndex() == currentMapIndex){
						objects.get(i).render(gc, r);
					}
				}
				else {				
					objects.get(i).render(gc, r);
				}
			}
		}
		
		renderUI(gc,r);
		
	}
	//---------------------------------------------------------------------------------------------

	
	private void renderUI(GameContainer gc, Renderer r) {
		r.setzDepth(Renderer.LAYER_UI);
		r.setCamX(0);
		r.setCamY(0);

		renderButtons(gc,r);
		
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
			r.drawImage(gc.getImageLoader().getImage("slot_black"),
					gc.getWidth()/2 + pX, gc.getHeight() - 80 , 
					0,0,59,(int)(59 * (localPlayer != null? localPlayer.getCoolDownPercent(i):1 )));
		}
		
		
		if(allClientReady) {
			r.drawImageTile((ImageTile)gc.getImageLoader().getImage("count"), gc.getWidth()/2 - 64, gc.getWidth()/2 - 64,(int)readyToStartTime,0, 0);
		}
	}

	private void renderButtons(GameContainer gc, Renderer r) {
		switch(currentMapIndex) {
		case 0:
			teamChooseButton.render(gc, r);
			break;
		case 1:
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
	
	public void changeMap() {
		this.currentMapIndex = targetMapIndex;
	}

	private void teamChange() {
		localPlayer.setTeamNumber((localPlayer.getTeamNumber() + 1) % 8);
	}
	
	public Player addPlayer(String name,int tileX, int tileY,boolean isLocalPlayer) {
		Player p = new Player(name,tileX, tileY, this,isLocalPlayer);
		this.objects.add(p);
		return p;
	}
	public Player addPlayer(String name,boolean isLocalPlayer) {
		int[] pos = maps[currentMapIndex].RandomSpawnPoint();
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
		if(currentMapIndex >= maps.length || currentMapIndex < 0)
			return Physics.MAX_HEIGHT;
		if(maps[currentMapIndex] == null)
			return Physics.MAX_HEIGHT;
		
		return maps[currentMapIndex].getHeight(tileX, tileY);		
	}
	
	public boolean getCollision(int tileX,int tileY) {
		if(currentMapIndex >= maps.length || currentMapIndex < 0)
			return true;
		if(maps[currentMapIndex] == null)
			return true;
		
		return maps[currentMapIndex].getCollision(tileX, tileY);
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
		return maps[currentMapIndex].getLevelW();
	}

	@Override
	public int getLevelH() {
		return maps[currentMapIndex].getLevelH();
		
	}

	public int getCurrentMapIndex() {
		return currentMapIndex;
	}

	public Camera getCamera() {
		return camera;
	}

	public boolean isAllClientReady() {
		return allClientReady;
	}

	public void setAllClientReady(boolean allClientReady) {
		this.allClientReady = allClientReady;
	}
}
