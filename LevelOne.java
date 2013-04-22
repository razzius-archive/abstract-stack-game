import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashSet;

@SuppressWarnings("serial")
public class LevelOne extends JPanel {
	private Duke player;
	private JLabel status;
	private boolean playing = false;
	public static final int LEVEL_WIDTH = 600;
	public static final int LEVEL_HEIGHT = 400;
	public static final int INTERVAL = 15;

	private BufferedImage bg;
	
	private Boolean[][] platforms = new Boolean[61][61];
	private HashSet<Platform> blocks = new HashSet<Platform>(2400); 

	private int gravity = 1;

	private void build(int x, int y, int w, int h) {
		for(int i=x; i<w+x; i++) {
			for(int j=y; j<h+y; j++) {
				platforms[i][j] = true;
			}
		}
	}
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
					if(player.grounded) {
						player.yVel = -12;
					}
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e){
				if(e.getKeyCode()==68 || e.getKeyCode()==65) {
					player.status = "idle";
				}
			}
		});
		this.status = status;
	}

	

	public void reset() {
		player = new Duke();
		build(5,39,30,1);
		build(15,29,20,1);
		build(0,20,10,1);
		build(35,15,10,1);
		build(15,25,10,1);
		build(45,35,10,1);
		build(20,46,40,1);
		build(30,53,20,1);
		build(0,59,60,1); //base
		for(int i=0; i<platforms.length; i++) {
			for(int j=0; j<platforms[0].length; j++) {
				if(platforms[i][j]!=null){
					blocks.add(new Platform(i*10, j*10, 10, 10));
				}
			}
		}
		playing = true;
		status.setText("Running...");
		requestFocusInWindow();
	}

	void tick() {
		player.move();
		player.grounded = false;
		for (Platform p : blocks) {
			if(player.intersects(p) && player.y <= p.y - p.height) {
				player.yVel = 0;
				player.y = p.y - player.height;
				player.grounded = true;
			}
		}
		if(!player.grounded) {
			player.yVel += gravity;
		}
		if(player.y <= 150) {
			for (Platform p : blocks) {
				p.y += (150 - player.y);
			}
			player.y = 150;
		}
		if(player.y >= 350) {
			for (Platform p : blocks) {
				p.y -= (player.y - 350);
			}
			player.y = 350;
		}

		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		player.draw(g);
		for (Platform p : blocks) {
			p.draw(g);
		}
	}
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(LEVEL_WIDTH, LEVEL_HEIGHT);
	}

	public void draw(Graphics g) {
		g.drawImage(bg, 0, 0, LEVEL_WIDTH, LEVEL_HEIGHT, null);
	}
}