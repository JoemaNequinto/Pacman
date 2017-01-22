public class GameLoop extends Thread {

	private final Game game;
	private final GameCanvas canvas;
    private boolean stopped;
    private boolean paused;

	public GameLoop(Game game, GameCanvas canvas) {
		this.game = game;
		this.canvas = canvas;
        this.stopped = false;
        this.paused = false;
	}

    public void pauseGame() {
        this.paused = true;
    }

    public void resumeGame() {
        this.paused = false;
    }

    public void stopGame() {
        stopped = true;
    }

	@Override
	public synchronized void run() {
		game.init();

		while (!game.isOver() && !stopped) {

            if (!paused) {
                game.update();
                canvas.repaint();
            }

			try {
				Thread.sleep(game.getDelayPacman());
				//Thread.sleep(game.getDelayGhost());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
