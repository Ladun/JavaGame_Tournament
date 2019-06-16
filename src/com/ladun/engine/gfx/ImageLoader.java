package com.ladun.engine.gfx;

import java.util.HashMap;

public class ImageLoader {
	
	private HashMap<String,Image> images = new HashMap<String,Image>();

	public ImageLoader() {
		
	}
		
	public Image getImage(String name) {
		if(images.containsKey(name))
			return images.get(name);
		return null;
	}
	
}
