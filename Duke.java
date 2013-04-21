import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// import org.imgscalr.Scalr;

public class Duke extends Sprite {
	public static int WIDTH = 30;
	public static int HEIGHT = 40;
	public static int X = 50;
	public static int Y = 20;	
	public static int XVEL = 0;
	public static int YVEL = 0;


	public BufferedImage img;
	private BufferedImage base;
	private int frame = 0;

	private BufferedImage[] runFrames = new BufferedImage[3];

	public Duke(int stageWidth, int stageHeight) {
		super(X, Y, XVEL, YVEL, WIDTH, HEIGHT, stageWidth, stageHeight);
		status = "idle";
		try {
			base = ImageIO.read(new File("duke.png"));
			runFrames[0] = ImageIO.read(new File("run00.png"));
			runFrames[1] = ImageIO.read(new File("run01.png"));
			runFrames[2] = ImageIO.read(new File("run02.png"));
			// img = Scalr.resize(base, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH,
   //             50, 100, Scalr.OP_ANTIALIAS);
		} catch (IOException e) {
			System.out.println("png missing");
		}
	}

	@Override
	public void draw(Graphics g) {
		if (status.equals("runLeft")) {
			this.xVel -= (this.xVel + MAX_VEL) * .6;
		} else if (status.equals("runRight")) {
			this.xVel += (-this.xVel + MAX_VEL) * .6;
		}
		if (status.equals("idle")) {
			g.drawImage(base, x, y, width, height, null);
		} else {
			g.drawImage(runFrames[frame], x, y, width, height, null);
			frame = (frame + 1) % runFrames.length;
		}
	}
}