import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class CreateMaze{
	public static void main(String[] args) throws IOException{
		//load in the maze strings
		ArrayList<String> lines = new ArrayList<String>();
		Scanner s = new Scanner(new File("maze.txt"));
		while(s.hasNextLine()){
			lines.add(s.nextLine());
		}
		s.close();
		int rows = lines.size();
		int columns = lines.get(0).length();
		int width = columns*2;
		int height = rows*2;
		//draw maze on an image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		for(int r=0; r<rows; r++){
			for(int c=0;c<columns;c++){
				if(lines.get(r).charAt(c) != 'A'){
					g.fillRect(c*2-14,r*2-14,28,28);
				}
			}
		}
		g.dispose();
		//save the image
		ImageIO.write(image,"png",new File("maze.png"));
	}
}