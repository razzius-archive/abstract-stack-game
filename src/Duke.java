import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Duke extends Sprite {
	public static int WIDTH = 30;
	public static int HEIGHT = 40;
	
	public static int XVEL = 0;
	public static int YVEL = 0;


	public int maxVel = 8;
	public String status = "idle";
	public boolean grounded = false;
	public int jump = -10;
	private BufferedImage base;
	private int frame = 0;

	private BufferedImage[] runFrames = new BufferedImage[3];

	public Duke() {
		super(250, 200, WIDTH, HEIGHT, "idle");
		this.slideFactor = .9;
		try {
			base = ImageIO.read(new File("../img/duke.png"));
			runFrames[0] = ImageIO.read(new File("../img/run00.png"));
			runFrames[1] = ImageIO.read(new File("../img/run01.png"));
			runFrames[2] = ImageIO.read(new File("../img/run02.png"));
		} catch (IOException e) {
			System.out.println("png missing");
		}
	}
	@Override
	public void draw(Graphics g) {
		if (status.equals("runLeft")) {
			this.xVel -= (this.xVel + maxVel) * .6;
		} else if (status.equals("runRight")) {
			this.xVel += (-this.xVel + maxVel) * .6;
		}
		if (status.equals("idle")) {
			g.drawImage(base, x, y, width, height, null);
		} else {
			g.drawImage(runFrames[frame], x, y, width, height, null);
			frame = (frame + 1) % runFrames.length;
		}
	}
}