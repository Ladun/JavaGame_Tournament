package com.ladun.engine.gfx;

public class ImageRequest {
	
	public Image image;
	public int zDepth;
	public int offX ,offY;
	public float angle;

	
	public ImageRequest(Image image,int zDepth, int offX,int offY,float angle)
	{
		this.image = image;
		this.zDepth = zDepth;
		this.offX = offX;
		this.offY = offY;
		this.angle = angle;
	}
}
