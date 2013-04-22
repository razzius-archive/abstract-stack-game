import java.awt.Graphics;

public interface Shape {
	public int x = 0;
	public int y = 0;

	public int width = 0;
	public int height = 0;

	public void draw(Graphics g);
}
