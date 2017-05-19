package com.whtss.assets.core;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.whtss.assets.entities.HealBox;
import com.whtss.assets.entities.Player;
import com.whtss.assets.entities.Player_sniper;
import com.whtss.assets.entities.SimpleEnemy;
import com.whtss.assets.entities.Sniper;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.hex.HexRect;
import com.whtss.assets.render.GameInfo;
import com.whtss.assets.render.GameRenderer;
import com.whtss.assets.render.GameRenderer.UIInterface;
import com.whtss.assets.render.SoundStuff;
import com.whtss.assets.util.RandomIterator;
import com.whtss.assets.util.RigidCollection;
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

		generate();
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

		HexPoint[] rooms = buildRooms();
		populateLevel(rooms, players);
		return rooms;
	}

	public void generate()
	{
		generate(new Player(null, this), new Player(null, this), new Player(null, this), new Player(null, this));
	}

	private HexPoint[] buildRooms()
	{
		HexPoint[] centers = chooseRoomCenters();
		roomTiles = divideTerritory(centers);
		buildWalls(centers.length, roomTiles);
		return centers;
	}

	private static HexPoint[] chooseRoomCenters()
	{
		int numOfRooms;
		do
		{
			numOfRooms = (int) Math.round(rand.nextGaussian() * 2 + 7);
		} while (numOfRooms < 3 || numOfRooms > 12);

		HexPoint[] centers = new HexPoint[numOfRooms];

		for (int c = 0; c < numOfRooms; c++)
		{
			final int x = width / 2 - 2 - rand.nextInt(width - 4);
			int y = height / 2 - 2 - rand.nextInt(height - 4);
			y += (x + y) % 2;
			centers[c] = HexPoint.XY(x, y);
			for (int i = 0; i < c; i++)
				if (centers[i].dist(centers[c]) < 4)
				{
					c--;
					break;
				}
		}

		return centers;
	}

	private int[][] divideTerritory(HexPoint[] roomCenters)
	{
		final HexRect lvlCells = getCells();
		final int numOfRooms = roomCenters.length;

		int[][] roomIds = new int[width][height];
		for (int x = 0; x < roomIds.length; x++)
			for (int y = 0; y < roomIds[x].length; y++)
				roomIds[x][y] = -1;

		//Sets up initial rooms
		RigidList<Set<HexPoint>> borders = new RigidList<>(numOfRooms);
		for (int room = 0; room < numOfRooms; room++)
		{
			borders.set(room, new HashSet<>());

			for (HexPoint tile : HexPoint.circ(roomCenters[room], 2))
			{
				if (roomCenters[room].dist(tile) == 2)
					borders.get(room).add(tile);
				else
					roomIds[lvlCells.X(tile)][lvlCells.Y(tile)] = room;
			}
		}

		//Expands them
		List<Integer> rooms = new ArrayList<>();
		for (int i = 0; i < numOfRooms; i++)
			rooms.add(i);
		while (!rooms.isEmpty())
		{
			int roomIndex = rand.nextInt(rooms.size());
			int room = rooms.get(roomIndex);
			Set<HexPoint> border = borders.get(room);

			HashSet<HexPoint> unclaimedTerritory = null;
			HexPoint borderCell = null;

			for (RandomIterator<HexPoint> ri = new RandomIterator<>(border); ri.hasNext();)
			{
				borderCell = ri.next();

				int friendly = 0;
				int wall = 0;
				unclaimedTerritory = new HashSet<HexPoint>();
				boolean valid = true;
				for (HexPoint adjCell : borderCell.adjacentCells())
					if (lvlCells.contains(adjCell))
						if (border.contains(adjCell) && wall < 2)
							wall++;
						else
						{
							int crid = roomIds[lvlCells.X(adjCell)][lvlCells.Y(adjCell)];

							if (crid < 0)
								unclaimedTerritory.add(adjCell);
							else if (crid == room)
								friendly++;
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

			roomIds[lvlCells.X(borderCell)][lvlCells.Y(borderCell)] = room;
			border.remove(borderCell);
		}

		return roomIds;
	}

	public void buildWalls(int numOfRooms, int[][] roomIds)
	{
		final HexRect lvlCells = getCells();
		
		//Keep track of walls
		List<HexPoint> walls = new LinkedList<>();
		for (int x = 0; x < roomIds.length; x++)
			for (int y = 0; y < roomIds[x].length; y++)
				if (roomIds[x][y] < 0)
				{
					walls.add(lvlCells.fromArrayCoords(x, y));
					floorLayer[x][y] = 1;
				}

		//Figure out which rooms are adjacent to which
		Map<RigidCollection<Integer>, Set<HexPoint>> roomLayout = new HashMap<>();
		for (int room1 = 0; room1 < numOfRooms; room1++)
			for (int room2 = room1 + 1; room2 < numOfRooms; room2++)
				roomLayout.put(new RigidCollection<Integer>(room1, room2), new HashSet<>());

		for (HexPoint wall : walls)
		{
			Set<Integer> nearbyRooms = new HashSet<>();

			for (HexPoint adjCell : wall.adjacentCells())
			{
				if (!lvlCells.contains(adjCell))
				{
					nearbyRooms = null;
					break;
				}

				final int adjCellX = lvlCells.X(adjCell), adjCellY = lvlCells.Y(adjCell), adjCellId = roomIds[adjCellX][adjCellY];
				nearbyRooms.add(adjCellId);
			}

			if (nearbyRooms == null || nearbyRooms.size() != 3)
				continue;

			int[] adjRooms = new int[2];
			int i = 0;
			for (Integer nbr : nearbyRooms)
				if (nbr > 0)
					adjRooms[i++] = nbr;

			roomLayout.get(new RigidCollection<>(adjRooms[0], adjRooms[1])).add(wall);
		}

		for (int room1 = 0; room1 < numOfRooms; room1++)
			for (int room2 = room1 + 1; room2 < numOfRooms; room2++)
			{
				Set<HexPoint> wall = roomLayout.get(new RigidCollection<>(room1, room2));
				if (wall.size() <= 1)
					continue;

				int nonHoles = 0;

				for (HexPoint tile : wall)
					if (rand.nextDouble() < (1 + nonHoles) / (wall.size() - 1))
						floorLayer[lvlCells.X(tile)][lvlCells.Y(tile)] = 0;
					else
						nonHoles++;
			}
	}

	private void populateLevel(HexPoint[] rooms, Player... players)
	{
		HexPoint[] leftRooms = new HexPoint[rooms.length / 3];
		HexPoint[] rightRooms = new HexPoint[rooms.length / 3];
		HexPoint[] centerRooms = new HexPoint[rooms.length - leftRooms.length - rightRooms.length];

		for (int room = 0; room < rooms.length; room++)
		{
			HexPoint r = rooms[room];

			for (int i = 0; i < leftRooms.length; i++)
			{
				if (r == null)
					break;
				if (leftRooms[i] == null || r.getX() < leftRooms[i].getX())
				{
					HexPoint lr = leftRooms[i];
					leftRooms[i] = r;
					r = lr;
				}
			}

			for (int i = 0; i < rightRooms.length; i++)
			{
				if (r == null)
					break;
				if (rightRooms[i] == null || r.getX() > rightRooms[i].getX())
				{
					HexPoint rr = rightRooms[i];
					rightRooms[i] = r;
					r = rr;
				}
			}

			if (r != null)
				for (int i = 0; i < centerRooms.length; i++)
					if (centerRooms[i] == null)
					{
						centerRooms[i] = r;
						break;
					}
		}

		HexPoint startRoom, endRoom, enemyRoom, Healroom;

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
			ep.setLocation(startRoom.mABY(playerStartOffsets[i][0], playerStartOffsets[i][1], playerStartOffsets[i][2]));
			ep.setActive(true);
			getEntities().add(players[i]);
		}
		
		Entity ep = new Player_sniper(startRoom, this);
		ep.setActive(true);
		getEntities().add(ep);

		activePlayerCount = players.length;

		getEntities().add(new SimpleEnemy(enemyRoom, this));
		getEntities().add(new Sniper(enemyRoom.mABY(0, 0, 1), this));

		getEntities().add(new HealBox(Healroom.mABY(0, 0, 2), this));
		this.end = endRoom;
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
				//They lost

				System.out.println("U suk.");
				System.out.println("You have failed to save your self…. The swedes trudge on obliterating the remaining inhabitants of the station. .");

				try
				{
					SoundStuff cam = new SoundStuff();
					cam.dbc();
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
				for (Entity e : getEntities())
					e.doTurn("Player");
			}
		}
	}

	public int getRoom(int x, int y)
	{
		return roomTiles == null ? -1 : roomTiles[x][y];
	}

	public int getRoom(HexPoint p)
	{
		return getRoom(getCells().X(p), getCells().Y(p));
	}

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