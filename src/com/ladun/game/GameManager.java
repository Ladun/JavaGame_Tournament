package com.ladun.game;

import java.util.ArrayList;

import com.ladun.engine.AbstractGame;
import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.objects.GameObject;

public class GameManager extends AbstractGame {

	public static final int TS = 16;

	private ArrayList<GameObject> objects = new ArrayList<GameObject>();

	private boolean[] collision;
	private int levelW, levelH;

	@Override
	public void init(GameContainer gc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
	}

	public int getLevelW() {
		return levelW;
	}

	public int getLevelH() {
		return levelH;
	}

	public GameObject getObject(String tag)
	{
		for(int i = 0; i < objects.size();i++)
		{
			if(objects.get(i).getTag() == tag)
				return objects.get(i);
		}
		
		return null;
	}
	public static void main(String[] args) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.setWidth(960);
		gc.setHeight(720);
		gc.setScale(1f);
		gc.start();
	}
}
