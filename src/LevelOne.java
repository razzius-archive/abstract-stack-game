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
	private Boolean started = false;
	private int countdown;
	private int rand;
	private int camelOffset;
	private Random random = new Random();
	private Ocaml camel;
	private Text gameOver;
	private Text instructions;
	private Duke player;
	private Boots boots;
	private Soap soap;
	private Spring spring;
	private JLabel status;
	private Boolean superJump = false;
	private Boolean superRun = false;
	private Boolean slippery = false;
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

	private int gravity;
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
			for(int j=y+2; j<y2-1; j++) {
				walls[i][j] = true;
			}
		}
		build(x+1, y2-1,x2-x-2, 1);
		build(x, y, x2-x, 2);
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
				if (!started) {
					//first keypress starts the game
					started = true;
				}
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
		started = false;
		superJump = false;
		superRun = false;
		slippery = false;
		status.setText("Find the Objective Camel!");
		gravity = 1;
		boots = new Boots(1320, 150, 40, 40);
		spring = new Spring(100, 550, 40, 40);
		soap = new Soap(2450, 420, 40, 40);
		player = new Duke();
		camel = new Ocaml();
		playing = true;
		gameOver = null;
		offsetx = 0;
		offsety = 0;

		//left side
		wallOff(0,10,0,58);

		//right side
		wallOff(400,420,0,80);

		//starting platform
		build(0,58,70,2);

		//three stacks
		wallOff(53,57,52,59);
		wallOff(57,65,46,59);
		wallOff(65,70,37,59);

		//next three
		wallOff(78,85,40,60);
		wallOff(85,91,45,60);
		wallOff(91,98,48,60);


		//builds to the right
		wallOff(115, 165, 55, 61);
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
		wallOff(218, 290, 45, 50);

		//underground thing
		wallOff(250, 300, 57, 60);
		
		//block shortcut
		wallOff(218, 230, 50, 65);

		//another underground
		wallOff(220, 260, 65, 70);

		//block skip to end
		wallOff(298, 300, 40, 60);
		//final staircase up
		wallOff(265,320,65,70);
		wallOff(320,338,60,70);

		//slice this platform nicely
		wallOff(338,342,55,70);
		wallOff(347,350,53,70);
		wallOff(360,362,50,70);
		wallOff(368,370,55,70);
		wallOff(386,400,58,69);

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
		//makes sure the camel doesn't move too far
		if (countdown == 0) {
			rand = random.nextInt(3);
			if (camelOffset > 1 || camelOffset < -1) {
				if (camelOffset > 0) {
					camel.xVel = -3;
					camelOffset = 0;
				} else {
					camel.xVel = 3;
					camelOffset = 0;
				}
			} else if (rand == 2) {
				camel.xVel += 3;
				countdown += 60;
				camelOffset += 1;
			} else if (rand == 1) {
				camel.xVel -= 3;
				countdown += 60;
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
				} else if (player.intersects(s) && player.x > s.x && player.x < s.x + s.width && player.yVel < 0) {
					player.y = s.y + s.height;
					player.yVel = 0;
				}
			}
		}
		if (player.intersects(boots)) {
			player.maxVel = 10;
			boots = new Boots(40,30,40,40);
			superRun = true;
			status.setText("Boots acquired! (try running)");	
		}
		if (player.intersects(spring)) {
			player.jump = -12;
			spring = new Spring(0,30,40,40);
			superJump = true;
			status.setText("Spring acquired! (try jumping)");	
		}
		if (player.intersects(soap)) {
			player.slideFactor = .99;
			soap = new Soap(80,30,40,40);
			slippery = true;
			status.setText("Slippery soap acquired! (uh oh)");	
		}
		if (!player.grounded) {
			player.yVel += gravity;
		}
		if (player.intersects(camel)) {
			status.setText("YOU WIN!");
		}

		//scrolling logic
		if (player.y <= 150) {
			for (Shape s : blocks) {
				s.y += (150 - player.y);
			}
			camel.y += (150 - player.y);
			if (!superRun) {
				boots.y += (150 - player.y);
			}
			if (!superJump) {
				spring.y += (150 - player.y);
			}
			if (!slippery) {
				soap.y += (150 - player.y);
			}
			offsety -= 150 - player.y;
			player.y = 150;
		}
		if (player.y >= 260) {
			for (Shape s : blocks) {
				s.y -= (player.y - 260);
			}
			camel.y -= (player.y - 260);
			
			if (!superRun) {
				boots.y -= (player.y - 260);
			}
			if (!superJump) {
				spring.y -= (player.y - 260);
			}
			if (!slippery) {
				soap.y -= (player.y - 260);
			}
			
			offsety += (player.y - 260);
			player.y = 260;
		}
		if (offsety > 430) {
			fin();
		}

		if (player.x >= 350 && offsetx < 4000) {
			for (Shape s : blocks) {
				s.x += (350 - player.x);
			}
			camel.x += (350 - player.x);
			if (!superRun) {
				boots.x += (350 - player.x);
			}
			if (!superJump) {
				spring.x += (350 - player.x);
			}
			if (!slippery) {
				soap.x += (350 - player.x);
			}
			
			offsetx -= 350 - player.x;
			player.x = 350;
		}
		if (player.x <= 200 && offsetx > 0) {
			for (Shape s : blocks) {
				s.x -= (player.x - 200);
			}
			camel.x -= (player.x - 200);
			if (!superRun) {
				boots.x -= (player.x - 200);
			}
			if (!superJump) {
				spring.x -= (player.x - 200);
			}		
			if (!slippery) {
				soap.x -= (player.x - 200);
			}		
			offsetx += (player.x - 200);
			player.x = 200;
		}
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		if (!started) {
			instructions = new Text("../img/instructions.png");
			instructions.draw(g);
		} else if (playing) {
			super.paintComponent(g);
			draw(g);
			player.draw(g);
			camel.draw(g);
			boots.draw(g);
			spring.draw(g);
			soap.draw(g);
			for (Shape s : blocks) {
				s.draw(g);
			}			
		} else {
			gameOver = new Text("../img/gameOver.png");
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