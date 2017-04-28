package com.whtss.assets;

public class Cell
{
	private int x, y;
	
	public Cell(int x, int y)
	{
		setX(x);
		setY(y);
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getZ() { return -x + -y; }
	
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	
	public void moveX(int diff) { this.x += diff; }
	public void moveY(int diff) { this.x += diff; }
	public void moveZ(int diff)
	{
		moveX(diff);
		moveY(-diff);
	}
	
	public void setZ(int z) { moveZ(getZ() - z); }
	
	public int dx(Cell other) { return getX() - other.getX(); }
	public int dy(Cell other) { return getY() - other.getY(); }
	public int dz(Cell other) { return getZ() - other.getZ(); }
}
