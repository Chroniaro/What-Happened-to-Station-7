package com.whtss.assets.core;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import com.whtss.assets.entities.Player;
import com.whtss.assets.entities.SimpleEnemy;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.hex.HexRect;
import com.whtss.assets.util.RigidList;

public class Level
{
	static final Random rand = new Random();
	
	private final static int width = 41, height = 19;
	
	//Layers
	int[][] floorLayer;
	LevelObject[][] objectLayer;
	private List<Entity> entities;
	
	private final HexRect bounds;
	private HexPoint end;
	
	public Level()
	{
		floorLayer = new int[width][height];
		int dw = -width/2;
		int dh = 1 - height;
		bounds = HexPoint.rect(HexPoint.origin.mXY(dw, dh + (dh + dw)%2), width, height);
		entities = new ArrayList<>();
		generate();
	}
	
	public void generate()
	{
		for(int x = 1; x < floorLayer.length - 1; x++)
			for(int y = 1; y < floorLayer[x].length - 1; y++)
				floorLayer[x][y] = 0;
		for(int x = 0; x < width; x++)
			floorLayer[x][0] = floorLayer[x][height - 1] = 1;
		for(int y = 0; y < height; y++)
			floorLayer[0][y] = floorLayer[width - 1][y] = 1;
		
		HexPoint[] rooms = buildFloorPlan();
		populateLevel(rooms);
	}
	
	private HexPoint[] buildFloorPlan()
	{
		//Hard coded basic room layout, TODO: Make this bit randomized
		
		HexPoint[] centers = {
					HexPoint.XY(-16, 10),
					HexPoint.XY(-16, -10),
					HexPoint.XY(-2, -8),
					HexPoint.XY(0, 0),
					HexPoint.XY(3, 9),
					HexPoint.XY(16, 12),
					HexPoint.XY(15, -9)
				};
		
		//
		
		HexRect r = getCells();
		int[][] roomIds = new int[width][height];
		for(int x = 0; x < roomIds.length; x++)
			for(int y = 0; y < roomIds[x].length; y++) 
				roomIds[x][y] = -1;
		RigidList<List<HexPoint>> borders = new RigidList<>(centers.length);
		for(int room = 0; room < centers.length; room++)
		{
			borders.set(room, new ArrayList<>());
			
			for(HexPoint tile : HexPoint.circ(centers[room], 2))
			{
				roomIds[r.X(tile)][r.Y(tile)] = room;
				if(centers[room].dist(tile) == 2)
					borders.get(room).add(tile);
			}
		}
		
		//rooms expand their borders
		
		List<Integer> rooms = new ArrayList<>();
		for(int i = 0; i < centers.length; i++)
			rooms.add(i);
		while(!rooms.isEmpty())
		{
			int roomIndex = rand.nextInt(rooms.size());
			int room = rooms.get(roomIndex);
			List<HexPoint> border = borders.get(room);
			int borderSize = border.size();
			
			if(border.isEmpty())
			{
				rooms.remove(roomIndex);
				continue;
			}
			
			HashSet<HexPoint> unclaimedTerritory = null;
			HexPoint borderCell = null;
			
			int start = rand.nextInt(borderSize);
			for(int i = 0; i < borderSize; i++)
			{
				int index = (start + i) % borderSize;
				borderCell = border.get(index);
				
				unclaimedTerritory = new HashSet<HexPoint>();
				for(HexPoint adjCell : borderCell.adjacentCells())
					if(r.contains(adjCell))
						if(roomIds[r.X(adjCell)][r.Y(adjCell)] == -1)
							unclaimedTerritory.add(adjCell);
				
				if(!unclaimedTerritory.isEmpty())
					break;
			}
			
			if(unclaimedTerritory == null || unclaimedTerritory.isEmpty())
			{
				rooms.remove(roomIndex);
				continue;
			}
			
			for(HexPoint cell : unclaimedTerritory)
			{
				roomIds[r.X(cell)][r.Y(cell)] = room;
				border.add(cell);
			}
			
			for(ListIterator<HexPoint> li = border.listIterator(); li.hasNext(); )
			{
				HexPoint cell = li.next();
				
				boolean surrounded = true;
				for(HexPoint a : cell.adjacentCells())
					if(r.contains(a))
					{
						int owner = roomIds[r.X(a)][r.Y(a)];
						if(owner != room && (owner == -1 || !borders.get(owner).contains(a)))
							surrounded = false;
					}
				if(surrounded)
					li.remove();
			}
		}
		
		//Make Station 7 great again by building the wall (around the rooms)
		for(List<HexPoint> l : borders)
			for(HexPoint cell : l)
				floorLayer[r.X(cell)][r.Y(cell)] = 1;
		
		//Clean it up
		for(int x = 0; x < width; x++)
			roomIds[x][0] = roomIds[x][height - 1] = -1;
		for(int y = 0; y < height; y++)
			roomIds[0][y] = roomIds[width - 1][y] = -1;
		
		for(int x = 1; x < width - 2; x++)
			for(int y = 1; y < height - 2; y++)
			{
				int c = 0;
				HexPoint cell = r.fromArrayCoords(x, y);
				for(HexPoint a : cell.adjacentCells())
					if(!r.contains(a) || roomIds[r.X(a)][r.Y(a)] == roomIds[x][y])
						c++;
				if(c < 3)
					floorLayer[x][y] = 1;
			}
		
		//Mr Gorbachev, tear down those walls!
		for(int i = 0; i < centers.length; i++)
		{
			int next = (i + 1) % centers.length, prev = (i + centers.length - 1) % centers.length;
			int nd = 0, pd = 0;
			List<HexPoint> border = borders.get(i);
			for(HexPoint c : border)
				for(HexPoint a : c.adjacentCells())
				{
					if(!r.contains(a))
						continue;
					int owner = roomIds[r.X(a)][r.Y(a)];
					if(owner == next || owner == prev)
					{
						if(owner == next)
							if(Math.random() < nd / 3.0)
								continue;
							else
								nd ++;
						else
							if(Math.random() < pd / 3.0)
								continue;
							else
								pd ++;
						
						floorLayer[r.X(c)][r.Y(c)] = 0;
						floorLayer[r.X(a)][r.Y(c)] = 0;
					}
				}
		}
		
		for(int x = 0; x < width; x++)
			floorLayer[x][height - 1] = floorLayer[x][0] = 1;
		for(int y = 0; y < height; y++)
			floorLayer[width - 1][y] = floorLayer[0][y] = 1;
		
		return centers;
	}

	//This bit will have to be way more complicated once we have a randomized room layout
	private void populateLevel(HexPoint[] rooms)
	{
		final int[] leftRooms = {0, 1};
		final int[] centerRooms = {2, 3, 4};
		final int[] rightRooms = {5, 6};
		
		int startRoom, endRoom, enemyRoom;
		
		if(rand.nextBoolean())
		{
			startRoom = leftRooms[rand.nextInt(leftRooms.length)];
			endRoom = rightRooms[rand.nextInt(rightRooms.length)];
		}
		else
		{
			endRoom = leftRooms[rand.nextInt(leftRooms.length)];
			startRoom = rightRooms[rand.nextInt(rightRooms.length)];
		}
		
		enemyRoom = centerRooms[rand.nextInt(centerRooms.length)];
		
		getEntities().add(new Player(rooms[startRoom], this));
		getEntities().add(new Player(rooms[startRoom].mABY(1, 0, 0), this));
		getEntities().add(new Player(rooms[startRoom].mABY(0, -1, 0), this));
		getEntities().add(new Player(rooms[startRoom].mABY(0, 0, 2), this));
		
		getEntities().add(new SimpleEnemy(rooms[enemyRoom], this));
		
		this.end = rooms[endRoom];
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
	
	public HexPoint getEnd() { return end; }
	public int getWidth() { return floorLayer.length; }
	public int getHeight() { return floorLayer[0].length; }
	public int getFloorTile(int x, int y) { return floorLayer[x][y]; }
	public int getFloorTile(HexPoint p) { return getFloorTile(getCells().X(p), getCells().Y(p)); }
	public HexRect getCells() { return bounds; }
	public List<Entity> getEntities() { return entities; }
}