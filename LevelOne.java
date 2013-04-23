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
	private Ocaml camel;
	private Text gameOver;
	private Duke player;
	private JLabel status;
	private boolean playing = false;
	public static final int LEVEL_WIDTH = 600;
	public static final int LEVEL_HEIGHT = 400;
	public static final int INTERVAL = 25;

	private BufferedImage bg;
	
	private Boolean[][] platforms = new Boolean[250][250];
	private HashSet<Platform> blocks = new HashSet<Platform>(2400); 

	private Boolean[][] walls = new Boolean[250][250];
	private HashSet<Wall> barriers = new HashSet<Wall>(2400);

	private int gravity = 1;
	private int offsetx = 0;
	private int offsety = 0;
	private void build(int x, int y, int w, int h) {
		for(int i=x; i<w+x; i++) {
			for(int j=y; j<h+y; j++) {
				platforms[i][j] = true;
			}
		}
	}

	private void wallOff(int x, int y, int w, int h) {
		for(int i=x; i<w+x; i++) {
			for(int j=y+1; j<h+y; j++) {
				walls[i][j] = true;
			}
		}
		build(x, y, w, 1);
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
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==65) {
					player.status = "runLeft";
				} else if (e.getKeyCode()==68) {
					player.status = "runRight";
				} else if (e.getKeyCode()==87) {
					if (player.grounded) {
						player.yVel = -12;
					}
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e){
				if (e.getKeyCode()==68 || e.getKeyCode()==65) {
					player.status = "idle";
				}
			}
		});
		this.status = status;
	}

	public void fin() {
		offsety = 0;
		gravity = 0;
		player.yVel = 0;
		status.setText("Press any key to restart.");
		playing = false;
		addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
				removeKeyListener(this);
				reset();
			}
		});
	}

	public void reset() {
		status.setText("Find the Objective Camel!");
		gravity = 1;
		player = new Duke();
		camel = new Ocaml();
		playing = true;
		gameOver = null;
		offsetx = 0;
		offsety = 0;

		wallOff(0,10,3,43);

		build(0,53,69,2);
		build(88,53,30,2);
		
		wallOff(53,47,7,6);
		wallOff(60,41,7,12);
		wallOff(67,35,2,18);

		wallOff(73,29,7,18);
		wallOff(80,35,7,12);
		wallOff(87,41,8,6);


		blocks = new HashSet<Platform>(2400);
		barriers = new HashSet<Wall>(2400);
		for(int i=0; i<platforms.length; i++) {
			for(int j=0; j<platforms[0].length; j++) {
				if (platforms[i][j]!=null) {
					blocks.add(new Platform(i*10, j*10, 10, 10));
				}
			}
		}
		for(int i=0; i<walls.length; i++) {
			for(int j=0; j<walls[0].length; j++) {
				if (walls[i][j]!=null) {
					barriers.add(new Wall(i*10, j*10, 10, 10));
				}
			}
		}
	}	

	void tick() {
		player.move();
		player.grounded = false;
		for (Wall w : barriers) {
			if (player.touches(w)) {
				if (player.x <= w.x) {
					player.x = w.x - player.width;
				} else {
					player.x = w.x + w.width;
				}
			} 
		}
		for (Platform p : blocks) {
			if (player.intersects(p) && player.y <= p.y - p.height - 20) {
				if (player.y + player.height != p.y) {
					player.xVel = 0;
				}
				player.yVel = 0;
				player.y = p.y - player.height;
				player.grounded = true;
			}
		}
		if (!player.grounded) {
			player.yVel += gravity;
		}

		//scrolling logic
		if (player.y <= 150) {
			for (Platform p : blocks) {
				p.y += (150 - player.y);
			}
			for (Wall w : barriers) {
				w.y += (150 - player.y);
			}
			camel.y += (150 - player.y);
			offsety -= 150 - player.y;
			player.y = 150;
		}
		if (player.y >= 300) {
			for (Platform p : blocks) {
				p.y -= (player.y - 300);
			}
			for (Wall w : barriers) {
				w.y -= (player.y - 300);
			}
			camel.y -= (player.y - 300);
			offsety += (player.y - 300);
			player.y = 300;
		}
		if (offsety > 300) {
			fin();
		}

		if (player.x >= 400) {
			for (Platform p : blocks) {
				p.x += (400 - player.x);
			}
			for (Wall w : barriers) {
				w.x += (400 - player.x);
			}
			camel.x += (400 - player.x);
			offsetx -= 150 - player.y;
			player.x = 400;
		}
		if (player.x <= 200) {
			for (Platform p : blocks) {
				p.x -= (player.x - 200);
			}
			for (Wall w : barriers) {
				w.x -= (player.x - 200);
			}
			camel.x -= (player.x - 200);
			offsetx += (player.x - 200);
			player.x = 200;
		}
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		if (playing) {
			super.paintComponent(g);
			draw(g);
			player.draw(g);
			camel.draw(g);
			for (Platform p : blocks) {
				p.draw(g);
			}			
			for (Wall w : barriers) {
				w.draw(g);
			}	
		} else {
			if (gameOver == null) {
				gameOver = new Text("gameOver.png");
			}
			gameOver.draw(g);
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