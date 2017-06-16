package com.whtss.assets.hex;

public class HexRect implements Iterable<HexPoint>
{
	private final HexPoint tl;
	private final int w, h;

	public HexRect(HexPoint topLeft, int width, int height)
	{
		tl = topLeft;
		w = width;
		h = height;
	}
	public HexPoint[][] asArray()
	{
		HexPoint[][] array = new HexPoint[w][h];
		Iterator i = iterator();
		for (HexPoint cell : i)
			array[i.x()][i.y()] = cell;
		array[0][0] = array[0][w - 1] = null;
		return array;
	}

	/*
	 * The following three methods implement the third coordinate system mentioned in in HexPoint. It's
	 * closely related the the X-Y coordinate system but tweaked mostly to work better with 2D arrays.
	 */
	
	public HexPoint fromArrayCoords(int x, int y)
	{
		return tl.mXY(x, 2 * y + x % 2);
	}

	public int X(HexPoint p)
	{
		return p.getX() - tl.getX();
	}

	public int Y(HexPoint p)
	{
		return (p.getY() - tl.getY()) / 2;
	}

	/**
	 * @param point A point to check
	 * @return Whether that point is in the specified rectangle
	 */
	public boolean contains(HexPoint point)
	{
		final HexPoint br = fromArrayCoords(w, h);

		if (point == null || point.equals(tl) || point.equals(tl.mX(w - 1)))
			return false;
		return point.getX() >= tl.getX() && point.getY() >= tl.getY() && point.getX() < br.getX() && point.getY() < br.getY() - 1;
	}

	@Override
	public Iterator iterator()
	{
		return new Iterator();
	}

	public int getHeight()
	{
		return h;
	}

	public int getWidth()
	{
		return w;
	}

	public HexPoint getTopLeftPoint()
	{
		return tl;
	}

	/**
	 * Iterator over every tile in the rectangle
	 */
	public class Iterator implements java.util.Iterator<HexPoint>, Iterable<HexPoint>
	{
		int x = 1, y = 0;
		int lx, ly;

		@Override
		public HexPoint next()
		{
			lx = x;
			ly = y;

			if (++x >= (y == 0 ? w - 1 : w))
			{
				x = 0;
				y++;
			}

			return fromArrayCoords(lx, ly);
		}

		public int x()
		{
			return lx;
		}

		public int y()
		{
			return ly;
		}

		@Override
		public boolean hasNext()
		{
			return y < h;
		}

		@Override
		public java.util.Iterator<HexPoint> iterator()
		{
			return this;
		}
	}
}