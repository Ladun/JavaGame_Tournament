package com.ladun.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.ladun.engine.gfx.Image;

public class Window {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	private JFrame frame;
	private BufferedImage image;
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics g;
	
	private GameContainer gc ;
	
	public Window(GameContainer gc)
	{
		this.gc =gc;
		
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		canvas = new Canvas();
		Dimension s = new Dimension((int)(gc.getWidth() * gc.getScale()),(int)(gc.getHeight()* gc.getScale()));
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);
		
		frame = new JFrame(gc.getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas,BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				gc.GameExit();
			}
		});
		frame.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		
		canvas.createBufferStrategy(2);
		bs= canvas.getBufferStrategy();
		g = bs.getDrawGraphics();

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Icon.png")));
		
		
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg;
		try {
			cursorImg = ImageIO.read(Image.class.getResourceAsStream("/Cursor.png"));//new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			// Create a new blank cursor.
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			    cursorImg, new Point(0, 0), "blank cursor");
			frame.setCursor(blankCursor);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	    
	}
	public void update(){
		g.drawImage(image,0, 0, canvas.getWidth(), canvas.getHeight(), null);
		gc.getRenderer().processText();
		bs.show();
	}
	public void fullScreen() {
		frame.dispose();
		frame.setUndecorated(true);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
		
		if(screenSize.width / resolution == 16 && screenSize.height / resolution == 9) {
			//canvas.setPreferredSize(screenSize);
			//canvas.setMaximumSize(screenSize);
			//canvas.setMinimumSize(screenSize);
			//gc.setWidth(screenSize.width);
			//gc.setHeight(screenSize.height);

			//image = new BufferedImage(gc.getWidth()	,gc.getHeight(),BufferedImage.TYPE_INT_RGB);
			//gc.getRenderer().init();
		}
		
		frame.setLocation(0, 0);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void setScreenSize(int width, int height, float scale) {
		frame.dispose();
		frame.setUndecorated(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension d= new Dimension((int)(width * scale), (int)(height * scale));
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public Canvas getCanvas() {
		return canvas;
	}
	public JFrame getFrame() {
		return frame;
	}
	public Graphics getG() {
		return g;
	}


}
