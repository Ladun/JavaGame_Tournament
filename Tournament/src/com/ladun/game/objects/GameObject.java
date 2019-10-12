package com.ladun.game.objects;

import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.components.Collider;
import com.ladun.game.components.Component;

public abstract class GameObject 
{
	protected String tag;
	protected float posX, posY, posZ;
	protected float angle;
	protected int width, height, hY; // 밑변의 길이,밑변의 길이, y축으로의 높이
	protected int pL,pR,pT,pB; // padding Left, Right, Top, Bottom
	protected boolean active = true;
	
	
	protected ArrayList<Component> components = new ArrayList<Component>();
	
	public abstract void update(GameContainer gc,GameManager gm);
	public abstract void render(GameContainer gc, Renderer r);
	public abstract void collision(GameObject other);
	
	public void updateComponents(GameContainer gc,GameManager gm)
	{
		for(Component c : components)
		{
			if(c.isEnable())
				c.update(gc, gm);
		}
	}
	
	public void renderComponents(GameContainer gc, Renderer r)
	{
		for(Component c : components)
		{
			if(c.isEnable())
				c.render(gc, r);
		}
	}
	
	public void addComponent(Component c)
	{
		components.add(c);
	}
	
	public void removeComponent(String tag)
	{
		for(int i = 0; i < components.size();i++)
		{
			if(components.get(i).getTag().equalsIgnoreCase(tag))
				components.remove(i);
		}
	}
	
	public Component findComponent(String tag)
	{
		for(int i = 0; i < components.size();i++)
		{
			if(components.get(i).getTag().equalsIgnoreCase(tag))
				return components.get(i);
		}
		return null;
	}
	
	public Collider getCollider() {
		for(int i = 0; i < components.size();i++)
		{
			if(components.get(i) instanceof Collider)
				return (Collider)components.get(i);
		}
		return null;
	}


	public float getCenterX() {
		return posX + pL + (width - pL - pR) / 2;
	}
	public float getCenterZ() {
		return posZ + pT + (height - pT - pB) / 2;		
	}
	public void setCenterX(float x) {
		this.posX = x - pL + (width - pL - pR) / 2;
	}
	public void setCenterZ(float z) {
		this.posZ = z - pT + (height - pT - pB) / 2;
	}
	
	public float getAngle() {
		return angle;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
	public int getpL() {
		return pL;
	}
	public int getpR() {
		return pR;
	}
	public int getpT() {
		return pT;
	}
	public int getpB() {
		return pB;
	}
	public GameObject setpL(int pL) {
		this.pL = pL;
		return this;
	}
	public GameObject setpR(int pR) {
		this.pR = pR;
		return this;
	}
	public GameObject setpT(int pT) {
		this.pT = pT;
		return this;
	}
	public GameObject setpB(int pB) {
		this.pB = pB;
		return this;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void addPosX(float _x) {
		this.posX += _x;
	}
	public void addPosY(float _y) {
		this.posY += _y;
	}
	public void addPosZ(float _z) {
		this.posZ += _z;
	}
	public float getPosX() {
		return posX;
	}
	public void setPosX(float posX) {
		this.posX = posX;
	}
	public float getPosY() {
		return posY;
	}
	public void setPosY(float posY) {
		this.posY = posY;
	}
	public float getPosZ() {
		return posZ;
	}
	public void setPosZ(float posZ) {
		this.posZ = posZ;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int gethY() {
		return hY;
	}
	
	
}
