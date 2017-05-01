package com.whtss.assets;

import java.util.List;

public class Level
{
	//Layers
	String[][] floorLayer;
	LevelObject[][] objectLayer;
	List<Entity> entities;
	int x = 1;

	public Level()
	{
		floorLayer = new String[41][18];
		for(int x = 0; x < floorLayer.length; x++)
			for(int y = 0; y < floorLayer[x].length; y++)
				floorLayer[x][y] = "(" + x + ", " + y + ")";
	}

	public int getWidth()
	{
		return floorLayer.length;
	}

	public int getHeight()
	{
		return floorLayer[0].length;
	}

	public String getValue(int x, int y)
	{
		return floorLayer[x][y];
	}
}