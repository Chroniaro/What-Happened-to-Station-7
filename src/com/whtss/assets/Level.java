package com.whtss.assets;

import java.util.List;
import java.util.Random;

public class Level
{
	Random rand = new Random();
	//Layers
	Integer[][] floorLayer;
	LevelObject[][] objectLayer;
	List<Entity> entities;
	int c = rand.nextInt(41 - 0 + 1) + 0;
	int v = rand.nextInt(19 - 0 + 1) + 0;
	public Level()
	{
		floorLayer = new Integer[41][18];
//		HexPoint.iterateArray(floorLayer, (HexPoint hex, int x, int y) ->
//		{
//			floorLayer[x][y] = (int)(Math.random() * 150) | (int)(Math.random() * 150) << 8 | (int)(Math.random() * 150) << 16;
//		});
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

	public int getValue(int x, int y)
	{
		return floorLayer[x][y];
	}
}