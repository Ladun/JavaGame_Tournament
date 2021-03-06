package com.ladun.engine;

public class GameContainer implements Runnable{
	
	private Thread thread;
	private Window window;
	private Renderer renderer;
	private Input input;
	private ResourceLoader resourceLoader;
	private AbstractGame game;
	
	private boolean running = false;
	private int width = 320 ,height = 240;
	private float scale = 1f;
	private String title = "Job War";
	
	

	public GameContainer(AbstractGame game)
	{
		this.game = game;
	}
	public synchronized void start(){
		
		window = new Window(this);
		renderer = new Renderer(this);
		resourceLoader = new ResourceLoader() ;
		input = new Input(this);
		
		thread = new Thread(this,"Main Loop");
		thread.start();
		//window.fullScreen();
	}
	
	public synchronized void stop(){
		try {
			thread.join();
		} catch (InterruptedException e) {
			
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
			
			
			
			while(unprocessedTime >= Time.DELTA_TIME)
			{
				unprocessedTime -= Time.DELTA_TIME;
				render = true;
				
				game.update();
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
				game.render(renderer);
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
	
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
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
