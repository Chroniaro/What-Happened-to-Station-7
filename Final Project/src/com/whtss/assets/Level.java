package com.whtss.assets;

import java.util.List;

public class Level
{
	//Layers
	int[][] floorLayer;
	LevelObject[][] objectLayer;
	List<Entity> entities;
	int x = 1;

	public Level()
	{
		floorLayer = new int[15][11];
		for(int x = 0; x < 15; x++)
			for(int y = 0; y < 11; y++)
				floorLayer[x][y] = (int)(Math.random() * 100) | (int)(Math.random() * 100) << 8 | (int)(Math.random() * 100) << 16;
	}

	public int getWidth()
	{
		return floorLayer.length;
	}

	public int getHeight()
	{
		return floorLayer[0].length;
	}

	public int getValue(int x, int y)
	{
		return floorLayer[x][y];
	}
}