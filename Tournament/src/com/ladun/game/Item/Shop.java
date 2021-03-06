package com.ladun.game.Item;

import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Item.Tooltip.Content;
import com.ladun.game.Scene.GameScene;

public class Shop {

	private static final int TYPE_INTERVAL = 16;	
	public enum JobType {
		Public(0) ,Warrior(1),Archer(2) ,Spear(3), Assasin(4),Magician(5),Crusader(6);
		
		private final int value;
		private JobType(int value) {
			this.value = value * TYPE_INTERVAL;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	private final int SLOT_GAP = 30;
	private final int SLOT_SIDE_GAP = 100;
	
	private JobType jobType;
	private boolean active;	
	private GameScene gs;
	
	private ArrayList<ShopSlot> slots = new ArrayList<ShopSlot>();
	
	public Shop(GameScene gs) {
		this.gs = gs;
		active = false;
		jobType = JobType.Public;
	}
	
	public void update(GameContainer gc, GameManager gm) {
		if(!active)
			return;
		
		for(ShopSlot _slot : slots) {
			if(_slot.isActive())
				_slot.update(gc, gm);
		}
	}
	public void render(GameContainer gc, Renderer r) {
		
		for(ShopSlot _slot : slots) {
			if(_slot.isActive())
				_slot.render(gc, r);
		}
	}
	
	public void settingSlot(GameContainer gc, GameManager gm) {
		
		active = true;
		jobType = JobType.values()[((GameScene)gm.getScene("InGame")).getLocalPlayer().getWeapon().getType().ordinal() + 1];
		
		int horizontalMaxCount = (gc.getWidth() - SLOT_SIDE_GAP * 4 + SLOT_GAP) / (ShopSlot.getWidth() + SLOT_GAP);
		int horizontalCount = 0;
		
		int verticalCount= 0;
				
		int slotIndex = 0;
		for(Item _item : gm.getItemDatabase().getItemList()) {
			System.out.printf("%d %d\n", _item.getID(), jobType.getValue());
			if(_item.getID() / TYPE_INTERVAL == jobType.getValue() / TYPE_INTERVAL ||_item.getID() / TYPE_INTERVAL == 0) {
				if(slotIndex < slots.size()){
					slots.get(slotIndex).setItem(slotIndex,_item,SLOT_SIDE_GAP * 2+ (ShopSlot.getWidth() + SLOT_GAP) * horizontalCount , 200 + (ShopSlot.getHeight() + SLOT_GAP) * verticalCount);
					slotIndex++;
				}else {
					slots.add(new ShopSlot(this,slotIndex,_item,SLOT_SIDE_GAP * 2+ (ShopSlot.getWidth() + SLOT_GAP) * horizontalCount , 200 + (ShopSlot.getHeight() + SLOT_GAP) * verticalCount));
					slotIndex++;
				}
				horizontalCount++;
				
				if(horizontalCount >= horizontalMaxCount) {
					verticalCount++;
					horizontalCount = 0;
				}
				
			}
		}
		for(int i = slotIndex; i < slots.size();i++) {
			slots.get(i).setActive(false);
		}
	}
	
	//---------------------------------------------
	public boolean isActive() {
		return active;
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public GameScene getGs() {
		return gs;
	}
	
	
}
