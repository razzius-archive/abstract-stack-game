import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Text implements Shape {
	public BufferedImage text;
	public Text(String file) {
		try {
			text = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("png missing");
		}
	}
	@Override
	public void draw(Graphics g) {
		g.drawImage(text, 0, 0, 600, 400, null);
	}
}