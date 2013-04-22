import java.awt.Graphics;

public class Sprite implements Shape {
	public int x;
	public int y;

	public int width;
	public int height;

	public int xVel;
	public int yVel;

	public String status;

	public Sprite(int x, int y, int w, int h, String status) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.status = status;
	}

	public void move() {
		this.x += this.xVel;
		this.y += this.yVel;
		friction();
	}

	public void friction() {
		this.xVel *=.9;
		if (Math.abs(xVel) < 2) {
			xVel = 0;
		}
	}

	public boolean intersects(Platform target) {
		// System.out.println(target.height);
		// System.out.println(x + width + " x " + target.x);
		// System.out.println(y + height + " y " + target.y);
		// System.out.println(x + " tx " + (target.x + target.width));
		// System.out.println(y + " ty " + (target.y + target.height));
		return (x+width >= target.x
			&& y+height >= target.y
			&& target.x + target.width >= x
			&& target.y <= y+height);
	}
	public void draw(Graphics g) {}
}