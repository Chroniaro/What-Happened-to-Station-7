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
		for(HexPoint cell : i)
			array[i.x()][i.y()] = cell;
		return array;
	}
	
	public HexPoint fromArrayCoords(int x, int y)
	{
		return tl.mXY(x, 2 * y + x % 2);
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
	
	public class Iterator implements java.util.Iterator<HexPoint>, Iterable<HexPoint>
	{
		int x = 0, y = 0;
		int lx, ly;
		
		@Override
		public HexPoint next()
		{
			lx = x; 
			ly = y;
			
			if(++x >= w)
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
			return ly < h;
		}
		
		@Override
		public java.util.Iterator<HexPoint> iterator()
		{
			return this;
		}
	}
}