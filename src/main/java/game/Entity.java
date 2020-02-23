package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Entity extends Rectangle {

	private static final long serialVersionUID = 1L;
	private int delay = 8;
	private Timer animation;
	
	protected BufferedImage img;

	public Entity(int x, int y, int width, int height, boolean isLiving) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		if (!isLiving) setBounds(x, y, width, height);
	}
	
	protected void setListener(ActionListener listener) {
		animation = new Timer(delay, listener);
	}
	
	protected void startAnimation() {
		animation.start();
	}
	
	protected void stopAnimation() {
		animation.stop();
	}
	
	protected void initImages(String path) {
		
		try (BufferedInputStream file = new BufferedInputStream(getClass().getResourceAsStream(path))) {
			img = ImageIO.read(file);
		} catch (Exception e) {
			System.err.println("Error! Something went wrong!");
			e.printStackTrace();
		}
			
	}
	
	protected void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
	}
	
	protected boolean collision(int x, int y) {
		Rectangle bounds = new Rectangle(x, y, width, height);
		Entity[][] walls = Game.walls;
		
		for (int i = 0; i < walls.length; i++)
			for (int j = 0; j < walls[0].length; j++)
				if ((walls[i][j] != null)  && (bounds.intersects(walls[i][j])))
					return false;
		return true;
	}
	
	protected void abscissaBorder() {
		int maxWidth = Game.WIN_WIDTH - 2;
		
		if (x < -1)
			x = maxWidth;
		else if (x > maxWidth)
			x = -1;
		
		setBounds(x, y, width, height);
	}
	
}
