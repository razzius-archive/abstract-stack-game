import java.awt.*;
import javax.swing.*;

public class Game implements Runnable {
    public void run() {
    	final JFrame frame = new JFrame("0");
    	frame.setLocation(0,0);

    	final JPanel status_panel = new JPanel();
    	frame.add(status_panel, BorderLayout.SOUTH);

    	final JLabel status = new JLabel("GAME ESTART");
    	status_panel.add(status);

    	LevelOne levelOne = new LevelOne(status);
    	frame.add(levelOne, BorderLayout.CENTER);

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        
    	frame.pack();
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);

    	levelOne.reset();

    }

    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Game());
    }
}

