import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class LevelOne extends JPanel {
	private Duke player;
	private JLabel status;

	private boolean playing = false;
	public static final int LEVEL_WIDTH = 600;
	public static final int LEVEL_HEIGHT = 400;
	public static final int INTERVAL = 20;
	private BufferedImage bg;

	private double gravity = 1.0;

	public LevelOne(JLabel status) {
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start();
		setFocusable(true);

		try {
			bg = ImageIO.read(new File("bg.png"));
		} catch (IOException e) {
			System.out.println("png missing");
		}
		addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==65) {
					player.status = "runLeft";
				} else if(e.getKeyCode()==68) {
					player.status = "runRight";
				} else if(e.getKeyCode()==87) {
					player.yVel = -20;
				}
				System.out.println(e.getKeyCode());
			}
			@Override
			public void keyTyped(KeyEvent e) {
				// System.out.println("keyTyped: [" + e + "].");
			}
			public void keyReleased(KeyEvent e){
				if(e.getKeyCode()==68 || e.getKeyCode()==65) {
					player.status = "idle";
				}
			}
		});
		this.status = status;
	}

	
	public void reset() {

		player = new Duke(LEVEL_WIDTH, LEVEL_HEIGHT);
		floor = new Platform(LEVEL_WIDTH, LEVEL_HEIGHT);
		playing = true;
		status.setText("Running...");

		requestFocusInWindow();
	}

	void tick() {
		player.move();
		player.yVel += gravity;
		// String w = player.yVel + " " + player.y;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		player.draw(g);
	}
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(LEVEL_WIDTH, LEVEL_HEIGHT);
	}

	public void draw(Graphics g) {
		g.drawImage(bg, 0, 0, LEVEL_WIDTH, LEVEL_HEIGHT, null);
	}
}