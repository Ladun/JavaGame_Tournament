package com.ladun.engine;

import java.util.HashMap;

import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;

public class ImageLoader {
	
	private HashMap<String,Image> images = new HashMap<String,Image>();
	
	private boolean load;

	
	public void ImageLoad() {
		if(load)
			return ;
		
		System.out.println("------[ Image Load ]----------------------");
		
		addImage("point", "/Point.png");
		addImage("player", "/entity/Character.png",GameManager.TS,GameManager.TS);
		
		//Weapon Load
		addImage("sword", "/weapon/Sword.png");
		addImage("bow", "/weapon/Bow.png");
		addImage("spear", "/weapon/Spear.png");
		addImage("projectile", "/weapon/Projectile.png",64,64);	
		
		//UI Image Load
		addImage("hbar", "/UI/HBar.png",334,13);
		addImage("mbar", "/UI/MBar.png",334,13);
		addImage("bar_frame", "/UI/BarFrame.png");
		addImage("slot", "/UI/Slot.png");
		addImage("key_image", "/UI/KeyImage.png",16,16);
		addImage("slot_black", "/UI/SlotBlack.png");
		addImage("window", "/UI/Window.png");
		addImage("inv","/UI/Inventory.png");
		addImage("warrior_skill", "/UI/SlotContents/Warrior_Skill.png",64,64);
		
		// Buttons
		addImage("store_button","/UI/StoreButton.png",128,128);
		addImage("start_button","/UI/mainScene/StartButton.png",76,76);
		addImage("option_button","/UI/mainScene/OptionButton.png",76,76);
		addImage("exit_button","/UI/mainScene/ExitButton.png",76,76);
		
		addImage("items","/UI/Items.png",54,54);
		
		addImage("count","/UI/count.png",128,128);
		addImage("loading","/UI/loading.png",128,128);
		addImage("map_icon","/UI/MapIcon.png",160,160);
		addImage("team_select","/UI/teamSelect.png");
		
		addImage("triangleButton__up", "/UI/TriangleButton_Up.png",52,52);
		
		// Map Object Image Load
		addImage("portal","/Map/objects/portal.png",80,72);
		addImage("battle_stone","/Map/objects/BattleStone.png",80,115);
		addImage("stone_bush_1","/Map/objects/StoneBush_1.png");

		System.out.println("------[ Image Load Finish ]---------------");
		load = true;
	}
	
	private void addImage(String name, String path) {
		try {
			System.out.print("Image Load..." + path + "...");
			images.put(name, new Image(path));
			System.out.println("Success");
		} catch (Exception e) {
			System.out.println("Fail");
			e.printStackTrace();
		}
	}
	private void addImageAlpha(String name, String path) {
		try {
			System.out.print("Image Load..." + path + "...");
			images.put(name, new Image(path));
			images.get(name).setAlpha(true);;
			System.out.println("Success");
		} catch (Exception e) {
			System.out.println("Fail");
			e.printStackTrace();
		}
	}
	private void addImage(String name, String path,int tileW,int tileH) {
		try {
			System.out.print("ImageTile Load..." + path + "...");
			images.put(name, new ImageTile(path,tileW,tileH));
			System.out.println("Success");
		} catch (Exception e) {
			System.out.println("Fail");
			e.printStackTrace();
		}
	}
		
	public Image getImage(String name) {
		if(images.containsKey(name))
			return images.get(name);
		return null;
	}
}
