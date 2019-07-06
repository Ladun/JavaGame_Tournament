package com.ladun.engine;

import java.util.HashMap;

import com.ladun.engine.gfx.Image;

public class ImageLoader {
	
	private HashMap<String,Image> images = new HashMap<String,Image>();

	public ImageLoader() {
		addImage("HOS", "/HOS.png");
	}
	
	private void addImage(String name, String path) {
		try {
			images.put(name, new Image(path));
			System.out.println("Image Load..." + path + "....Success");
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