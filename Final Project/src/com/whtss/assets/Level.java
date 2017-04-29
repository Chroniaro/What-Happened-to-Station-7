package com.whtss.assets;

import java.util.List;

public class Level
{
	//Layers
	int[][] floorLayer;
	LevelObject[][] objectLayer;
	List<Entity> entities;
	
	public Level()
	{
		floorLayer = new int[10][10];
	}
	
	public int getWidth()
	{
		return 10;
	}
	
	public int getHeight()
	{
		return 10;
	}
	
	public int getValue(int x, int y)
	{
		return floorLayer[x][y];
	}
}