package com.whtss.assets;

public class Entity extends HexCell implements LightSource
{
	public Entity(int x, int y)
	{
		super(x, y);
	}
	
	public int light() { return 0; }
}
