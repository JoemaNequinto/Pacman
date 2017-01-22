import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;

public class GameCanvas extends JComponent{

	private Game game;

	public GameCanvas() {
	}

    public GameCanvas(Game game) {
		this.game = game;
		addKeyListener(game);
		requestFocus();
    }

	public void setGame(Game game) {
		this.game = game;
		addKeyListener(game);
		requestFocus();
	}

	@Override
	public void paintComponent(Graphics g) {
		game.draw((Graphics2D)g);
	}
}
