import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;
import javax.imageio.ImageIO;

public class Ceiling extends Shape {

	public BufferedImage img;

	public Ceiling(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		try {
			img = ImageIO.read(new File("ceiling.png"));
		} catch (IOException e) {
			System.out.println("png missing");
		}
	}

	public void draw(Graphics g) {
		g.drawImage(img, x, y, width, height, null);
	}
}