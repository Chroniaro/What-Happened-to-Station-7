package com.whtss.assets.hex;

import java.util.Iterator;

public class HexCirc implements Iterator<HexPoint>, Iterable<HexPoint>
{
	private final HexPoint c;
	private final int r;
	
	private int a, b;
	
	public HexCirc(HexPoint center, int radius)
	{
		
		c = center;
		r = radius;
		
		a = -radius;
		b = 0;
		
		throw new UnsupportedOperationException("This class doesn't work yet.");
	}
	
	@Override
	public HexPoint next()
	{
		final HexPoint _return = c.mAB(a, b);
		
		if(b++ > r - Math.max(a, 0))
		{
			b = -r - Math.min(a, 0);
			if(a++ > r)
				return null;
		}
		
		return _return;
	}
	
	@Override
	public boolean hasNext()
	{
		return a < r || b < 0;
	}

	@Override
	public Iterator<HexPoint> iterator()
	{
		return this;
	}
}
