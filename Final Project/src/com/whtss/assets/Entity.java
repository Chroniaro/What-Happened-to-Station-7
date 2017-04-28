package com.whtss;

public class Entity extends Cell implements LightSource
{
	public Entity(int x, int y)
	{
		super(x, y);
	}
	
	public int light() { return 0; }
}
