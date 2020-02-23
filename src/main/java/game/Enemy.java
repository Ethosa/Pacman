package game;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;


public class Enemy extends Entity implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private int xSteps, ySteps;
	private int dir = 0;
	
	public Enemy(int x, int y, int width, int height, JPanel panel) {
		super(x, y, width, height, true);
		this.panel = panel;
		
		dir = (int)(Math.random() * 4);
		
		setListener(this);
		startAnimation();
	}
	
	private void choiceDirection() {
		
		// Direction	
		switch(dir) {
			case 0:															// <-- Left
				ghostMove(-1, 0);
				break;
			case 1:			   												// <-- Right
				ghostMove(1, 0);
				break;
			case 2:			  												// <-- Up
				ghostMove(0, -1);
				break;
			case 3:			 												// <-- Down
				ghostMove(0, 1);
				break;
		}
		
	}
	
	private void ghostMove(int dx, int dy) {
		
		if (collision(x+dx, y+dy)) {
			x += dx;
			y += dy;
		}
		
		if ((collision(x+dy, y+dx) || collision(x+dy*(-1), y+dx*(-1))) && !detectPlayer(dx, dy))
			dir = (int)(Math.random() * 4);
	}
	
	private boolean detectPlayer(int dx, int dy) {
		boolean flag = false;
		int xPlayer = Game.pacman.x;
		int yPlayer = Game.pacman.y;
		xSteps = x;
		ySteps = y;
		
		while((dx != 0) && collision(xSteps, y) && !flag) {
			if (xSteps == xPlayer && ySteps == yPlayer)
				flag =  true;
			xSteps += dx;
		}
		while((dy != 0) && collision(x, ySteps) && !flag) {
			if (ySteps == yPlayer && xSteps == xPlayer)
				flag = true;
			ySteps += dy;
		}

		return flag;
	}
	
	@Override
	protected void render(Graphics g) {
		g.drawImage(img, x, y, null);
		abscissaBorder();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		initImages("/res/ghost" + dir + ".png");
		choiceDirection();
		panel.repaint();
	}
	
}
