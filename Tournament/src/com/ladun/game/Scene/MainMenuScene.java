package com.ladun.game.Scene;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.objects.UI.Button;

public class MainMenuScene extends AbstractScene{

	private Button testButton;
	
	
	@Override
	public boolean init(GameManager gm, boolean active) {
		// TODO Auto-generated method stub
		this.active = active;
		this.name = "MainMenu";
		
		testButton = new Button(300, 300, 100,100,0xffad3867);
		objects.add(testButton);
		
		return true;
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).isActive())
				objects.get(i).update(gc, gm, dt);
		}
		
		if(testButton.isReleased())
		{
			gm.changeScene("InGame");
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub

		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).isActive())
				objects.get(i).render(gc, r);
		}
	}

	@Override
	public int getLevelW() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLevelH() {
		// TODO Auto-generated method stub
		return 0;
	}

}
