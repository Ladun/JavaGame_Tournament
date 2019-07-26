package com.ladun.engine.gfx;

public class ImageRequest {
	
	public Image image;
	public int zDepth;
	public int offX ,offY;
	public float xPivot, yPivot;
	public boolean xMirror, yMirror;
	public float angle;

	
	public ImageRequest(Image image,int zDepth, int offX,int offY,float xPivot, float yPivot,boolean xMirror, boolean yMirror,float angle)
	{
		this.image = image;
		this.zDepth = zDepth;
		this.offX = offX;
		this.offY = offY;
		this.xPivot = xPivot;
		this.yPivot = yPivot;
		this.angle = angle;
	}
}
