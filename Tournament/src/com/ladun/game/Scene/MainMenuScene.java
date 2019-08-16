package com.ladun.game.Scene;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.objects.UI.Button;

public class MainMenuScene extends AbstractScene{

	private Button testButton;
	
	
	@Override
	public boolean init(GameContainer gc,GameManager gm, boolean active) {
		// TODO Auto-generated method stub
		this.gm = gm;
		this.active = active;
		this.name = "MainMenu";
		
		testButton = new Button(50, 50, 100,100,0xffad3867);
		objects.add(testButton);
		
		return true;
	}

	@Override
	public void update(GameContainer gc) {
		// TODO Auto-generated method stub
		for(int i = 0; i < objects.size();i++) {
			if(objects.get(i).isActive())
				objects.get(i).update(gc, gm);
		}
		
		if(testButton.isReleased())
		{
			ConnectToServer(gc,gm);
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

	//-------------------------------------------------------------------------------
	private void ConnectToServer(GameContainer gc,GameManager gm) {
		if(gm.isLoading())
			return;
		
		gm.setLoading(true);
		new Thread(new Runnable() {
			double stTime = System.currentTimeMillis();
			

			@Override
			public void run() {
					
				gm.getClient().connect();
				
				double nowTime = 0; 
				
				while(!gm.getClient().isServerRespond()) {
					//System.out.println(gm.getClient().isServerRespond());
					nowTime = (System.currentTimeMillis() - stTime) / 1000; 
					if(nowTime >= 5|| gm.getClient().isServerRespond()) {
						break;
					}
					
				}
				if(nowTime>= 5) {
					//TODO 서버가 닫혀있음
					System.out.println("Server is Closed");
					gm.getClient().disconnect();
				}
				else {
					gm.changeScene("InGame");
					((GameScene)gm.getScene("InGame")).mapLoad(gc,0);
				}
				gm.setLoading(false);
				
			}
			
		},"Connecting-Thread").start();;
	}
	
	//-------------------------------------------------------------------------------
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
