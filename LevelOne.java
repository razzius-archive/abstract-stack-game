import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Set;

@SuppressWarnings("serial")
public class LevelOne extends JPanel {
	private Duke player;
	private JLabel status;

	public boolean playing = false;
	public static final int LEVEL_WIDTH = 600;
	public static final int LEVEL_HEIGHT = 400;
	public static final int INTERVAL = 20;
	private BufferedImage bg;

	private double gravity = 1.0;


	class MultiKeyPressListener implements KeyListener {

	    // Set of currently pressed keys
	    private final Set<Character> pressed = new HashSet<Character>();

	    @Override
	    public synchronized void keyPressed(KeyEvent e) {
	        pressed.add(e.getKeyChar());
	        if (pressed.size() > 1) {
	            // More than one key is currently pressed.
	            // Iterate over pressed to get the keys.
	            for (char c : pressed) {
	            	System.out.println("HERE");
	            	if (c==(char)65){
	            		System.out.println("a pressed");
	            	}
	            }
	        }
	    }

	    @Override
	    public synchronized void keyReleased(KeyEvent e) {
	        pressed.remove(e.getKeyChar());
	    }

	    @Override
	    public void keyTyped(KeyEvent e) {/* Not used */ }
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

		final MultiKeyPressListener keyboard = new MultiKeyPressListener();

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e){
				keyboard.keyPressed(e);
				// if(e.getKeyCode()==65) {
				// 	player.status = "runLeft";
				// } else if(e.getKeyCode()==68) {
				// 	player.status = "runRight";
				// }
				// if(e.getKeyCode()==87) {
				// 	player.yVel = -20;
				// }
				// System.out.println(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e){
				keyboard.keyReleased(e);
				System.out.println("release");
				// System.out.println("release");
				// player.status = "idle";
			}
		});
		this.status = status;
	}

	
	public void reset() {

		player = new Duke(LEVEL_WIDTH, LEVEL_HEIGHT);

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