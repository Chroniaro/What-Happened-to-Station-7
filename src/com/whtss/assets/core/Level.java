package com.whtss.assets.core;

import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.whtss.assets.entities.Enemy;
import com.whtss.assets.entities.EnemySniper;
import com.whtss.assets.entities.HealBox;
import com.whtss.assets.entities.Player;
import com.whtss.assets.entities.PlayerSniper;
import com.whtss.assets.entities.Shotgun_Enemy;
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
	private static final Random RNG = new Random();

	private final static int width = 41, height = 19;

	//Layers
	private int[][] floorLayer;
	private LevelObject[][] objectLayer;
	private Collection<Entity> entities;
	private int[][] roomTiles = null;
	//	TODO: private byte[][][][] pathing;

	private final HexRect cellsRect;
	private HexPoint endPoint;
	private UIInterface uiinterface;
	private GameInfo.UIInterface infoInterface;
	private final int[][] playerStartOffsets = new int[][] { { 0, 0 }, { 1, 0 }, { 0, -1 }, { -1, 1 }, { 1, -1 }, { 0, 1 }, { -1, 0 } };
	private Collection<Player> persistant;
	private int activePlayerCount;
	private int floor = 1;

	public Level(GameRenderer.UIInterface UIInterface, GameInfo.UIInterface infoInterface)
	{
		uiinterface = UIInterface;
		this.infoInterface = infoInterface;

		floorLayer = new int[width][height];
		objectLayer = new LevelObject[width][height];

		persistant = new LinkedList<Player>();

		int dw = -width / 2;
		int dh = 1 - height;
		cellsRect = HexPoint.rect(HexPoint.origin.mXY(dw, dh + (dh + dw) % 2), width, height);
		entities = new HashSet<>();

		generate();
	}

	public void generate()
	{
		generate(new PlayerSniper(null, this), new Player(null, this), new Player(null, this), new Player(null, this));
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

		HexPoint[] centers = chooseRoomCenters();
		roomTiles = divideTerritory(centers);
		buildWalls(centers.length, roomTiles);
		populateLevel(centers, players);
		return centers;
	}

	private static HexPoint[] chooseRoomCenters()
	{
		int numOfRooms;
		do
		{
			numOfRooms = (int) Math.round(RNG.nextGaussian() * 2 + 7);
		} while (numOfRooms < 3 || numOfRooms > 12);

		HexPoint[] centers = new HexPoint[numOfRooms];

		for (int c = 0; c < numOfRooms; c++)
		{
			final int x = width / 2 - 2 - RNG.nextInt(width - 4);
			int y = height / 2 - 2 - RNG.nextInt(height - 4);
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
			int roomIndex = RNG.nextInt(rooms.size());
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
		Map<RigidCollection<Integer>, Collection<HexPoint>> roomLayout = new HashMap<>();
		for (int room1 = 0; room1 < numOfRooms; room1++)
			for (int room2 = room1 + 1; room2 < numOfRooms; room2++)
				roomLayout.put(new RigidCollection<Integer>(room1, room2), new HashSet<>());

		for (HexPoint wall : walls)
		{
			Collection<Integer> nearbyRooms = new HashSet<>();

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
				Collection<HexPoint> wall = roomLayout.get(new RigidCollection<>(room1, room2));
				if (wall.size() <= 1)
					continue;

				int nonHoles = 0;

				for (HexPoint tile : wall)
					if (RNG.nextDouble() < (1 + nonHoles) / (wall.size() - 1))
						floorLayer[lvlCells.X(tile)][lvlCells.Y(tile)] = 0;
					else
						nonHoles++;
			}
	}

	private void populateLevel(HexPoint[] rooms, Player... players)
	{
		HexPoint[] leftRooms = new HexPoint[rooms.length / 3];
		HexPoint[] rightRooms = new HexPoint[rooms.length / 3];
		HexPoint[] centerRooms = new HexPoint[rooms.length / 3 + rooms.length % 3];

		for (int r = 0; r < rooms.length; r++)
		{
			HexPoint room = rooms[r];

			for (int i = 0; i < leftRooms.length; i++)
				if (leftRooms[i] == null || room.getX() < leftRooms[i].getX())
				{
					HexPoint tmp = leftRooms[i];
					leftRooms[i] = room;
					room = tmp;
				}

			if (room != null)
			{
				for (int i = 0; i < rightRooms.length; i++)
					if (rightRooms[i] == null || room.getX() > rightRooms[i].getX())
					{
						HexPoint tmp = rightRooms[i];
						rightRooms[i] = room;
						room = tmp;
					}

				if (room != null)
					centerRooms[r - rooms.length / 3 * 2] = room;
			}
		}

		HexPoint startRoom, endRoom, enemyRoom, Healroom, pbonus;

		startRoom = leftRooms[RNG.nextInt(leftRooms.length)];
		endRoom = rightRooms[RNG.nextInt(rightRooms.length)];

		if (RNG.nextBoolean())
		{
			HexPoint tmp = startRoom;
			startRoom = endRoom;
			endRoom = tmp;
		}

		enemyRoom = centerRooms[RNG.nextInt(centerRooms.length)];
		Healroom = centerRooms[RNG.nextInt(centerRooms.length)];

		pbonus = rooms[RNG.nextInt(rooms.length)];

		activePlayerCount = Math.min(players.length, playerStartOffsets.length);
		for (int i = 0; i < activePlayerCount; i++)
		{
			Entity ep = players[i];
			ep.setLocation(startRoom.mAB(playerStartOffsets[i][0], playerStartOffsets[i][1]));
			ep.setActive(true);
			getEntities().add(ep);
		}
		double r = Math.random();
		double x = Math.random();
		double z = Math.random();

		if (x > .40 && x < .80)
		{
			getEntities().add(new Enemy(enemyRoom.mY(-2), this));
		}
		if (x < .40)
		{
			getEntities().add(new EnemySniper(enemyRoom.mY(-2), this));
		}
		if (x > .80)
		{
			getEntities().add(new Shotgun_Enemy(enemyRoom.mY(-2), this));
		}

		if (z > .40)
		{
			getEntities().add(new Enemy(enemyRoom.mY(2), this));
		}
		if (z < .40)
		{
			getEntities().add(new EnemySniper(enemyRoom.mY(2), this));
		}

		getEntities().add(new HealBox(Healroom, this));

		if (Math.random() < .50)
		{
			if (r < .60)
			{
				getEntities().add(new Player(pbonus, this));
			}
			if (r > .60)
			{
				getEntities().add(new PlayerSniper(pbonus, this));
			}
		}
		this.endPoint = endRoom;
	}

	//TODO: add this
	public void calcPathFinding()
	{

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

	/**
	 * Not yet implemented
	 */
	public Path getPath(HexPoint start, HexPoint end) //TODO: Make this work
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

	public int thingsToPenetrateDeeplyAndThouroughly(HexPoint start, HexPoint end)
	{
		HexRect r = getCells();
		if (!(r.contains(start) && r.contains(end)))
			return -1;

		Line2D.Double line = new Line2D.Double(start.getVisualX(10), start.getVisualY(10), end.getVisualX(10), end.getVisualY(10));
		int tx = Math.min(start.getX(), end.getX()) - 1;
		int ty = Math.min(start.getY(), end.getY()) - 1;
		ty -= (ty + tx) % 2;
		int w = Math.abs(start.getX() - end.getX()) + 3;
		int h = Math.abs(start.getY() - end.getY()) + 3;
		r = new HexRect(HexPoint.XY(tx, ty), w, h);
		int intersections = 0;
		
		Set<HexPoint> checkPoints = new HashSet<HexPoint>();
		
		for (HexPoint p : r)
			if (getCells().contains(p))
				if (getFloorTile(p) % 2 != 0)
					checkPoints.add(p);
		for (Entity e : getEntities())
			checkPoints.add(e.getLocation());
		
		for (HexPoint p : checkPoints)
			if (line.intersects(p.getBorder(10).getBounds2D()))
				if(!(p.equals(start) || p.equals(end)))
					intersections++;

		return intersections;
	}

	public boolean isThroughWall(HexPoint start, HexPoint end)
	{
		int t = thingsToPenetrateDeeplyAndThouroughly(start, end);
		return (t == -1 || t > 0);
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
		return endPoint;
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
		return cellsRect;
	}

	public Collection<Entity> getEntities()
	{
		return entities;
	}

	public int getLevelNumber()
	{
		return floor;
	}
}

//TODO: Make this work
@SuppressWarnings("unused")
class Path implements Iterable<HexPoint>
{
	private final int dist;
	private final byte dir;

	public Path(int x1, int y1, int x2, int y2)
	{
		dist = 0;
		dir = 0;
	}

	public int getDist()
	{
		return dist;
	}

	@Override
	public Iterator<HexPoint> iterator()
	{
		return null;
	}

	class PathIterator implements Iterator<HexPoint>
	{
		@Override
		public boolean hasNext()
		{
			return false;
		}

		@Override
		public HexPoint next()
		{
			return null;
		}
	}
}