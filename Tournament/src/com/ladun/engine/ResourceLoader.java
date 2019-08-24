package com.ladun.engine;

import java.util.HashMap;

import com.ladun.engine.audio.SoundClip;
import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;

public class ResourceLoader {
	
	private HashMap<String,Image> images = new HashMap<String,Image>();
	private HashMap<String,SoundClip> sounds = new HashMap<String,SoundClip>();
	
	private boolean imageLoad;
	private boolean soundLoad;

	
	public void ImageLoad() {
		if(imageLoad)
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
		addImage("skill_icon", "/UI/SlotContents/SkillIcon.png",64,64);
		
		// Buttons
		addImage("store_button","/UI/StoreButton.png",96,96);
		addImage("job_button","/UI/JobButton.png",96,96);
		for(int i = 1; i <= 3;i++) {
			addImage("jobSelect_button" + i,"/UI/JobSelect/JobSelect" + i + ".png",64,64);
			
		}
		
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
		imageLoad = true;
	}
	
	public void SoundLoad() {
		if(soundLoad)
			return ;
		
		System.out.println("------[ Sound Load ]----------------------");
		addSound("sword_attack", "/audio/sword_attack.wav");
		addSound("bow_attack", "/audio/bow_attack.wav");
		addSound("spear_attack", "/audio/spear_attack.wav");
		addSound("button", "/audio/button.wav");
		
		addSound("bgm", "/audio/bgm.wav");
		System.out.println("------[ Sound Load Finish ]---------------");

		soundLoad = true;		
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
		
	private void addSound(String name, String path) {
		try {
			System.out.print("Sound Load..." + path + "...");
			sounds.put(name, new SoundClip(path));
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
	
	public SoundClip getSound(String name) {
		if(sounds.containsKey(name))
			return sounds.get(name);
		return null;
	}

	public HashMap<String, SoundClip> getSounds() {
		return sounds;
	}
}