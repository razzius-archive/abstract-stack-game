import java.awt.Graphics;

public class Sprite extends Shape {

	//inherits x,y
	
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

	public boolean intersects(Shape target) {
		return (x+width >= target.x
			&& y+height >= target.y
			&& target.x + target.width >= x
			&& target.y + target.height >= y);
	}
	public void draw(Graphics g) {}
}