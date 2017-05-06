package com.whtss.assets.hex;

public class HexCirc implements Iterable<HexPoint>
{
	private final HexPoint c;
	private final int r;
	
	public HexCirc(HexPoint center, int radius)
	{
		c = center;
		r = radius;
	}
	
	public boolean contains(HexPoint point)
	{
		return c.dist(point) <= r;
	}

	@Override
	public Iterator iterator()
	{
		return new Iterator();
	}
	
	public int getRadius()
	{
		return r;
	}
	
	public HexPoint getCenterPoint()
	{
		return c;
	}
	
	public class Iterator implements java.util.Iterator<HexPoint>, Iterable<HexPoint>
	{
		int a = -r, b = 0;
		int la, lb;
		
		@Override
		public HexPoint next()
		{
			la = a;
			lb = b;
			
			if(++b > r - Math.max(a, 0))
			{
				a++;
				b = -r - Math.min(a, 0);
			}
			
			return c.mAB(la, lb);
		}
		
		public int a()
		{
			return la;
		}
		
		public int b()
		{
			return lb;
		}
		
		@Override
		public boolean hasNext()
		{
			return la < r || lb < 0;
		}
		
		@Override
		public java.util.Iterator<HexPoint> iterator()
		{
			return this;
		}
	}
}