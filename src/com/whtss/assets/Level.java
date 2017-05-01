package com.whtss.assets;

import java.util.List;

public class Level
{
	//Layers
	Integer[][] floorLayer;
	LevelObject[][] objectLayer;
	List<Entity> entities;
	int x = 1;
	
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

	public int getHeight()
	{
		return floorLayer[0].length;
	}

	public int getValue(int x, int y)
	{
		return floorLayer[x][y];
	}
}