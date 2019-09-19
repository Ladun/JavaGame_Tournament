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
		Public(0) ,Warrior(1),Archer(2) ,Spear(3), Assasin(4);
		
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
	private Tooltip tooltip;
	private GameScene gs;
	
	private ArrayList<ShopSlot> slots = new ArrayList<ShopSlot>();
	
	public Shop(GameScene gs) {
		this.gs = gs;
		this.tooltip = new Tooltip();
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
		if(tooltip.isActive())
			tooltip.update(gc, gm);
	}
	public void render(GameContainer gc, Renderer r) {
		
		for(ShopSlot _slot : slots) {
			if(_slot.isActive())
				_slot.render(gc, r);
		}
		
		if(tooltip.isActive())
			tooltip.render(gc, r);
	}
	
	public void settingSlot(GameContainer gc, GameManager gm) {
		
		active = true;
		
		int horizontalMaxCount = (gc.getWidth() - SLOT_SIDE_GAP * 4) / (ShopSlot.getWidth() + SLOT_GAP);
		int horizontalCount = 0;
		
		int verticalCount= 0;
				
		int slotIndex = 0;
		for(Item _item : gm.getItemDatabase().getItemList()) {
			if(_item.getID() / TYPE_INTERVAL == jobType.getValue() / TYPE_INTERVAL) {
				if(slotIndex < slots.size()){
					slots.get(slotIndex).setItem(slotIndex,_item,SLOT_SIDE_GAP * 2+ (ShopSlot.getWidth() + SLOT_GAP) * horizontalCount , SLOT_SIDE_GAP + (ShopSlot.getHeight() + SLOT_GAP) * verticalCount);
					slotIndex++;
				}else {
					slots.add(new ShopSlot(this,slotIndex,_item,SLOT_SIDE_GAP * 2+ (ShopSlot.getWidth() + SLOT_GAP) * horizontalCount , SLOT_SIDE_GAP + (ShopSlot.getHeight() + SLOT_GAP) * verticalCount));
					slotIndex++;
				}
				horizontalCount++;
				
				if(horizontalCount >= horizontalMaxCount) {
					verticalCount++;
					horizontalCount = 0;
				}
				
			}
		}
	}
	public void tooltipSetting(Item item,int id) {
		if(!tooltip.isActive()) {
			Tooltip.Content[] content = new Tooltip.Content[item.typeCount()+ 1 ];
			int contentIndex = 1;
			content[0] = tooltip.new Content(item.getName(),0xff000000);
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < Item.Type.values().length;i++) {
				//System.out.println("1: " + Item.Type.values()[i]);
				if(item.hasType(Item.Type.values()[i])) {
					sb.setLength( 0);
					switch(Item.Type.values()[i]) {
					case STAT_HEALTH:
						sb.append("HP");
						break;
					case STAT_MANA:
						sb.append("MP");
						break;
					case STAT_DAMAGE:
						sb.append("Damage");
						break;
					case STAT_DEFENCE:
						sb.append("Defence");
						break;
					case STAT_MOVESPEED:
						sb.append("MoveSpeed");
						break;
					default:
						break;
					}
					int option = item.getOptionValue(Item.Type.values()[i]);
					//System.out.println(option);
					if(option != 0) {
						if(option > 0)
							sb.append(" +");
						sb.append(option);
					}
					
					content[contentIndex++] =  tooltip.new Content(sb.toString(),0xff444444);
				}
			}
			tooltip.setting(content, id);
		}
		tooltip.setActive(true);
	}
	
	//---------------------------------------------
	public boolean isActive() {
		return active;
	}

	public Tooltip getTooltip() {
		return tooltip;
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
