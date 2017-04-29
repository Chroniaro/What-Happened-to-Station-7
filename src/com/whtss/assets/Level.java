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
		floorLayer = new Integer[42][32];
		HexPoint.iterateArray(floorLayer, (HexPoint hex, int x, int y) ->
		{
			if(Math.random()<.25)
			 floorLayer[x][y] = (int)(1) | (int)(1) << 8 | (int)(1) << 16;
			else
		     floorLayer[x][y] = (int)(Math.random() * 200) | (int)(Math.random() * 200) << 8 | (int)(Math.random() * 200) << 16;
		});
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