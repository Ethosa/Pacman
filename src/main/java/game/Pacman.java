package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;


public class Pacman extends Entity implements KeyListener, ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private boolean up, down, left, right;
	private int key = 0;
	private int dx = 0, dy = 0;
	private int imgNum = 0;
	private boolean directionSelected;
	private double degree = 0;
	private AffineTransform at;
	private double newDegree = 0;
	private boolean canChangeImg = false;

	public Pacman(int x, int y, int width, int height, JPanel panel) {
		super(x, y, width, height, true);
		this.panel = panel;

		up = down  = right = left = false;
		at = AffineTransform.getTranslateInstance(x, y);
		initImages("/res/pacman" + (imgNum++)  + ".png");
		setListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		at = AffineTransform.getTranslateInstance(x, y);
		move();
		panel.repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		key = e.getKeyCode();

		switch (key) {
			case KeyEvent.VK_UP:
				setAnimConf(0, -1, 3 * Math.PI / 2);
				up = true;
				down = false;
				break;
			case KeyEvent.VK_LEFT:
				setAnimConf(-1, 0, Math.PI);
				left = true;
				right = false;
				break;
			case KeyEvent.VK_DOWN:
				setAnimConf(0, 1, Math.PI / 2);
				down = true;
				up = false;
				break;
			case KeyEvent.VK_RIGHT:
				setAnimConf(1, 0, 0);
				right = true;
				left = false;
				break;
		}
		
		directionSelected = true;
	}

	@Override
	protected void render(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, at, null);
		
		if (imgNum < 3 && canChangeImg)
			initImages("/res/pacman" + (imgNum++)  + ".png");
		else
			imgNum = 0;
		
		abscissaBorder();
	}
	
	private void setAnimConf(int dx, int dy, double degree) {
		this.degree = degree;
		this.dx = dx;
		this.dy = dy;

		startAnimation();
	}
	
	private void move() {
		
		if (directionSelected) {
			if ((up || down) && !collision(x, y+dy)) {
				if (left && collision(x-1, y))
					x--;
				else if (right && collision(x+1, y))
					x++;
			}
			else if ((right || left) && !collision(x+dx, y)) {
				if (up && collision(x, y-1))
					y--;
				else if (down && collision(x, y+1))
					y++;
			}
			else
				directionSelected = false;

			at.rotate(newDegree, 16, 16);
		}
		else if (collision(x+dx, y+dy)) {
			at.rotate(degree, 16, 16);
			canChangeImg = true;
			newDegree = degree;
			x += dx;
			y += dy;
		}
		else {
			canChangeImg = false;
			at.rotate(degree, 16, 16);
			up = down = left = right = false;
		}
		
	}
	
}
