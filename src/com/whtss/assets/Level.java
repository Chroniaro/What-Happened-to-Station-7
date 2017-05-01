package com.whtss.assets;

import java.util.List;
import java.util.Random;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.hex.HexRect;

public class Level
{
	static final Random rand = new Random();
	
	private final static int width = 41, height = 18;
	
	//Layers
	String[][] floorLayer;
	LevelObject[][] objectLayer;
	List<Entity> entities;
	
	private final HexRect bounds;
	final HexPoint start;
	
	public Level()
	{
		floorLayer = new String[width][height];
		for(int x = 0; x < floorLayer.length; x++)
			for(int y = 0; y < floorLayer[x].length; y++)
				floorLayer[x][y] = "(" + x + ", " + y + ")";
		
		int dw = -width/2;
		int dh = 1 - height;
		bounds = HexPoint.rect(HexPoint.origin.mXY(dw, dh + (dh + dw)%2), width, height);
		start = getCells().fromArrayCoords(rand.nextInt(width), rand.nextInt(height));
	}

	public int getWidth()
	{
		return floorLayer.length;
	}
	public int getEntrance()
	{
		return floorLayer.length;
	}
	public HexPoint getstart()
	{
		return start;
	}

	public int getHeight()
	{
		return floorLayer[0].length;
	}

	public String getValue(int x, int y)
	{
		return floorLayer[x][y];
	}

	public HexRect getCells()
	{
		return bounds;
	}
}