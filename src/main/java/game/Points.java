package game;

import java.awt.Graphics;

public class Points extends Entity {

	private static final long serialVersionUID = 1L;

	public Points(int x, int y, int width, int height) {
		super(x+5, y+5, width, height, false);
	}
	
	@Override
	protected void render(Graphics g) {
		g.drawOval(x+2, y+2, width, height);
		eat(g);
	}
	
	private void eat(Graphics g) {
		Pacman player = Game.pacman;
		Points[][] points = Game.points;

		for (int i = 0; i < points.length; i++)
			for (int j = 0; j < points[0].length; j++)
				if (points[i][j] != null && player.intersects(points[i][j])) {
					g.clearRect(x, y, width + 5, height + 5);
					points[i][j] = null;
				}

		}
	
}
