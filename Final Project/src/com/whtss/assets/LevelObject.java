package com.whtss;

public class LevelObject extends Cell implements LightSource
{
	public LevelObject(int x, int y)
	{
		super(x, y);
	}
	
	public int light() { return 0; }
}
