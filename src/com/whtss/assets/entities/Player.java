package com.whtss.assets.entities;

import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;

public class Player extends Entity implements Damageable
{
	final int speed = 5;
	int move = 0;
	int health = 100;
	
	public Player(HexPoint location, Level level)
	{
		super(location, level);
	}
	
	@Override
	public void endTurn()
	{
		move = 0;
	}
       
	public int gethealth()
	{
		return health;
	}
	
	public UIAction walk(int da, int db, int dhy)
	{
		int dist = Math.abs(da) + Math.abs(db) + Math.abs(dhy);
		if(getLocation().mABY(da, db, 2 * dhy).equals(getLevel().getEnd()))
		{
			setActive(false);
			return success;
		}
		if(dist + move <= speed)
		{
			if(move(da, db, dhy))
				move += dist;
			
			return null;
		}
		else
			return success;
	}
	
	@UIEventHandle("Q") public UIAction walkNA (int modifiers, HexPoint target) { return walk(-1, 0, 0); }
	@UIEventHandle("W") public UIAction walkPY (int modifiers, HexPoint target) { return walk(0, 0, 1); }
	@UIEventHandle("E") public UIAction walkPB (int modifiers, HexPoint target) { return walk(0, 1, 0); }
	@UIEventHandle("A") public UIAction walkNB (int modifiers, HexPoint target) { return walk(0, -1, 0); }
	@UIEventHandle("S") public UIAction walkNY (int modifiers, HexPoint target) { return walk(0, 0, -1); }
	@UIEventHandle("D") public UIAction walkPA (int modifiers, HexPoint target) { return walk(1, 0, 0); }

	@Override
	public int getHealth()
	{
		return health;
	}

	@Override
	public void takeDamage(int amount)
	{
		health -= amount;
	}
}
