package com.ladun.engine.gfx;

public class StringRequest {
	public String str;
	public float size;
	public int color;
	public int posX, posY;
	
	public StringRequest(String str,int posX, int posY, float size, int color) {
		this.str = str;
		this.posX = posX;
		this.posY = posY;
		this.size = size;
		this.color = color;
	}
}
