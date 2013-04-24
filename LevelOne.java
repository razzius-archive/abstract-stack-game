import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashSet;
import java.util.Random;

@SuppressWarnings("serial")
public class LevelOne extends JPanel {
	private int countdown;
	private int rand;
	private int camelOffset;
	private Random random = new Random();
	private Ocaml camel;
	private Text gameOver;
	private Duke player;
	private Boots boots;
	private Spring spring;
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

	private void wallOff(int x, int x2, int y, int y2) {
		for(int i=x; i<x2; i++) {
			for(int j=y+2; j<y2; j++) {
				walls[i][j] = true;
			}
		}
		build(x, y, x2-x, 2);
	}
	public LevelOne(JLabel status) {
		new RepeatingReleasedEventsFixer().install();		
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
						player.yVel = player.jump;
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
		// After losing, make any key start the game over
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
		boots = new Boots(1320, 150, 30, 30);
		spring = new Spring(400, 400, 40, 40);
		player = new Duke();
		camel = new Ocaml();
		playing = true;
		gameOver = null;
		offsetx = 0;
		offsety = 0;

		//left side
		wallOff(0,10,0,58);

		//right side
		wallOff(400,420,0,60);

		//starting platform
		build(0,58,70,2);

		//three stacks
		wallOff(53,57,52,58);
		wallOff(57,65,46,58);
		wallOff(65,70,37,58);

		//next three
		wallOff(78,85,40,60);
		wallOff(85,91,45,60);
		wallOff(91,98,48,60);


		//builds to the right
		wallOff(115, 165, 58, 61);
		wallOff(173, 200, 58, 61);

		//aerial staircase
		wallOff(190, 200, 50, 58);
		wallOff(180, 185, 43, 46);
		wallOff(170, 175, 36, 39);
		wallOff(160, 165, 32, 35);

		//bringing the platform back;
		wallOff(120, 155, 28, 31);

		//platform with a powerup
		build(130, 20, 10, 1);

		//long jump off
		wallOff(220, 290, 45, 50);

		//underground thing
		wallOff(250, 290, 57, 60);

		//another underground
		wallOff(220, 260, 65, 70);
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
		// status.setText(Integer.toString(camelOffset));
		//makes sure the camel doesn't move too far
		if (countdown == 0) {
			rand = random.nextInt(3);
			if (camelOffset > 1 || camelOffset < -1) {
				if (camelOffset > 0) {
					camel.xVel = -6;
					camelOffset = 0;
				} else {
					camel.xVel = 6;
					camelOffset = 0;
				}
			} else if (rand == 2) {
				camel.xVel += 6;
				countdown += 40;
				camelOffset += 1;
			} else if (rand == 1) {
				camel.xVel -= 6;
				countdown += 40;
				camelOffset -= 1;
			}
		} else {
			countdown--;
		}
		player.move();
		camel.move();
		player.grounded = false;
		for (Shape s : blocks) {
			if (s.getClass().isInstance(wallSample)) {
				if (player.intersects(s)) {

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
		if (player.intersects(boots)) {
			player.maxVel = 12;
			boots = new Boots(0,0,0,0); //make boots invisible
			status.setText("Boots acquired! (try running)");	
		}
		if (player.intersects(spring)) {
			player.jump = -12;
			spring = new Spring(0,0,0,0); //make boots invisible
			status.setText("Spring acquired! (try jumping)");	
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
			boots.y += (150 - player.y);
			spring.y += (150 - player.y);
			offsety -= 150 - player.y;
			player.y = 150;
		}
		if (player.y >= 280) {
			for (Shape s : blocks) {
				s.y -= (player.y - 280);
			}
			camel.y -= (player.y - 280);
			boots.y -= (player.y - 280);
			spring.y -= (player.y - 280);
			
			offsety += (player.y - 280);
			player.y = 280;
		}
		if (offsety > 420) {
			fin();
		}

		if (player.x >= 350 && offsetx < 4000) {
			for (Shape s : blocks) {
				s.x += (350 - player.x);
			}
			camel.x += (350 - player.x);
			boots.x += (350 - player.x);
			spring.x += (350 - player.x);
			
			offsetx -= 350 - player.x;
			player.x = 350;
		}
		if (player.x <= 200 && offsetx > 0) {
			for (Shape s : blocks) {
				s.x -= (player.x - 200);
			}
			camel.x -= (player.x - 200);
			boots.x -= (player.x - 200);
			spring.x -= (player.x - 200);
			
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
			boots.draw(g);
			spring.draw(g);
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