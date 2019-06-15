package com.ladun.game.objects;

import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.components.Component;

public abstract class GameObject 
{
	protected String tag;
	protected float posX,posY;
	protected int width, height;
	protected int padding,paddingTop;
	protected boolean active = true;
	
	
	protected ArrayList<Component> components = new ArrayList<Component>();
	
	public abstract void update(GameContainer gc,GameManager gm,float dt);
	public abstract void render(GameContainer gc, Renderer r);
	public abstract void collision(GameObject other);
	
	public void updateComponents(GameContainer gc,GameManager gm,float dt)
	{
		for(Component c : components)
		{
			c.update(gc, gm, dt);
		}
	}
	
	public void renderComponents(GameContainer gc, Renderer r)
	{
		for(Component c : components)
		{
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
	
	public int getPadding() {
		return padding;
	}
	public void setPadding(int padding) {
		this.padding = padding;
	}
	public int getPaddingTop() {
		return paddingTop;
	}
	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
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
	
	
}
