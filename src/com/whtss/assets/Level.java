package com.whtss.assets;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.whtss.assets.entities.Player;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.hex.HexRect;

public class Level
{
	static final Random rand = new Random();
	
	private final static int width = 41, height = 19;
	
	//Layers
	int[][] floorLayer;
	LevelObject[][] objectLayer;
	private List<Entity> entities;
	
	private final HexRect bounds;
	private HexPoint start;
	private HexPoint end;
	
	public Level()
	{
		floorLayer = new int[width][height];
		for(int x = 0; x < floorLayer.length; x++)
			for(int y = 0; y < floorLayer[x].length; y++)
				floorLayer[x][y] = 0;
		
		int dw = -width/2;
		int dh = 1 - height;
		bounds = HexPoint.rect(HexPoint.origin.mXY(dw, dh + (dh + dw)%2), width, height);
		entities = new ArrayList<>();
		generate();
	}
	
	private void generate()
	{
		start = getCells().fromArrayCoords(rand.nextInt(width - 2) + 1, rand.nextInt(height - 2) + 1);
		do {
			end = getCells().fromArrayCoords(rand.nextInt(width), rand.nextInt(height));
		} while(end.dist(start) < 3);
		
		getEntities().add(new Player(start, this));
		getEntities().add(new Player(start.mABY(1, 0, 0), this));
		getEntities().add(new Player(start.mABY(0, -1, 0), this));
		getEntities().add(new Player(start.mABY(0, 0, 2), this));
	}

	public HexPoint performAction(HexPoint select, HexPoint mouse, KeyEvent key)
	{
		for(Entity e : getEntities())
			if(e.getLocation().equals(select))
				return e.input(key, mouse);
		return select;
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

	public int getFloorTile(int x, int y)
	{
		return floorLayer[x][y];
	}

	public HexRect getCells()
	{
		return bounds;
	}

	public List<Entity> getEntities()
	{
		return entities;
	}

	public void nextTurn()
	{
		for(Entity e : entities)
			e.endTurn();
	}
}