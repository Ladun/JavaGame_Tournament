package com.ladun.engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.ladun.engine.gfx.Font;
import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageRequest;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.engine.gfx.Light;
import com.ladun.engine.gfx.LightRequest;

public class Renderer {
	
	public final static int LAYER_UI = Integer.MAX_VALUE;
	
	private Font font = Font.STANDARD;
	private ArrayList<ImageRequest> imageRequests = new ArrayList<ImageRequest>();
	private ArrayList<LightRequest> lightRequests = new ArrayList<LightRequest>();	
	
	
	private int pW,pH;
	private int[] p;
	private int[] zb;
	private int[] lm;
	private int[] lb;
	
	private int ambientColor = 0xff232323;
	private int zDepth = 0;
	private boolean processing = false;
	private int camX, camY;
	
	public Renderer(GameContainer gc){
		
		
		pW = gc.getWidth();
		pH = gc.getHeight();
		p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData(); 
		zb = new int[p.length];
		lm = new int[p.length];
		lb = new int[p.length];
	}
	
	public void clear(){
		for(int i =0; i < p.length;i++)
		{
			p[i] = 0;
			zb[i] = 0;
			lm[i] = ambientColor;
			lb[i] = 0;
		}
	}
	
	public void process(){
		processing = true;
		
		Collections.sort(imageRequests,new Comparator<ImageRequest>(){

			@Override
			public int compare(ImageRequest i0, ImageRequest i1) {
				// TODO Auto-generated method stub
				if(i0.zDepth < i1.zDepth)
					return 1;
				if(i0.zDepth > i1.zDepth)
					return -1;
				return 0;
			}
		});
		
		for(int i = 0; i < imageRequests.size();i++)
		{
			ImageRequest ir = imageRequests.get(i);
			setzDepth(ir.zDepth);
			drawImage(ir.image,ir.offX,ir.offY,ir.angle);
		}
		
		//Draw lighting
		for(int i = 0; i < lightRequests.size();i++)
		{
			LightRequest l = lightRequests.get(i);
			this.drawLightRequest(l.light, l.locX, l.locY);
		}
		
		
		
		for(int i = 0; i< p.length;i++)
		{
			
			float r = ((lm[i] >> 16) & 0xff) /255f;
			float g = ((lm[i] >> 8) & 0xff) /255f;
			float b = (lm[i] & 0xff) /255f;
			
			
			p[i] = ((int)(((p[i] >> 16) & 0xff) * r) << 16 | (int)(((p[i] >> 8) & 0xff) * g) << 8 | (int)((p[i] & 0xff) * b));
		}
		
		imageRequests.clear();
		lightRequests.clear();
		processing = false;
	}
	
	public void setPixel(int x,int y, int value){

		int alpha = ((value >> 24) & 0xff);
		
		if(x < 0 || x>= pW || y < 0 || y >= pH || alpha == 0)
			return;
		
		int index = x + y * pW;
		
		if(zb[index] > zDepth)
			return;
		zb[index] = zDepth;
		
		if(alpha == 255)	
		{
			p[index] = value;
		}
		else{
			int pixelColor = p[index];
			
			int newRed = ((pixelColor >> 16) & 0xff) - (int)((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * alpha /255f);
			int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * alpha /255f);
			int newBlue = ((pixelColor) & 0xff) - (int)((((pixelColor) & 0xff) - ((value) & 0xff)) * alpha /255f);
			
			p[index] = (newRed << 16 | newGreen << 8 | newBlue);
			
		}
	}
	
	public void setLightMap(int x,int y,int value)
	{
		
		if(x < 0 || x>= pW || y < 0 || y >= pH )
			return;
		
		int baseColor = lm[x+ y * pW];
		
		int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff); 
		int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff); 
		int maxBlue = Math.max(baseColor & 0xff, value & 0xff); 
		
		
		
		lm[x + y * pW] = ( maxRed << 16 | maxGreen << 8 | maxBlue);
		
		
	}

	public void setLightBlock(int x,int y,int value)
	{
		if(x < 0 || x>= pW || y < 0 || y >= pH )
			return;
		
		
		if(zb[x+y*pW] > zDepth)
			return;
		
		lb[x + y * pW] = value;
		
		
	}
	//------------------------------------------------------------------------------------------------------------------------------
	public void drawText(String text,int offX,int offY, int color)
	{

		offX -= camX;
		offY -= camY;
		int offset = 0;
		
		for(int i = 0; i < text.length();i++)
		{
			int unicode = text.codePointAt(i);
			
			
			for(int y = 0; y < font.getFontImage().getH();y++)
			{
				for(int x = 0 ; x < font.getWidths()[unicode];x++)
				{
					if(font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff){
						setPixel(x + offX + offset,y + offY,color);
					}
				}
			}
			
			offset += font.getWidths()[unicode];
		}
	}
	
	//------------------------------------------------------------------------------------------------------------------------------
	public void drawImage(Image image, int offX, int offY, float angle){

		offX -= camX;
		offY -= camY;
		if(image.isAlpha() && !processing)
		{
			imageRequests.add(new ImageRequest(image,zDepth,offX,offY,angle));
			return;
		}
		
		//Don't Render code
		if(offX < -image.getW()) return;
		if(offY < -image.getH()) return;
		if(offX >= pW) return;
		if(offY >= pH) return;
		
		int newX = 0;
		int newY = 0;
		int newWidth = image.getW();
		int newHeight = image.getH();

		double c = Math.cos(Math.toRadians(angle)); // 1
		double s = Math.sin(Math.toRadians(angle)); // 0			

		int hw = newWidth /2;
		int hh = newHeight /2;
		int cx = offX + hw;
		int cy = offY + hh;
		
		//Clipping code
		if(offX < 0){newX -= offX;}
		if(offY < 0){newY -= offY;}
		if(newWidth + offX >= pW){newWidth -= (newWidth + offX - pW);}
		if(newHeight + offY >= pH){newHeight -= (newHeight + offY - pH);}
		
		for(int y = newY; y < newHeight;y++)
		{
			for(int x = newX; x < newWidth;x++)
			{
				setPixel(cx + (int)((x-hw) * c - (y-hh) * s),
						 cy + (int)((x-hw) *s + (y-hh) * c),
						 image.getP()[x + y *image.getW()]);
				
				setLightBlock(cx + (int)((x-hw) * c - (y-hh) * s),
						 	  cy + (int)((x-hw) *s + (y-hh) * c),
							  image.getLightBlock());
			}
		}
	}
	
	public void drawImageTile(ImageTile image,int offX,int offY,int tileX,int tileY,float angle)
	{
		offX -= camX;
		offY -= camY;

		if(image.isAlpha() && !processing)
		{
			imageRequests.add(new ImageRequest(image.getTileImage(tileX, tileY),zDepth,offX,offY,angle));
			return;
		}
		
		//Don't Render code
		if(offX < -image.getTileW()) return;
		if(offY < -image.getTileH()) return;
		if(offX >= pW) return;
		if(offY >= pH) return;
				
		int newX = 0;
		int newY = 0;
		int newWidth = image.getTileW();
		int newHeight = image.getTileH();

		double c = Math.cos(Math.toRadians(angle)); // 1
		double s = Math.sin(Math.toRadians(angle)); // 0			

		int hw = newWidth /2;
		int hh = newHeight /2;
		int cx = offX + hw;
		int cy = offY + hh;
				
				
		//Clipping code
		if(offX < 0){newX -= offX;}
		if(offY < 0){newY -= offY;}
		if(newWidth + offX >= pW){newWidth -= (newWidth + offX - pW);}
		if(newHeight + offY >= pH){newHeight -= (newHeight + offY - pH);}
				
		for(int y = newY; y < newHeight;y++)
		{
			for(int x = newX; x < newWidth;x++)
			{
				setPixel(cx + (int)((x - hw) * c - (y - hh) * s),
						 cy + (int)((x - hw) * c + (y - hh) * s),
						 image.getP()[(x +tileX * image.getTileW())+ (y + tileY * image.getTileH()) *image.getW()]);
				
				setLightBlock(cx + (int)((x - hw) * c - (y - hh) * s),
						 	  cy + (int)((x - hw) * c + (y - hh) * s),
							  image.getLightBlock());
			}
		}
	}

	//------------------------------------------------------------------------------------------------------------------------------
	public void drawRect(int offX,int offY,int width, int height, float angle,int color)
	{
		offX -= camX;
		offY -= camY;

		if(offX < -width) return;
		if(offY < -height) return;
		if(offX >= pW) return;
		if(offY >= pH) return;
		
		double c = Math.cos(Math.toRadians(angle));
		double s = Math.sin(Math.toRadians(angle));

		int hw = width /2;
		int hh = height /2;
		int cx = offX + hw;
		int cy = offY + hh;
		for(int y = -hh; y <= hh;y++)
		{					
			setPixel((int)(cx + (-hw *c - y * s))	,
					 (int)(cy + (-hw *s + y * c)),
					 color);
			setPixel((int)(cx + (hw *c - y * s))	,
					 (int)(cy + (hw *s + y * c)),
					 color);
		}
		for(int x = -hw; x <= hw;x++)
		{
			setPixel((int)(cx + (x *c - -hh * s))	,
					 (int)(cy + (x *s + -hh * c)),
					 color);
			setPixel((int)(cx + (x *c - hh * s))	,
					 (int)(cy + (x *s + hh * c)),
					 color);
		}  
	}
	
	public void drawFillRect(int offX,int offY,int width, int height, float angle,int color)
	{
		offX -= camX;
		offY -= camY;
		
		//Don't Render code
		if(offX < -width) return;
		if(offY < -height) return;
		if(offX >= pW) return;
		if(offY >= pH) return;		
		
		double c = Math.cos(Math.toRadians(angle)); // 1
		double s = Math.sin(Math.toRadians(angle)); // 0			

		int hw = width /2;
		int hh = height /2;
		int cx = offX + hw;
		int cy = offY + hh;
		
		for(int y = -hh; y <= hh;y++)
		{
			for(int x = -hw; x <= hw;x++)
			{
				setPixel(cx + (int)(x * c - y * s),
						cy + (int)(x * s + y * c),
						color);
			}
		}
	}

	public void drawCircle(int cx,int cy,int r, int color)
 {
		cx -= camX;
		cy -= camY;

		if(cx < -r) return;
		if(cy < -r) return;
		if(cx >= pW + r) return;
		if(cy >= pH + r) return;

		int x = 0;
		int y = r;
		int p = 1- r;
		
		setPixel(cx		 , r+cy		, color);
		setPixel(cx		 , -r+ cy	, color);
		setPixel(r + cx	 , cy		, color);
		setPixel(-r + cx , cy		, color);
		
		while(x < y) {
			x++;
			if(p < 0) {
				p += x + x + 1;
			}
			else {
				p += x + x + 1 - y - y;
				y--;
			}
			setPixel(x + cx		, y+cy		, color);
			setPixel(x + cx		, -y+ cy	, color);
			setPixel(-x + cx	, y+cy		, color);
			setPixel(-x + cx	, -y+ cy	, color);
			setPixel(y + cx		, x+cy		, color);
			setPixel(y + cx		, -x+ cy	, color);
			setPixel(-y + cx	, x+cy		, color);
			setPixel(-y + cx	, -x+ cy	, color);
		}
		
	}
	
	public void drawFillCircle(int cx,int cy,int r, int color) {
		cx -= camX;
		cy -= camY;

		if(cx < -r) return;
		if(cy < -r) return;
		if(cx >= pW + r) return;
		if(cy >= pH + r) return;

		int x = 0;
		int y = r;
		int p = 1- r;
		
		setPixel(cx			, r+cy		, color);
		setPixel(cx			, -r+ cy	, color);
		setPixel(r + cx		, cy		, color);
		setPixel(-r + cx	, cy		, color);
		
		while(x < y) {
			x++;
			if(p < 0) {
				p += x + x + 1;
			}
			else {
				p += x + x + 1 - y - y;
				y--;

				setPixel(cx			, y+cy		, color);
				setPixel(cx			, -y+ cy	, color);
				setPixel(y + cx		, cy		, color);
				setPixel(-y + cx	, cy		, color);
				for(int xx =1; xx < x;xx++) {
					setPixel(xx + cx	, y+cy		, color);
					setPixel(xx + cx	, -y+ cy	, color);
					setPixel(-xx + cx	, y+cy		, color);
					setPixel(-xx + cx	, -y+ cy	, color);
					setPixel(y + cx		, xx+cy		, color);
					setPixel(y + cx		, -xx+ cy	, color);
					setPixel(-y + cx	, xx+cy		, color);
					setPixel(-y + cx	, -xx+ cy	, color);
				}
			}
			setPixel(x + cx		, y+cy		, color);
			setPixel(x + cx		, -y+ cy	, color);
			setPixel(-x + cx	, y+cy		, color);
			setPixel(-x + cx	, -y+ cy	, color);
			setPixel(y + cx		, x+cy		, color);
			setPixel(y + cx		, -x+ cy	, color);
			setPixel(-y + cx	, x+cy		, color);
			setPixel(-y + cx	, -x+ cy	, color);
		}
		
		for(int i = -y+1; i < y;i++) {
			for(int j = -y+1; j < y;j++) {
				setPixel(i + cx	, j+ cy	, color);
			}
		}
		
	}
	
	public void drawElipse(int cx, int cy,int w,int h,int color) {
		cx -= camX;
		cy -= camY;

		if(cx < -w) return;
		if(cy < -h) return;
		if(cx >= pW + w) return;
		if(cy >= pH + h) return;
		
		int x = 0;
		int y = h;
		int w2 = w * w;
		int h2 = h * h;
		int p = (4 * h2 + w2 *(1 - 4 * h))/ 4;

		setPixel(cx, h + cy, color);
		setPixel(cx, -h + cy, color);
		setPixel(w + cx, cy, color);
		setPixel(-w + cx, cy, color);
		
				
		// x �������� ����
		while(h2* x < w2 * (y-1)) {
			++x;
			if( p < 0) {
				p+= h2 * (2 * x + 1);
			}
			else {
				--y;			
				p+= h2 * (2 * x + 1) - 2 * w2* y;	
			}
			
			setPixel(x + cx, y + cy, color);
			setPixel(-x + cx, y + cy, color);
			setPixel(x + cx, -y + cy, color);
			setPixel(-x + cx, -y + cy, color);
		}
		
		x = w;
		y = 0;
		p = (4 * w2 + h2 * (1- 4*w))/4;		
		
		// y �������� ����
		while(h2* x > w2 * y ) {
			++y;
			if( p < 0) {
				p+= w2 * (2 * y + 1);
			}
			else {		
				--x;		
				p+= w2 * (2 *y + 1) - 2 * h2 * x;
			}
			
			setPixel(x + cx, y + cy, color);
			setPixel(-x + cx, y + cy, color);
			setPixel(x + cx, -y + cy, color);
			setPixel(-x + cx, -y + cy, color);
		}
	}
	
	public void drawFillElipse(int cx, int cy,int w,int h,int color) {
		cx -= camX;
		cy -= camY;

		if(cx < -w) return;
		if(cy < -h) return;
		if(cx >= pW + w) return;
		if(cy >= pH + h) return;
		
		int x = 0;
		int y = h;
		int w2 = w * w;
		int h2 = h * h;
		int p = (4 * h2 + w2 *(1 - 4 * h))/ 4;

		setPixel(cx, h + cy, color);
		setPixel(cx, -h + cy, color);
		setPixel(w + cx, cy, color);
		setPixel(-w + cx, cy, color);
		
				
		// x �������� ����
		while(h2* x < w2 * (y-1)) {
			++x;
			if( p < 0) {
				p+= h2 * (2 * x + 1);
			}
			else {
				--y;
				p+= h2 * (2 * x + 1) - 2 * w2* y;	
				
				setPixel(cx, y + cy, color);
				setPixel(cx, -y + cy, color);
				for(int xx= 1; xx < x; xx++) {
					setPixel(xx + cx, y + cy, color);
					setPixel(-xx + cx, y + cy, color);
					setPixel(xx + cx, -y + cy, color);
					setPixel(-xx + cx, -y + cy, color);
				}
			}
			
			setPixel(x + cx, y + cy, color);
			setPixel(-x + cx, y + cy, color);
			setPixel(x + cx, -y + cy, color);
			setPixel(-x + cx, -y + cy, color);
		}
		
		x = w;
		y = 0;
		p = (4 * w2 + h2 * (1- 4*w))/4;		
		
		// y �������� ����
		while(h2* x > w2 * (y+1)) {
			++y;
			if( p < 0) {
				p+= w2 * (2 * y + 1);
			}
			else {
				--x;
				p+= w2 * (2 *y + 1) - 2 * h2 * x;	
				
				setPixel(x + cx, cy, color);
				setPixel(-x + cx, cy, color);
				for(int yy= 1; yy < y; yy++) {
					setPixel(x + cx, yy + cy, color);
					setPixel(-x + cx, yy + cy, color);
					setPixel(x + cx, -yy + cy, color);
					setPixel(-x + cx, -yy + cy, color);
				}			
			}
			
			setPixel(x + cx, y + cy, color);
			setPixel(-x + cx, y + cy, color);
			setPixel(x + cx, -y + cy, color);
			setPixel(-x + cx, -y + cy, color);
		}
		
		
		for(int i = -x +1 ; i < x ; i++)
			for(int j = -y ; j <= y; j++)
				setPixel(i + cx, j + cy, color);
	}
	
	//------------------------------------------------------------------------------------------------------------------------------
	
	public void drawLight(Light l, int offX,int offY)
	{
		lightRequests.add(new LightRequest(l,offX,offY));
	}
	
	private void drawLightRequest(Light l,int offX,int offY)
	{

		offX -= camX;
		offY -= camY;
		for(int i = 0; i < l.getDiameter();i++)
		{
			drawLightLine(l, l.getRadius(),l.getRadius(), i,0,offX,offY);
			drawLightLine(l, l.getRadius(),l.getRadius(), i, l.getDiameter(),offX,offY);
			drawLightLine(l, l.getRadius(),l.getRadius(), 0, i ,offX,offY);
			drawLightLine(l, l.getRadius(),l.getRadius(), l.getDiameter(),i,offX,offY);
		}
	}
	
	private void drawLightLine(Light l, int x0,int y0, int x1 ,int y1,int offX,int offY)
	{
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		
		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;
		
		int err = dx - dy;
		int e2;
		
		while(true){
			int screenX = x0 - l.getRadius() + offX;
			int screenY = y0 - l.getRadius() + offY;
			
			if(screenX < 0 || screenX >= pW || screenY < 0 || screenY >= pH)
				return;
			
			int lightColor = l.getLightValue(x0, y0);
			if(lightColor == 0)
				return;
			
			
			if(lb[screenX + screenY * pW] == Light.FULL)
				return;
			
			setLightMap(screenX ,screenY,lightColor);
			
			if(x0 == x1 && y0 == y1)
				break;
			
			e2 = 2 * err;
			
			if(e2 > -1 * dy)
			{
				err -= dy;
				x0 += sx;
				
			}
			
			if(e2 < dx)
			{
				err += dx;
				y0 += sy;
			}
		}
	}
	
	//------------------------------------------------------------------------------------------------------------------------------
	public int getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(int ambientColor) {
		this.ambientColor = ambientColor;
	}

	public int getCamX() {
		return camX;
	}

	public void setCamX(int camX) {
		this.camX = camX;
	}

	public int getCamY() {
		return camY;
	}

	public void setCamY(int camY) {
		this.camY = camY;
	}

	public int getzDepth() {
		return zDepth;
	}

	public void setzDepth(int zDepth) {
		this.zDepth = zDepth;
	}
}