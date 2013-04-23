import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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

	private Wall wallSample = new Wall(0,0,0,0);
	private Platform platformSample = new Platform(0,0,0,0);

	private BufferedImage bg;
	
	private Boolean[][] platforms = new Boolean[500][500];
	private Boolean[][] walls = new Boolean[500][500];

	private HashSet<Shape> blocks = new HashSet<Shape>(2400);

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

		//right side
		wallOff(200,0,20,60);

		build(0,53,69,2);
		build(60,56,30,2);

		build(80,50,300,2);
		wallOff(300,46,200,4);
		
		wallOff(53,47,7,6);
		wallOff(60,41,7,12);
		wallOff(67,35,2,18);

		wallOff(73,29,7,14);
		wallOff(80,35,7,8);
		wallOff(87,41,8,2);


		blocks = new HashSet<Shape>(2400);

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
					blocks.add(new Wall(i*10, j*10, 10, 10));
				}
			}
		}
	}	

	void tick() {
		status.setText(Integer.toString(player.x));
		player.move();
		player.grounded = false;
		for (Shape s : blocks) {
			if (s.getClass().isInstance(wallSample)) {
				if (player.intersects(s)) {
					System.out.println(s.x + "?, " + player.x);
					//hitting left side
					if (player.x <= s.x) {
						player.x = s.x - player.width;
					} else {
						player.x = s.x + s.width;
					}
				}
			} else if (s.getClass().isInstance(platformSample)) {
				if (player.intersects(s) && player.y <= s.y - s.height - 20) {
					player.yVel = 0;
					player.y = s.y - player.height;
					player.grounded = true;
				}
			}
		}
		if (!player.grounded) {
			player.yVel += gravity;
		}

		//scrolling logic
		if (player.y <= 150) {
			for (Shape s : blocks) {
				s.y += (150 - player.y);
			}
			camel.y += (150 - player.y);
			offsety -= 150 - player.y;
			player.y = 150;
		}
		if (player.y >= 300) {
			for (Shape s : blocks) {
				s.y -= (player.y - 300);
			}
			camel.y -= (player.y - 300);
			offsety += (player.y - 300);
			player.y = 300;
		}
		if (offsety > 400) {
			fin();
		}

		if (player.x >= 400 && offsetx < 1600) {
			for (Shape s : blocks) {
				s.x += (400 - player.x);
			}
			camel.x += (400 - player.x);
			offsetx -= 400 - player.x;
			player.x = 400;
		}
		if (player.x <= 200 && offsetx > 0) {
			for (Shape s : blocks) {
				s.x -= (player.x - 200);
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
			for (Shape s : blocks) {
				s.draw(g);
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