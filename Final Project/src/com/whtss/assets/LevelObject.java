package com.whtss.assets;

public class LevelObject extends HexCell implements LightSource
{
	public LevelObject(int x, int y)
	{
		super(x, y);
	}
	
	public int light() { return 0; }
}
