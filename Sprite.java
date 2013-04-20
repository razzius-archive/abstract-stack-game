import java.awt.Graphics;

public class Sprite {
	public int x;
	public int y;

	public int width;
	public int height;

	public double xVel;
	public double yVel;

	public int xMax;
	public int yMax;

	public String status;
	public int MAX_VEL = 10;

	public Sprite(int x, int y, int xVel, int yVel, int width,
		int height, int xMax, int yMax) {
		this.x = x;
		this.y = y;
		this.xVel = xVel;
		this.yVel = yVel;
		this.width = width;
		this.height = height;
		this.xMax = xMax;
		this.yMax = yMax;
	}

	public void move() {
		this.x += this.xVel;
		this.y += this.yVel;
		friction();
	}
	public void friction() {
		if (this.xVel >= MAX_VEL) {
			this.xVel = MAX_VEL;
		}
		if (this.xVel <= -MAX_VEL) {
			this.xVel = -MAX_VEL;
		}
		this.xVel *=.9;
		if (Math.abs(xVel) < 2) {
			xVel = 0;
		}
		if (this.y > yMax - this.height) {
			this.y = yMax - this.height;
			if (Math.abs(this.yVel) > 3) {
				this.yVel *=-.4;
			} else {
				this.yVel = 0;
			}
		}
	}

	public boolean intersects(Sprite target) {
		return (x+width >= target.x
			&& y+height >= target.y
			&& target.x + target.width >= x
			&& target.y + target.height >= y);
	}

	public void draw(Graphics g) {}
}
