import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Scorebar extends Game{
	private int score;
	public Scorebar(int score){
		this.score = score;
	}
	
	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0,0,472,50);
		g.setFont(new Font("Monospaced", Font.BOLD, 20));
		g.setColor(Color.WHITE);
		g.drawString("Score: ", 25, 32);
		g.drawString(String.valueOf(this.score), 100, 32);
		
	}

}
