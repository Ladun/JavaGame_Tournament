package com.ladun.game.Scene;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Image;
import com.ladun.game.Announce;
import com.ladun.game.GameManager;
import com.ladun.game.net.Client.ServerState;
import com.ladun.game.objects.UI.Button;

public class MainMenuScene extends AbstractScene{

	private Button testButton;
	
	private Image backgroundImage;
	
	
	@Override
	public boolean init(GameContainer gc,GameManager gm, boolean active) {
		// TODO Auto-generated method stub
		this.gm = gm;
		this.active = active;
		this.name = "MainMenu";
		
		backgroundImage = new Image("/UI/mainScene/mainScene.png");
		
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
		r.drawImage(backgroundImage, 0, 0, 0);
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
					synchronized (gm) {
						nowTime = (System.currentTimeMillis() - stTime) / 1000; 
						if(nowTime >= 5|| gm.getClient().isServerRespond()) {
							break;
						}
					
					}
					
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				synchronized (gm) {
					if(gm.getClient().getServerState() != ServerState.GAME_START) {
						if(nowTime>= 5) {
							//TODO 서버가 닫혀있음
							//System.out.println("Server is Closed");
							gm.getClient().disconnect();
							gm.getAnnounce().addString("Server is Closed",Announce.DEFAULT_COLOR);
							gm.getAnnounce().show(5);
						}
						else {
							gm.changeScene("InGame");
							((GameScene)gm.getScene("InGame")).mapLoad(gc,0);
						}
					}
					gm.setLoading(false);
				}
				
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
