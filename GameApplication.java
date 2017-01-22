import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameApplication {

	public static void start(final Game game) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame(game.getTitle());
				frame.setMinimumSize(new Dimension(game.getWidth(), game.getHeight()));
				frame.setMaximumSize(new Dimension(game.getWidth(), game.getHeight()));
				frame.setPreferredSize(new Dimension(472, 658));
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				Container container = new Container();
				container.setLayout(new BorderLayout());
				JPanel bot = new JPanel();
				bot.setPreferredSize(new Dimension(472, 50));
				bot.setBackground(Color.BLACK);
				frame.add(container);
				GameCanvas canvas = new GameCanvas();
				canvas.setGame(game);
				canvas.setSize(new Dimension(472, 558));
				//container.add(top, BorderLayout.NORTH);
				container.add(bot, BorderLayout.SOUTH);
				container.add(canvas, BorderLayout.CENTER);
				frame.setVisible(true);
				frame.pack();				//added
				canvas.requestFocus();
				new GameLoop(game, canvas).start();
			}
		});
	}
}