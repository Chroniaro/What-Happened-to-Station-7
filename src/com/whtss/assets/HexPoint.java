package com.whtss.assets;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.io.Serializable;

public class HexPoint implements Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6059701303873700245L;
	
	private final static double xratio = .75, yratio = Math.sqrt(3) / 4;
	
	private final static Path2D.Double border = new Path2D.Double();
	static
	{
		border.moveTo(-.5, 0);
		border.lineTo(-.25, -yratio);
		border.lineTo(.25, -yratio);
		border.lineTo(.5, 0);
		border.lineTo(.25, yratio);
		border.lineTo(-.25, yratio);
		border.closePath();
	}
	
	private final int x, y, a, b;
	
	public static final HexPoint origin = new HexPoint(0, 0, 0, 0);
	
	private HexPoint(int x, int y, int a, int b)
	{
		this.x = x;
		this.y = y;
		this.a = a;
		this.b = b;
	}
	
	public static HexPoint AB(int a, int b)
	{
		return new HexPoint(a + b, a - b, a, b);
	}
	
	public static HexPoint XY(int x, int y)
	{
		if((x + y) % 2 != 0)
			throw new Error("Invalid x and y, " + x + " and " + y + ", " + "different parodies.");
		else
			return new HexPoint(x, y, (x + y) / 2, (x - y) / 2);
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getA() { return a; }
	public int getB() { return b; }
	public double getVisualX(double size) { return size * getX() * xratio; }
	public double getVisualY(double size) { return size * getY() * yratio; }
	
	public HexPoint mXY(int dx, int dy) { return XY(getX() + dx, getY() + dy); }
	public HexPoint mAB(int da, int db) { return AB(getA() + da, getB() + db); }
	public HexPoint mX(int dx) { return mXY(dx, 0); }
	public HexPoint mY(int dy) { return mXY(0, dy); }
	public HexPoint mA(int da) { return mAB(da, 0); }
	public HexPoint mB(int db) { return mAB(0, db); }
	
	public int dX(HexPoint h) { return Math.abs(h.getX() - getX()); } 
	public int dY(HexPoint h) { return Math.abs(h.getY() - getY()); }
	public int dA(HexPoint h) { return Math.abs(h.getA() - getA()); }
	public int dB(HexPoint h) { return Math.abs(h.getB() - getB()); }
	public int dist(HexPoint h) { return Math.max(Math.max(dA(h), dB(h)), dX(h)); }
	
	public Shape getBorder(double size)
	{
		AffineTransform at = new AffineTransform();
		at.translate(getVisualX(size), getVisualY(size));
		at.scale(size, size);
		return border.createTransformedShape(at);
	}
	
	@Override
	public String toString() { return "[" + getX() + ", " + getY() + "]"; }
	public String abCoords() { return "<" + getA() + ", " + getB() + ">"; }
	
	@Override
	public int hashCode() { return a ^ Integer.reverse(b); }
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof HexPoint && ((HexPoint)obj).getA() == getA() && ((HexPoint)obj).getB() == getB();
	}
	
	@Override
	public HexPoint clone()
	{
		return new HexPoint(getX(), getY(), getA(), getB());
	}
	
	public static interface HexIterator 
	{
		public void run(HexPoint point, int c1, int c2);
		
		public static interface Simple extends HexIterator
		{
			public void run(HexPoint point);
			
			@Override
			public default void run(HexPoint point, int c1, int c2)
			{
				run(point);
			}
		}
	}
	
	public static void iterateRectangle(HexPoint topLeft, int width, int height, HexIterator iterator)
	{
		for(int dy = 0; dy < height; dy++)
			for(int dx = 0; dx < (width + 1) / 2 - dy % 2; dx++)
				iterator.run(topLeft.mXY(2 * dx + dy % 2, dy), dy, dx);
	}
	
	public static void iterateRectangle(HexPoint topLeft, int width, int height, HexIterator.Simple iterator)
	{
		iterateRectangle(topLeft, width, height, (HexIterator)iterator);
	}
	
	public static void iterateHexagon(HexPoint center, int radius, HexIterator iterator)
	{
		for(int da = -radius; da <= radius; da++)
			for(int db = -radius - Math.min(da, 0); db <= radius - Math.max(da, 0); db++)
				iterator.run(center.mAB(da, db), da, db);
	}
	
	public static void iterateHexagon(HexPoint center, int radius, HexIterator.Simple iterator)
	{
		iterateHexagon(center, radius, (HexIterator)iterator);
	}
	
	public static HexPoint fromVisual(int x, int y, int size)
	{
		if(x > 0)
			x += size;
		if(y > 0)
			y += size;
		int hx = (x - size / 2) / (int)Math.round(size * xratio);
		int hy;
		if(hx % 2 == 0)
			hy = 2 * ((y - size / 2) / (int)Math.round(size * yratio * 2));
		else
			hy = 2 * (y / (int)Math.round(size * yratio * 2)) - 1;
		return XY(hx, hy);
	}
}