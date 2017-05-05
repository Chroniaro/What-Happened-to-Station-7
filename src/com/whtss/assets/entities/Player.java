package com.whtss.assets.entities;

import com.whtss.assets.Entity;
import com.whtss.assets.Level;
import com.whtss.assets.hex.HexPoint;

public class Player extends Entity
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
	
	public UIAction _Q(int modifiers, HexPoint target)
	{
		return walk(-1, 0, 0);
	}
	
	public UIAction _W(int modifiers, HexPoint target)
	{
		return walk(0, 0, 1);
	}
	public UIAction _P(int modifiers, HexPoint target)
	{
		return walk(0, 0, 1);
	}
	
	
	public UIAction _E(int modifiers, HexPoint target)
	{
		return walk(0, 1, 0);
	}
	
	public UIAction _A(int modifiers, HexPoint target)
	{
		return walk(0, -1, 0);
	}
	
	public UIAction _S(int modifiers, HexPoint target)
	{
		return walk(0, 0, -1);
	}
	
	public UIAction _D(int modifiers, HexPoint target)
	{
		return walk(1, 0, 0);
	}
}
