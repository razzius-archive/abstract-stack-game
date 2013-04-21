import java.awt.Graphics;

public class Shape {
	public int x = 0;
	public int y = 0;

	public int width = 0;
	public int height = 0;

	public Shape(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean intersects(Shape target) {
		return (x+width >= target.x
			&& y+height >= target.y
			&& target.x + target.width >= x
			&& target.y + target.height >= y);
	}

	public void draw(Graphics g) {}
}
