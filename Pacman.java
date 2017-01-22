import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.net.MalformedURLException;

public class Pacman extends Game {
	
	public static void main(String[] args) {
		GameApplication.start(new Pacman());
	}
	final int WALK = 2;
	BufferedImage maze, pacman, red, blue, pink, orange, gameover,ready,winner;
	int requestDir, currentDir;
	
	int framePacman, frameGhost;
	int columns, rows;
	int column, row;
	int rowRed, columnRed, rowBlue, columnBlue, rowPink, columnPink, rowOrange, columnOrange;
	int columnPacman , rowPacman;
	Scorebar scorebar;
	ArrayList<String> lines = new ArrayList<String>();
	char[][] cell;
	int score;
	//int currentDirRed,currentDirBlue,currentDirPink,currentDirOrange;

	public Pacman(){
		try {
			Scanner s = new Scanner(new File("maze/maze.txt"));
			int r = 0;
			while (s.hasNextLine()) {
				String line = s.nextLine();
				lines.add(line);
				if (line.contains("E")) {
					rowPacman = r;
					columnPacman = line.indexOf('E');
				}if (line.contains("F")) {
					rowRed = r;
					columnRed = line.indexOf('F');
				}if (line.contains("G")) {
					rowBlue = r;
					columnBlue = line.indexOf('G');
				}if (line.contains("H")) {
					rowPink = r;
					columnPink = line.indexOf('H');
				}if (line.contains("I")) {
					rowOrange = r;
					columnOrange = line.indexOf('I');
				}
				r++;
			}
			s.close();
			
			rows = lines.size();	//260
			columns = lines.get(0).length();	//228
			
			//width = columns*WALK;
			//height = rows*WALK;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		cell = getCells();
		title = "PACMAN";
		framePacman = 0;
		frameGhost = 0;
		requestDir = KeyEvent.VK_UP;
		currentDir = KeyEvent.VK_UP;

		//currentDirRed = KeyEvent.VK_DOWN;
		//currentDirBlue = KeyEvent.VK_LEFT;
		//currentDirPink = KeyEvent.VK_UP;
		//currentDirOrange = KeyEvent.VK_RIGHT;
		
		try {
			pacman = ImageIO.read(new File("images/pacman.png"));
			maze = ImageIO.read(new File("images/maze1.png"));
			red = ImageIO.read(new File("images/redGhost.png"));
			blue = ImageIO.read(new File("images/blueGhost.png"));
			pink = ImageIO.read(new File("images/pinkGhost.png"));
			orange = ImageIO.read(new File("images/orangeGhost.png"));
			gameover = ImageIO.read(new File("images/gameover.png"));
			ready = ImageIO.read(new File("images/ready.png"));
			winner = ImageIO.read(new File("images/winner.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void playSound(String fileName) throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException{
		File url = new File(fileName);
		Clip clip = AudioSystem.getClip();

		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		clip.open(ais);
		clip.start();
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (37 <= key && key <= 40) {
			requestDir = key;
		}
	}
	@Override
	public void update(){
		framePacman++;
		if (framePacman > 5) {
			framePacman = 0;
		}
		frameGhost++;
		if(frameGhost > 7){
			frameGhost = 0;
		}
		if(check() == true){
			over = true;
		}else{
			over = false;
		}
		if (walk(requestDir) == true) {
			currentDir = requestDir;
		} else {
			walk(currentDir);
		}
		moveRedGhost();
		moveBlueGhost();
		movePinkGhost();
		moveOrangeGhost();
	
		/*
		if(moveRedGhost(requestDir)==true){
			currentDirRed = requestDir;
		}else{
			moveRedGhost(currentDirRed);
		}
		if(moveBlueGhost(requestDir)==true){
			currentDirBlue = requestDir;
		}else{
			moveBlueGhost(currentDirBlue);
		}
		if(movePinkGhost(requestDir)==true){
			currentDirPink = requestDir;
		}else{
			moveOrangeGhost(currentDirPink);
		}
		if(moveOrangeGhost(requestDir)==true){
			currentDirOrange = requestDir;
		}else{
			moveOrangeGhost(currentDirOrange);
		}
		*/
		//eating pills
		if(cell[rowPacman][columnPacman] == 'C'){
			cell[rowPacman][columnPacman] = 'B';
			score +=10;
			// try{
			// 	playSound("sound/pacman_chomp.wav");
			// }catch(LineUnavailableException | UnsupportedAudioFileException | IOException e){
			// 	e.printStackTrace();
			// }
		}else if(cell[rowPacman][columnPacman] == 'D'){
			cell[rowPacman][columnPacman] = 'B';
			score += 50;
			delayPacman = 15;
			new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public synchronized void run() {
		               delayPacman = 25;
			        }
				},
				5000 //3 seconds
			);
		}
		System.out.println(score);
		scorebar = new Scorebar(score);
	}
	private boolean check(){
		if(rowRed == rowPacman && columnRed == columnPacman){
			return true;
		}if(rowBlue == rowPacman && columnBlue == columnPacman){
			return true;
		}if(rowPink == rowPacman && columnPink == columnPacman){
			return true;
		}if(rowOrange == rowPacman && columnOrange == columnPacman){
			return true;
		}
		return false;
	}
	private boolean walk(int requestDir) {
		// current position of packman is (row, column)
		switch (requestDir) {
			case KeyEvent.VK_LEFT: // 37
				if (columnPacman > 0 && charAt(rowPacman, columnPacman-1) != 'A' && charAt(rowPacman, columnPacman-1) != '0') {
					columnPacman -= 1;
					return true;
				}if(columnPacman == 0 && cell[rowPacman][columns-1] == 'B'){
					columnPacman = columns-1;
					return true;
				}
				break;
			case KeyEvent.VK_UP:   // 38
				if (rowPacman > 0 && charAt(rowPacman-1, columnPacman) != 'A' && charAt(rowPacman-1, columnPacman) != '0') {
					rowPacman -= 1;
					return true;
				}
				break;
			case KeyEvent.VK_RIGHT: // 39
				if (columnPacman < columns-1 && charAt(rowPacman, columnPacman+1) != 'A' && charAt(rowPacman, columnPacman+1) != '0') {
					columnPacman += 1;
					return true;
				}if(columnPacman == 227 && cell[rowPacman][227] == 'B'){
					columnPacman = 0;
					return true;
				}
				break;
			case KeyEvent.VK_DOWN:  // 40
				if (rowPacman < rows-1 && charAt(rowPacman+1, columnPacman) != 'A' && charAt(rowPacman+1, columnPacman) != '0') {
					rowPacman += 1;
					return true;
				}
				break;
		}
		return false;
	}

	private char charAt(int row, int column) {
		return lines.get(row).charAt(column);
	}

	public char[][] getCells(){
		char[][] cell = new char[rows][columns];
		for(int r=0; r<rows; r++){
			System.arraycopy(lines.get(r).toCharArray(), 0, cell[r], 0, columns);
		}
		return cell;
	}
	private void moveRedGhost(){
		if(rowRed < rowPacman && charAt(rowRed+1,columnRed) != 'A'){
			rowRed +=1;
		}else if(rowRed < rowPacman && charAt(rowRed, columnRed-1) != 'A'){
			columnRed -= 1;
		}else if(rowRed < rowPacman && charAt(rowRed, columnRed+1) != 'A'){
			columnRed += 1;
		}else if(rowRed < rowPacman && charAt(rowRed-1,columnRed) != 'A'){
			rowRed -=1;
		}
		else if(rowRed > rowPacman && charAt(rowRed-1,columnRed) != 'A'){
			rowRed -= 1;
		}else if(rowRed > rowPacman && charAt(rowRed, columnRed+1) != 'A'){
			columnRed += 1;
		}else if(rowRed > rowPacman && charAt(rowRed,columnRed-1) != 'A'){
			columnRed -= 1;
		}else if(rowRed > rowPacman && charAt(rowRed+1,columnRed) != 'A'){
			rowRed +=1;
		}
		else if(columnRed < columnPacman && charAt(rowRed, columnRed+1) != 'A'){
			columnRed += 1;
		}else if(columnRed < columnPacman && charAt(rowRed-1,columnRed) != 'A'){
			rowRed -=1;
		}else if(columnRed < columnPacman && charAt(rowRed+1,columnRed) != 'A'){
			rowRed +=1;
		}else if(columnRed < columnPacman && charAt(rowRed,columnRed-1) != 'A'){
			columnRed -= 1;	
		}else if(columnRed > columnPacman && charAt(rowRed,columnRed-1) != 'A'){
			columnRed -= 1;
		}else if(columnRed > columnPacman && charAt(rowRed-1,columnRed) != 'A'){
			rowRed -=1;
		}else if(columnRed > columnPacman && charAt(rowRed+1,columnRed) != 'A'){
			rowRed +=1;
		}else if(columnRed > columnPacman && charAt(rowRed, columnRed+1) != 'A'){
			columnRed += 1;
		}
	}

/*
	private void moveRedGhost(){
		if(rowRed < rowPacman){
			if(charAt(rowRed+1,columnRed) != 'A'){
				rowRed +=1;
			}else if(charAt(rowRed, columnRed-1) != 'A'){
				columnRed -= 1;
			}else if(charAt(rowRed, columnRed+1) != 'A'){
				columnRed += 1;
			}else if(charAt(rowRed-1,columnRed) != 'A'){
				rowRed -=1;
			}
		}else if(rowRed > rowPacman){
			if(charAt(rowRed-1,columnRed) != 'A'){
				rowRed -= 1;
			}else if(charAt(rowRed, columnRed+1) != 'A'){
				columnRed += 1;
			}else if(charAt(rowRed,columnRed-1) != 'A'){
				columnRed -= 1;
			}else if(charAt(rowRed+1,columnRed) != 'A'){
				rowRed +=1;
			}
		}else if(columnRed < columnPacman){
			if(charAt(rowRed, columnRed+1) != 'A'){
				columnRed += 1;
			}else if(charAt(rowRed-1,columnRed) != 'A'){
				rowRed -=1;
			}else if(charAt(rowRed+1,columnRed) != 'A'){
				rowRed +=1;
			}else if(charAt(rowRed,columnRed-1) != 'A'){
				columnRed -= 1;
			}
		}else if(columnRed > columnPacman){
			if(charAt(rowRed,columnRed-1) != 'A'){
				columnRed -= 1;
			}else if(charAt(rowRed-1,columnRed) != 'A'){
				rowRed -=1;
			}else if(charAt(rowRed+1,columnRed) != 'A'){
				rowRed +=1;
			}else if(charAt(rowRed, columnRed+1) != 'A'){
				columnRed += 1;
			}
		}
	}*/
	private void moveBlueGhost(){
		if(rowBlue < rowPacman){
			if(charAt(rowBlue+1,columnBlue) != 'A'){
				rowBlue +=1;
			}else if(charAt(rowBlue, columnBlue+1) != 'A'){
				columnBlue += 1;
			}else if(charAt(rowBlue,columnBlue-1) != 'A'){
				columnBlue -= 1;
			}else if(charAt(rowBlue-1,columnBlue) != 'A'){
				rowBlue -= 1;
			}
		}if(rowBlue > rowPacman){
			if(charAt(rowBlue-1,columnBlue) != 'A'){
				rowBlue -= 1;
			}else if(charAt(rowBlue,columnBlue-1) != 'A'){
				columnBlue -= 1;
			}else if(charAt(rowBlue, columnBlue+1) != 'A'){
				columnBlue += 1;
			}else if(charAt(rowBlue+1,columnBlue) != 'A'){
				rowBlue +=1;
			}
		}if(columnBlue < columnPacman){
			if(charAt(rowBlue, columnBlue+1) != 'A'){
				columnBlue += 1;
			}else if(charAt(rowBlue-1,columnBlue) != 'A'){
				rowBlue -= 1;
			}else if(charAt(rowBlue+1,columnBlue) != 'A'){
				rowBlue +=1;
			}else if(charAt(rowBlue,columnBlue-1) != 'A'){
				columnBlue -= 1;
			}
		}if(columnBlue > columnPacman){
			if(charAt(rowBlue,columnBlue-1) != 'A'){
				columnBlue -= 1;
			}else if(charAt(rowBlue-1,columnBlue) != 'A'){
				rowBlue -= 1;
			}else if(charAt(rowBlue+1,columnBlue) != 'A'){
				rowBlue +=1;
			}else if(charAt(rowBlue, columnBlue+1) != 'A'){
				columnBlue += 1;
			}
		}
	}
	private void movePinkGhost(){
		if(rowPink < rowPacman){
			if(charAt(rowPink+1,columnPink) != 'A'){
				rowPink +=1;
			}else if(charAt(rowPink, columnPink+1) != 'A'){
				columnPink += 1;
			}else if(charAt(rowPink,columnPink-1) != 'A'){
				columnPink -= 1;
			}else if(charAt(rowPink-1,columnPink) != 'A'){
				rowPink -= 1;
			}
		}if(rowPink > rowPacman){
			if(charAt(rowPink-1,columnPink) != 'A'){
				rowPink -= 1;
			}else if(charAt(rowPink,columnPink-1) != 'A'){
				columnPink -= 1;
			}else if(charAt(rowPink, columnPink+1) != 'A'){
				columnPink += 1;
			}else if(charAt(rowPink+1,columnPink) != 'A'){
				rowPink +=1;
			}
		}if(columnPink < columnPacman){
			if(charAt(rowPink, columnPink+1) != 'A'){
				columnPink += 1;
			}else if(charAt(rowPink+1,columnPink) != 'A'){
				rowPink +=1;
			}else if(charAt(rowPink-1,columnPink) != 'A'){
				rowPink -= 1;
			}else if(charAt(rowPink,columnPink-1) != 'A'){
				columnPink -= 1;
			}
		}if(columnPink > columnPacman){
			if(charAt(rowPink,columnPink-1) != 'A'){
				columnPink -= 1;
			}else if(charAt(rowPink-1,columnPink) != 'A'){
				rowPink -= 1;
			}else if(charAt(rowPink+1,columnPink) != 'A'){
				rowPink +=1;
			}else if(charAt(rowPink, columnPink+1) != 'A'){
				columnPink += 1;
			}
		}
	}
	private void moveOrangeGhost(){
		if(rowOrange < rowPacman){
			if(charAt(rowOrange+1,columnOrange) != 'A'){
				rowOrange +=1;
			}else if(charAt(rowOrange, columnOrange+1) != 'A'){
				columnOrange += 1;
			}else if(charAt(rowOrange,columnOrange-1) != 'A'){
				columnOrange -= 1;
			}else if(charAt(rowOrange-1,columnOrange) != 'A'){
				rowOrange -= 1;
			}
		}if(rowOrange > rowPacman){
			if(charAt(rowOrange-1,columnOrange) != 'A'){
				rowOrange -= 1;
			}else if(charAt(rowOrange,columnOrange-1) != 'A'){
				columnOrange -= 1;
			}else if(charAt(rowOrange, columnOrange+1) != 'A'){
				columnOrange += 1;
			}else if(charAt(rowOrange+1,columnOrange) != 'A'){
				rowOrange +=1;
			}
		}if(columnOrange < columnPacman){
			if(charAt(rowOrange, columnOrange+1) != 'A'){
				columnOrange += 1;
			}else if(charAt(rowOrange-1,columnOrange) != 'A'){
				rowOrange -= 1;
			}else if(charAt(rowOrange+1,columnOrange) != 'A'){
				rowOrange +=1;
			}else if(charAt(rowOrange,columnOrange-1) != 'A'){
				columnOrange -= 1;
			}
		}if(columnOrange > columnPacman){
			if(charAt(rowOrange,columnOrange-1) != 'A'){
				columnOrange -= 1;
			}else if(charAt(rowOrange-1,columnOrange) != 'A'){
				rowOrange -= 1;
			}else if(charAt(rowOrange+1,columnOrange) != 'A'){
				rowOrange +=1;
			}else if(charAt(rowOrange, columnOrange+1) != 'A'){
				columnOrange += 1;
			}
		}
	}
	/*
	private boolean moveRedGhost(int requestDir) {
		// current position of packman is (row, column)
		switch (requestDir) {
			case KeyEvent.VK_LEFT: // 37
			if (columnRed < columns-1 && charAt(rowRed, columnRed+1) != 'A' && charAt(rowRed, columnRed+1) != '0') {
					columnRed += 1;
					return true;
				}if(columnRed == 227 && cell[rowRed][227] == 'B'){
					columnRed = 0;
					return true;
				}
				break;
				
			case KeyEvent.VK_UP:   // 38
			if (columnRed > 0 && charAt(rowRed, columnRed-1) != 'A' && charAt(rowRed, columnRed-1) != '0') {
					columnRed -= 1;
					return true;
				}if(columnRed == 0 && cell[rowRed][columns-1] == 'B'){
					columnRed = columns-1;
					return true;
				}
				break;
			
			
				
				
			case KeyEvent.VK_RIGHT: // 39
				if (rowPacman > 0 && charAt(rowRed-1, columnRed) != 'A' && charAt(rowRed-1, columnRed) != '0') {
					rowRed -= 1;
					return true;
				}
				break;
			case KeyEvent.VK_DOWN:  // 40
				if (rowRed < rows-1 && charAt(rowRed+1, columnRed) != 'A' && charAt(rowRed+1, columnRed) != '0') {
					rowRed += 1;
					return true;
				}
				break;
				
		}
		return false;
	}

	private boolean moveBlueGhost(int requestDir){
		switch(requestDir){
			case KeyEvent.VK_LEFT: // 37
			if (rowBlue < rows-1 && charAt(rowBlue+1, columnBlue) != 'A' && charAt(rowBlue+1, columnBlue) != '0') {
					rowBlue += 1;
					return true;
				}
				break;
				
			case KeyEvent.VK_UP:   // 38
				if (columnBlue > 0 && charAt(rowBlue, columnBlue-1) != 'A' && charAt(rowBlue, columnBlue-1) != '0') {
					columnBlue -= 1;
					return true;
				}if(columnBlue == 0 && cell[rowBlue][columns-1] == 'B'){
					columnBlue = columns-1;
					return true;
				}
				break;
			case KeyEvent.VK_RIGHT: // 39
			if (rowBlue > 0 && charAt(rowBlue-1, columnBlue) != 'A') {
					rowBlue -= 1;
					return true;
				}
				break;
				
			case KeyEvent.VK_DOWN:  // 40
			if (columnBlue < columns-1 && charAt(rowBlue, columnBlue+1) != 'A' && charAt(rowBlue, columnBlue+1) != '0') {
					columnBlue += 1;
					return true;
				}if(columnBlue == 227 && cell[rowBlue][227] == 'B'){
					columnBlue = 0;
					return true;
				}
				break;
				
		}
		return false;
	}

	private boolean movePinkGhost(int requestDir){
		switch(requestDir){
			case KeyEvent.VK_LEFT: // 37
			if (columnPink < columns-1 && charAt(rowPink, columnPink+1) != 'A' && charAt(rowPink, columnPink+1) != '0') {
					columnPink += 1;
					return true;
				}if(columnPink == 227 && cell[rowPink][227] == 'B'){
					columnPink = 0;
					return true;
				}
				break;
				
			case KeyEvent.VK_UP:   // 38
			if (rowPink < rows-1 && charAt(rowPink+1, columnPink) != 'A' && charAt(rowPink+1, columnPink) != '0') {
					rowPink += 1;
					return true;
				}
				break;
				
			case KeyEvent.VK_RIGHT: // 39
			if (rowPink > 0 && charAt(rowPink-1, columnPink) != 'A') {
					rowPink -= 1;
					return true;
				}
				break;
				
			case KeyEvent.VK_DOWN:  // 40
				if (columnPink > 0 && charAt(rowPink, columnPink-1) != 'A' && charAt(rowPink, columnPink-1) != '0') {
					columnPink -= 1;
					return true;
				}if(columnPink == 0 && cell[rowPink][columns-1] == 'B'){
					columnPink = columns-1;
					return true;
				}
				break;
		}
		return false;
	}

	private boolean moveOrangeGhost(int requestDir){
		switch(requestDir){
			case KeyEvent.VK_LEFT: // 37
			if (columnOrange > 0 && charAt(rowOrange, columnOrange-1) != 'A' && charAt(rowOrange, columnOrange-1) != '0') {
					columnOrange -= 1;
					return true;
				}if(columnOrange == 0 && cell[rowOrange][columns-1] == 'B'){
					columnOrange = columns-1;
					return true;
				}
				break;
			
				
			case KeyEvent.VK_UP:   // 38
			if (rowOrange < rows-1 && charAt(rowOrange+1, columnOrange) != 'A' && charAt(rowOrange+1, columnOrange) != '0') {
					rowOrange += 1;
					return true;
				}
				break;
				
			case KeyEvent.VK_RIGHT: // 39
				if (columnOrange < columns-1 && charAt(rowOrange, columnOrange+1) != 'A' && charAt(rowOrange, columnOrange+1) != '0') {
					columnOrange += 1;
					return true;
				}if(columnOrange == 227 && cell[rowOrange][227] == 'B'){
					columnOrange = 0;
					return true;
				}
				break;
			case KeyEvent.VK_DOWN:  // 40
				if (rowOrange > 0 && charAt(rowOrange-1, columnOrange) != 'A') {
					rowOrange -= 1;
					return true;
				}
				break;
		}
		return false;
	}
	*/
	@Override
	public void draw(Graphics2D g){
		//draw maze
		g.drawImage(maze, 0, 50, null);
		g.setColor(Color.WHITE);
		//draw pills
		for (int r=0; r<rows; r++) {
			for (int c=0; c<columns; c++) {
				if (cell[r][c] == 'C') {
					//pill
					g.fillOval(c*WALK-3, r*WALK+46, 6, 6);
				}else if (cell[r][c] == 'D') {
					//power pill
					g.fillOval(c*WALK-6, r*WALK+43, 12, 12);
				}
			}
		}
		//draw pacman
		g.drawImage(pacman.getSubimage((framePacman/2)*30, (currentDir-37)*30, 28, 28), columnPacman*WALK-14, rowPacman*WALK+36, null);
		//draw ghosts
		g.drawImage(red.getSubimage((frameGhost)*32,0,32,32), columnRed*WALK-14, rowRed*WALK+36, null);
		g.drawImage(blue.getSubimage((frameGhost)*32,0,32,32), columnBlue*WALK-12, rowBlue*WALK+36, null);
		g.drawImage(pink.getSubimage((frameGhost)*32,0,32,32), columnPink*WALK-16, rowPink*WALK+36, null);
		g.drawImage(orange.getSubimage((frameGhost)*32,0,32,32), columnOrange*WALK-19, rowOrange*WALK+36, null);
		//draw scores
		scorebar.draw(g);
		g.drawImage(ready,155,329,null);
		if(score == 2600){
			g.drawImage(winner,15,100,null);
			over = true;
		}
		if(over == true){
			g.drawImage(gameover, 11, 150,null);
		}
	}
}