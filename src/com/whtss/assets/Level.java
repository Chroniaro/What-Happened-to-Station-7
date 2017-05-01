package com.whtss.assets;

import java.util.List;
import java.util.Random;

public class Level
{
	Random rand = new Random();
	//Layers
	String[][] floorLayer;
	LevelObject[][] objectLayer;
	List<Entity> entities;
	int c = rand.nextInt(41 - 0 + 1) + 0;
	int v = rand.nextInt(19 - 0 + 1) + 0;
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
	public int getEntrance()
	{
		return floorLayer.length;
	}
	public int getstart()
	{
		return floorLayer[c][v];
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