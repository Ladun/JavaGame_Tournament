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
		
		addImage("point", "/Point.png");
		addImage("player", "/entity/Character.png",GameManager.TS,GameManager.TS);
		
		//Weapon Load
		addImage("sword", "/weapon/Sword.png");
		addImage("bow", "/weapon/Bow.png");
		addImage("spear", "/weapon/Spear.png");
		addImage("projectile", "/weapon/Projectile.png",64,64);	
		
		//UI Image Load
		addImage("bar", "/UI/Bar.png",334,13);
		addImage("slot", "/UI/Slot.png");
		addImage("triangleButton__up", "/UI/TriangleButton_Up.png",52,52);
		
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
