import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Ocaml extends Sprite {
	public static int WIDTH = 30;
	public static int HEIGHT = 40;
	
	public static int XVEL = 0;
	public static int YVEL = 0;
	
	private int frame = 0;

	private BufferedImage[] runFrames = new BufferedImage[3];

	public Ocaml() {
		super(3850, 540, WIDTH, HEIGHT, "");
		try {
			runFrames[0] = ImageIO.read(new File("ocaml0.png"));
			runFrames[1] = ImageIO.read(new File("ocaml1.png"));
			runFrames[2] = ImageIO.read(new File("ocaml2.png"));
		} catch (IOException e) {
			System.out.println("ocaml png missing");
		}
	}
	@Override
	public void draw(Graphics g) {
		g.drawImage(runFrames[(frame / 10 % 3)], x, y, width, height, null);
		frame = frame + 1 % 500;
	}
}