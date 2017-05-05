package com.whtss.assets;

import java.awt.event.KeyEvent;
import com.whtss.assets.hex.HexPoint;

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
	
	public void nextTurn()
	{
		getCurrentLevel().nextTurn();
	}
	
	public HexPoint processAction(HexPoint select, HexPoint mouse, KeyEvent key)
	{
		switch(key.getKeyCode())
		{
			default:
				if(select == null)
					return null;
				else
					return getCurrentLevel().performAction(select, mouse, key);
		}
	}
}