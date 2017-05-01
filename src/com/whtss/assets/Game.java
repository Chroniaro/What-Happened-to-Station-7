package com.whtss.assets;

public class Game
{
	Level x = new Level();
	int floor = 1;
	public Level getCurrentLevel()
	{
		return x;
	}
	
	public int getfloor()
	{
		return floor;
	}
	
	public void nextfloor()
	{
		floor++;
	}
	public void update(long deltaT)
	{
		
	}
}