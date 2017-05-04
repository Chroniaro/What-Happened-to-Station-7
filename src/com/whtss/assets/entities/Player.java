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
	
	@Override
	public boolean move(int da, int db, int dhy)
	{
		int dist = Math.abs(da) + Math.abs(db) + Math.abs(dhy);
		if(dist + move <= speed)
			if(super.move(da, db, dhy))
			{
				move += dist;
				return true;
			}
		return false;
	}
	
	public UIAction _Q(int modifiers, HexPoint target)
	{
		move(-1, 0, 0);
		return move < speed ? null : success;
	}
	
	public UIAction _W(int modifiers, HexPoint target)
	{
		move(0, 0, 1);
		return move < speed ? null : success;
	}
	
	public UIAction _E(int modifiers, HexPoint target)
	{
		move(0, 1, 0);
		return move < speed ? null : success;
	}
	
	public UIAction _A(int modifiers, HexPoint target)
	{
		move(0, -1, 0);
		return move < speed ? null : success;
	}
	
	public UIAction _S(int modifiers, HexPoint target)
	{
		move(0, 0, -1);
		return move < speed ? null : success;
	}
	
	public UIAction _D(int modifiers, HexPoint target)
	{
		move(1, 0, 0);
		return move < speed ? null : success;
	}
}