package com.whtss.assets.core;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.whtss.assets.Station7;
import com.whtss.assets.entities.HealBox;
import com.whtss.assets.entities.Player;
import com.whtss.assets.entities.SimpleEnemy;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.hex.HexRect;
import com.whtss.assets.render.GameInfo;
import com.whtss.assets.render.GameRenderer;
import com.whtss.assets.render.GameRenderer.UIInterface;
import com.whtss.assets.render.SoundStuff;
import com.whtss.assets.util.RandomIterator;
import com.whtss.assets.util.RigidList;

public class Level
{
	static final Random rand = new Random();

	private final static int width = 41, height = 19;

	//Layers
	int[][] floorLayer;
	LevelObject[][] objectLayer;
	private List<Entity> entities;
	private int[][] roomTiles = null;

	private final HexRect bounds;
	private HexPoint end;
	private UIInterface uiinterface;
	private GameInfo.UIInterface infoInterface;
	private final int[][] playerStartOffsets = new int[][] { { 0, 0, 0 }, { 1, 0, 0 }, { 0, -1, 0 }, { 0, 0, 2 }, { 0, 0, -2 }, { 0, 1, 0 }, { -1, 0, 0 } };
	private List<Player> persistant;
	private int activePlayerCount;
	private int floor = 1;

	public Level(GameRenderer.UIInterface UIInterface, GameInfo.UIInterface infoInterface)
	{
		uiinterface = UIInterface;
		this.infoInterface = infoInterface;

		floorLayer = new int[width][height];
		objectLayer = new LevelObject[width][height];

		persistant = new ArrayList<Player>();

		int dw = -width / 2;
		int dh = 1 - height;
		bounds = HexPoint.rect(HexPoint.origin.mXY(dw, dh + (dh + dw) % 2), width, height);
		entities = new ArrayList<>();
	}

	private HexPoint[] generate(Player... players)
	{
		getEntities().clear();

		for (int x = 0; x < objectLayer.length; x++)
			for (int y = 0; y < objectLayer[x].length; y++)
				objectLayer[x][y] = null;

		for (int x = 1; x < floorLayer.length - 1; x++)
			for (int y = 1; y < floorLayer[x].length - 1; y++)
				floorLayer[x][y] = 0;
		for (int x = 0; x < width; x++)
			floorLayer[x][0] = floorLayer[x][height - 1] = 1;
		for (int y = 0; y < height; y++)
			floorLayer[0][y] = floorLayer[width - 1][y] = 1;

		HexPoint[] rooms = buildFloorPlan();
		populateLevel(rooms, players);
		return rooms;
	}

	public void generate()
	{
		generate(new Player(null, this), new Player(null, this), new Player(null, this), new Player(null, this));
	}
	
	private HexPoint[] buildFloorPlan()
	{
		//Hard coded basic room layout, TODO: Make this bit randomized

		HexPoint[] centers = { HexPoint.XY(-16, 10), HexPoint.XY(-16, -10), HexPoint.XY(-2, -8), HexPoint.XY(0, 0), HexPoint.XY(3, 9), HexPoint.XY(16, 12), HexPoint.XY(15, -9) };

		HexRect r = getCells();
		
		int[][] roomIds = new int[width][height];
		for (int x = 0; x < roomIds.length; x++)
			for (int y = 0; y < roomIds[x].length; y++)
				roomIds[x][y] = -1;
		
		roomTiles = roomIds;
		
		RigidList<Set<HexPoint>> borders = new RigidList<>(centers.length);
		for (int room = 0; room < centers.length; room++)
		{
			borders.set(room, new HashSet<>());

			for (HexPoint tile : HexPoint.circ(centers[room], 2))
			{
				if (centers[room].dist(tile) == 2)
					borders.get(room).add(tile);
				else
					roomIds[r.X(tile)][r.Y(tile)] = room;
			}
		}

		//rooms expand their borders

		List<Integer> rooms = new ArrayList<>();
		for (int i = 0; i < centers.length; i++)
			rooms.add(i);
		while (!rooms.isEmpty())
		{
//			try
//			{
//				Thread.sleep(100);
//			}
//			catch (InterruptedException e)
//			{
//				e.printStackTrace();
//			}
			
			int roomIndex = rand.nextInt(rooms.size());
			int room = rooms.get(roomIndex);
			Set<HexPoint> border = borders.get(room);

			HashSet<HexPoint> unclaimedTerritory = null;
			HexPoint borderCell = null;
			
			for (RandomIterator<HexPoint> ri = new RandomIterator<>(border); ri.hasNext(); )
			{	
				borderCell = ri.next();

				int friendly = 0;
				int wall = 0;
				unclaimedTerritory = new HashSet<HexPoint>();
				boolean valid = true;
				for (HexPoint adjCell : borderCell.adjacentCells())
					if (r.contains(adjCell))
						if(border.contains(adjCell) && wall < 2)
							wall ++;
						else
						{
							int crid = roomIds[r.X(adjCell)][r.Y(adjCell)];
							
							if (crid < 0)
								unclaimedTerritory.add(adjCell);
							else if(crid == room)
								friendly ++;
							else
								valid = false;
						}

				if (valid && friendly > 1)
					break;
				else
					unclaimedTerritory = null;
			}

			if (unclaimedTerritory == null)
			{
				rooms.remove(roomIndex);
				continue;
			}
			
			for (HexPoint cell : unclaimedTerritory)
				border.add(cell);
			
			roomIds[r.X(borderCell)][r.Y(borderCell)] = room;
			border.remove(borderCell);
		}

		//Make Station 7 great again by building the wall (around the rooms)
		for(int x = 0; x < roomIds.length; x++)
			for(int y = 0; y < roomIds[x].length; y++)
				if(roomIds[x][y] == -2)
					floorLayer[x][y] = 1;

//		//Mr Gorbachev, tear down those walls!
//		for (int i = 0; i < centers.length; i++)
//		{
//			int next = (i + 1) % centers.length, prev = (i + centers.length - 1) % centers.length;
//			int nd = 0, pd = 0;
//			List<HexPoint> border = borders.get(i);
//			for (HexPoint c : border)
//				for (HexPoint a : c.adjacentCells())
//				{
//					if (!r.contains(a))
//						continue;
//					int owner = roomIds[r.X(a)][r.Y(a)];
//					if (owner == next || owner == prev)
//					{
//						if (owner == next)
//							if (Math.random() < nd / 3.0)
//								continue;
//							else
//								nd++;
//						else if (Math.random() < pd / 3.0)
//							continue;
//						else
//							pd++;
//
//						floorLayer[r.X(c)][r.Y(c)] = 0;
//						floorLayer[r.X(a)][r.Y(c)] = 0;
//					}
//				}
//		}
//
//		for (int x = 0; x < width; x++)
//			floorLayer[x][height - 1] = floorLayer[x][0] = 1;
//		for (int y = 0; y < height; y++)
//			floorLayer[width - 1][y] = floorLayer[0][y] = 1;
		
		return centers;
	}

	//This bit will have to be way more complicated once we have a randomized room layout
	private void populateLevel(HexPoint[] rooms, Player... players)
	{
		final int[] leftRooms = { 0, 1 };
		final int[] centerRooms = { 2, 3, 4 };
		final int[] rightRooms = { 5, 6 };

		int startRoom, endRoom, enemyRoom, Healroom;

		if (rand.nextBoolean())
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
		Healroom = centerRooms[rand.nextInt(centerRooms.length)];

		for (int i = 0; i < players.length && i < playerStartOffsets.length; i++)
		{
			Entity ep = players[i];
			if (ep == null)
				System.out.println("Null player");
			ep.setLocation(rooms[startRoom].mABY(playerStartOffsets[i][0], playerStartOffsets[i][1], playerStartOffsets[i][2]));
			ep.setActive(true);
			getEntities().add(players[i]);
		}

//		activePlayerCount = players.length;
		activePlayerCount = 1;

		getEntities().add(new SimpleEnemy(rooms[enemyRoom], this));

		getEntities().add(new HealBox(rooms[Healroom].mABY(0, 0, 2), this));
		this.end = rooms[endRoom];
	}

	public void processInput(HexPoint select, HexPoint mouse, KeyEvent key, String turn)
	{
		try
		{
			for (Entity e : getEntities())
				if (e.getLocation().equals(select))
					if (e.isActive())
						e.input(key, mouse, turn);
		}
		catch (ConcurrentModificationException e)
		{
			return;
		}
	}
	
	/**
	 * Not yet implemented
	 */
	public HexPoint[] visibleCells(HexPoint origin) //TODO: Make this work
	{
		throw new UnsupportedOperationException("This hasn't been implemented yet.");
	}
 
	public UIInterface getUIInterface()
	{
		return uiinterface;
	}

	public void nextTurn(String turn)
	{
		for (Entity e : entities)
			e.doTurn(turn);
	}

	public void addPersistantPlayer(Player p)
	{
		persistant.add(p);
		deadPlayer();
	}

	public void deadPlayer()
	{
		activePlayerCount--;

		if (activePlayerCount == 0)
		{
			if (persistant.isEmpty())
			{
				SoundStuff cam;
				try
				{
					cam = Station7.soundStuff;
					cam.dbc();
					cam = Station7.soundStuff = new SoundStuff();
					cam.swnat();
				}
				catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				generate(persistant.toArray(new Player[persistant.size()]));
				persistant.clear();
				floor++;
				infoInterface.refresh();
				for(Entity e : getEntities())
					e.doTurn("Player");
			}
		}
	}

	public int getRoom(int x, int y) { return roomTiles == null ? -1 : roomTiles[x][y]; }
	public int getRoom(HexPoint p) { return getRoom(getCells().X(p), getCells().Y(p)); }
	
	public HexPoint getEnd()
	{
		return end;
	}

	public int getWidth()
	{
		return floorLayer.length;
	}

	public int getHeight()
	{
		return floorLayer[0].length;
	}

	public int getFloorTile(int x, int y)
	{
		return floorLayer[x][y];
	}

	public int getFloorTile(HexPoint p)
	{
		return getFloorTile(getCells().X(p), getCells().Y(p));
	}

	public HexRect getCells()
	{
		return bounds;
	}

	public List<Entity> getEntities()
	{
		return entities;
	}

	public int getLevelNumber()
	{
		return floor;
	}
}