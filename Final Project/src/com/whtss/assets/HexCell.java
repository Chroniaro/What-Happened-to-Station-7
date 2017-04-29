package com.whtss.assets;

public class HexCell
{
	private int a, b;
	
	public HexCell(int a, int b)
	{
		setA(a);
		setB(b);
	}
	
	public int getA() { return a; }
	public int getB() { return b; }
	public int getY() { return -a + -b; }
	
	public void setA(int a) { this.a = a; }
	public void setB(int b) { this.b = b; }
	
	public void moveA(int diff) { this.a += diff; }
	public void moveB(int diff) { this.b += diff; }
	public void moveY(int diff)
	{
		moveA(diff);
		moveB(-diff);
	}
	
	public void setY(int y) { moveY(getY() - y); }
	
	public int da(HexCell other) { return getA() - other.getA(); }
	public int db(HexCell other) { return getB() - other.getB(); }
	public int dy(HexCell other) { return getY() - other.getY(); }
}
