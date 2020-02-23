package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int WALL = 0xFF000000;
	private static final int PLAYER = 0xFFFF6A00;
	private static final int ENEMY = 0xFFFF0000;
	private static final int POINT = 0xFFFFFFFF;
	private JFrame window = new JFrame("Pacman");
	private BufferedImage img;
	private int mapWidth;
	private int mapHeight;
	private int[] mapPixels;	
	private int ghostCount;
	private String showInfo;
	private Graphics graph;
	
	protected static final int WIN_WIDTH = 640;
	protected static final int WIN_HEIGHT = 480;
	protected static Enemy[] ghosts = new Enemy[4];
	protected static Pacman pacman;
	protected static Entity[][] walls;
	protected static Points[][] points;
	
	public Game() {
		initMap();
		setFocusable(true);
		addKeyListener(pacman);
		requestFocus();
		windowConf();
	}

	private void windowConf() {

		try {
			ImageIcon icon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/res/pacman2.png")));
			window.setIconImage(icon.getImage());
		} catch (IOException e) {
			e.printStackTrace();
		}

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		setBackground(Color.WHITE);
		window.add(this);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	private void build() {

		for (int i = 0; i < mapWidth; i++)
			for (int j = 0; j < mapHeight; j++) {
				
				if (mapPixels[i + j*mapWidth] == WALL)
					walls[i][j] = new Entity(i*32, j*32, 32, 32, false);
				else if (mapPixels[i + j*mapWidth] == PLAYER)
					pacman = new Pacman(i*32, j*32, 32, 32, this);
				else if (mapPixels[i + j*mapWidth] == ENEMY)
					ghosts[ghostCount++] = new Enemy(i*32, j*32, 32, 32, this);
				else if (mapPixels[i + j*mapWidth] == POINT)
					points[i][j] = new Points(i*32, j*32, 16, 16);
		 
			}
	}
	
	private void initMap() {

		try (BufferedInputStream path = new BufferedInputStream(getClass().getResourceAsStream("/res/map.png"))) {
			img = ImageIO.read(path);
			mapWidth  = img.getWidth();
			mapHeight = img.getHeight();

			ghostCount = 0;
			walls = new Entity[mapWidth][mapHeight];
			points = new Points[mapWidth][mapHeight];
			mapPixels = img.getRGB(0, 0, mapWidth, mapHeight, null, 0, mapWidth);
			
			build();
		} catch (Exception e) {
			System.err.println("Error! Something went wrong!");
			e.printStackTrace();
		}

	}
	
	@Override
	public void paint(Graphics g) {
		this.graph = g;
		super.paint(g);

		paintNotLivingEntities(g);
		paintLivingEntities(g);
		check();
	}
	
	private void paintNotLivingEntities(Graphics g) {
		
		for (int i = 0; i < mapWidth; i++)
			for (int j = 0; j < mapHeight; j++) {
				
				if (mapPixels[i + j*mapWidth] == WALL)
					walls[i][j].render(g);
				else if (mapPixels[i + j*mapWidth] == POINT && points[i][j] != null)
					points[i][j].render(g);
			}
	}
	
	private void paintLivingEntities(Graphics g) {
		ghostCount = 0;
		
		for (int i = 0; i < mapWidth; i++)
			for (int j = 0; j < mapHeight; j++)
				 if (mapPixels[i + j*mapWidth] == ENEMY)
					ghosts[ghostCount++].render(g);
		

		pacman.render(g);
	}
	
	private void check() {
		if (win())
			stopGame();
		if (gameOver())
			stopGame();
	}

	private boolean win() {
		boolean flag = true;
		
		for (int i = 0; i < points.length; i++)
			for (int j = 0; j < points[0].length; j++)
				if (points[i][j] != null)
					flag = false;

		if (flag) showInfo = "You Win!";
		
		return flag;
	}

	private boolean gameOver() {
		
		for (int i = 0; i < ghosts.length; i++)
			if (pacman.intersects(ghosts[i])) {
				showInfo = "Game Over!";
				return true;
			}
		
		return false;
	}
	
	private void stopGame() {
		graph.setColor(Color.RED);
		graph.drawString(showInfo, WIN_WIDTH - 330, WIN_HEIGHT - 40);
		
		setFocusable(false);
		pacman.stopAnimation();
		
		for (int i = 0; i < ghosts.length; i++)
			ghosts[i].stopAnimation();
	}
	
	public static void main(String[] args) {
		new Game();
	}
	
}
