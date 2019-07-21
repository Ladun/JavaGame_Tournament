package com.ladun.engine;

public class GameContainer implements Runnable{
	
	private Thread thread;
	private Window window;
	private Renderer renderer;
	private Input input;
	private ImageLoader imageLoader;
	private AbstractGame game;
	
	private boolean running = false;
	private final double UPDATE_CAP = 1.0/60.0;
	private int width = 320 ,height = 240;
	private float scale = 1f;
	private String title = "Tournament";
	

	public GameContainer(AbstractGame game)
	{
		this.game = game;
	}
	public synchronized void start(){
		
		window = new Window(this);
		renderer = new Renderer(this);
		imageLoader = new ImageLoader() ;
		input = new Input(this);
		
		thread = new Thread(this,"Main Loop");
		thread.start();
	}
	
	public synchronized void stop(){
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void run(){
		
		running = true;
		
		boolean render = false;
		double firstTime = 0;
		double lastTime = System.nanoTime() / 1000000000.0;
		double passedTime = 0;
		double unprocessedTime = 0;
		
		double frameTime = 0;
		int frames = 0;
		int fps = 0;
		

		int ticks = 0;
		int tps = 0;
		
		if(!game.init(this)) {
			System.out.println("AbstractGame Init Failed!");
			dispose();
			return;
		}
		
		
		while(running){
			render = false;
			
			firstTime = System.nanoTime() / 1000000000.0;
			passedTime = firstTime - lastTime;
			lastTime = firstTime;
			
			unprocessedTime += passedTime;
			frameTime += passedTime;
			
			
			
			while(unprocessedTime >= UPDATE_CAP)
			{
				unprocessedTime -= UPDATE_CAP;
				render = true;
				
				game.update(this, (float)UPDATE_CAP);
				input.update();				
				ticks++;

				
			}
			
			
			if(render)
			{
				if(frameTime >= 1.0){					
					frameTime =0;
					fps = frames;
					frames = 0;		
					
					tps = ticks;
					ticks = 0;
				}				
				renderer.clear();
				game.render(this, renderer);
				renderer.process();
				renderer.setCamX(0);
				renderer.setCamY(0);
				//renderer.drawText("FPS : " + fps,0,3,0xffffffff);
				//renderer.drawText("TPS : " + tps,0,20,0xffffffff);
				window.update();
				frames++;
			}
			else{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
	
					e.printStackTrace();
				}
				
			}
		}
		
		dispose();
	}

	public void GameExit() {
		this.running = false;
	}
	private void dispose(){
		game.dispose();
	}
	
	public ImageLoader getImageLoader() {
		return imageLoader;
	}
	public Renderer getRenderer() {
		return renderer;
	}
	public Input getInput() {
		return input;
	}
	public Window getWindow() {
		return window;
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
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
