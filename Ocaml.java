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

	public int MAX_VEL = 10;
	
	private int frame = 0;

	private BufferedImage[] runFrames = new BufferedImage[2];

	public Ocaml() {
		super(100, 100, WIDTH, HEIGHT, "");
		try {
			runFrames[0] = ImageIO.read(new File("ocaml0.png"));
			runFrames[1] = ImageIO.read(new File("ocaml1.png"));
		} catch (IOException e) {
			System.out.println("ocaml png missing");
		}
	}
	@Override
	public void draw(Graphics g) {
		g.drawImage(runFrames[frame], x, y, width, height, null);
		frame = (frame + 1) % runFrames.length;
	}
}