package com.whtss.assets;

import java.awt.event.KeyEvent;
import com.whtss.assets.hex.HexPoint;

public class Game
{
	Level x = new Level();
	int floor = 1;
	private Runnable turnUpdateStuff;
	
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
	
	public void setNextTurnRunnable(Runnable nextTurn)
	{
		turnUpdateStuff = nextTurn;
	}
	
	public void nextTurn()
	{
		turnUpdateStuff.run();
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