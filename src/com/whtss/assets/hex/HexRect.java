package com.whtss.assets.hex;

import java.util.Iterator;

public class HexRect implements Iterator<HexPoint>, Iterable<HexPoint>
{
	private final HexPoint tl;
	private final int w, h;
	
	private int x, y, lx, ly;
	
	public HexRect(HexPoint topLeft, int width, int height)
	{
		tl = topLeft;
		w = width; 
		h = height;
		
		x = y = 0;
	}
	
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
		
		return tl.mXY(lx, 2 * ly + lx % 2);
	}
	
	@Override
	public boolean hasNext()
	{
		return ly < h;
	}

	@Override
	public Iterator<HexPoint> iterator()
	{
		return this;
	}
	
	public int getRelX()
	{
		return lx;
	}
	
	public int getRelY()
	{
		return ly;
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
}