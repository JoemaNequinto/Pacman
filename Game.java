import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class Game implements KeyListener{

	protected boolean over;
	protected String title = "Game";
	protected int width=472, height=658;
	protected int delayPacman = 25;
	
	public void init() {}
	abstract public void update();
	abstract public void draw(Graphics2D g);


	public boolean isOver() { return over; }
	public String getTitle() { return title; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getDelayPacman() { return delayPacman; }

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}
}
