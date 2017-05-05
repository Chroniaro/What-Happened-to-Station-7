package com.whtss.assets.core;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.whtss.assets.LevelObject;
import com.whtss.assets.entities.Player;
import com.whtss.assets.entities.SimpleEnemy;
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
	private HexPoint enimy1;
	public Level()
	{
		floorLayer = new int[width][height];
		int dw = -width/2;
		int dh = 1 - height;
		bounds = HexPoint.rect(HexPoint.origin.mXY(dw, dh + (dh + dw)%2), width, height);
		entities = new ArrayList<>();
		generate();
	}
	
	private void generate()
	{
		for(int x = 1; x < floorLayer.length - 1; x++)
			for(int y = 1; y < floorLayer[x].length - 1; y++)
				floorLayer[x][y] = 0;
		for(int x = 0; x < width; x++)
			floorLayer[x][0] = floorLayer[x][height - 1] = 1;
		for(int y = 0; y < height; y++)
			floorLayer[0][y] = floorLayer[width - 1][y] = 1;
		
		start = getCells().fromArrayCoords(rand.nextInt(width - 4) + 2, rand.nextInt(height - 4) + 2);
		//enimy1 = getCells().fromArrayCoords(7, 7);
		int zz =rand.nextInt(width);
		int yy = rand.nextInt(height);
		enimy1 = getCells().fromArrayCoords(zz, yy);
		
		do {
			end = getCells().fromArrayCoords(rand.nextInt(width), rand.nextInt(height));
		} while(end.dist(start) < 3 || !getCells().contains(end));
		
		getEntities().add(new Player(start, this));
		getEntities().add(new Player(start.mABY(1, 0, 0), this));
		getEntities().add(new Player(start.mABY(0, -1, 0), this));
		getEntities().add(new Player(start.mABY(0, 0, 2), this));
		getEntities().add(new SimpleEnemy(enimy1, this));
		
	}

	public HexPoint performAction(HexPoint select, HexPoint mouse, KeyEvent key)
	{
		for(Entity e : getEntities())
			if(e.getLocation().equals(select))
				if(e.isActive())
					return e.input(key, mouse);
		return select;
	}
	
	public void nextTurn()
	{
		for(Entity e : entities)
			e.endTurn();
	}
	
	public HexPoint getstart() { return start; }
	public HexPoint getEnd() { return end; }
	public int getWidth() { return floorLayer.length; }
	public int getHeight() { return floorLayer[0].length; }
	public int getFloorTile(int x, int y) { return floorLayer[x][y]; }
	public int getFloorTile(HexPoint p) { return getFloorTile(getCells().X(p), getCells().Y(p)); }
	public HexRect getCells() { return bounds; }
	public List<Entity> getEntities() { return entities; }
}