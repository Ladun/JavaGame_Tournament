package com.ladun.game.Item;

import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;

public class Shop {
	public enum JobType {
		Public(0) ,Warrior(50),Archer(100) ,Spear(150), Assasin(200);
		private final int value;
		private JobType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	private final int SLOT_GAP = 30;
	private final int TYPE_INTERVAL = 50;
	
	private JobType jobType;
	private boolean active;	
	private ArrayList<ShopSlot> slots = new ArrayList<ShopSlot>();
	
	public Shop() {
		active = true;
		jobType = JobType.Public;
	}
	
	public void settingSlot(GameContainer gc, GameManager gm) {
		
		System.out.println(slots.size());
		active = true;
		
	//	int verticalCount = (gc.getHeight() - 200) / 
				
		int _slotPosX = (gc.getWidth() - ShopSlot.WIDTH) / 2;		
		int slotIndex = 0;
		for(Item _item : gm.getItemDatabase().getItemList()) {
			if(_item.getID() / TYPE_INTERVAL == jobType.getValue() / TYPE_INTERVAL) {
				if(slotIndex < slots.size()){
					slots.get(slotIndex).setItem(_item,_slotPosX , 100 + (ShopSlot.HEIGHT + SLOT_GAP) * slotIndex);
					slotIndex++;
				}else {
					slots.add(new ShopSlot(_item,_slotPosX , 100 + (ShopSlot.HEIGHT + SLOT_GAP) * slotIndex));
					slotIndex++;
				}
			}
		}
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
	
	
}
