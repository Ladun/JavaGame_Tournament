package com.ladun.engine;

import java.util.HashMap;

import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;

public class ImageLoader {
	
	private HashMap<String,Image> images = new HashMap<String,Image>();

	public ImageLoader() {
		addImage("HOS", "/HOS.png");
		addImage("player", "/entity/Character.png",GameManager.TS,GameManager.TS);
		addImage("weapon", "/weapon/Weapon.png",GameManager.TS,GameManager.TS);
		addImage("weapon_attack", "/weapon/Weapon_attack.png",GameManager.TS * 2,GameManager.TS *2);
		addImage("projectile", "/weapon/Projectile.png",64,64);
		
	}
	
	private void addImage(String name, String path) {
		try {
			images.put(name, new Image(path));
			System.out.println("Image Load..." + path + "....Success");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void addImage(String name, String path,int tileW,int tileH) {
		try {
			images.put(name, new ImageTile(path,tileW,tileH));
			System.out.println("ImageTile Load..." + path + "....Success");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public Image getImage(String name) {
		if(images.containsKey(name))
			return images.get(name);
		return null;
	}
}
