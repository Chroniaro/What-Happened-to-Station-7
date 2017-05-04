package com.whtss.assets.entities;

import java.awt.Color;

import com.whtss.assets.Entity;
import com.whtss.assets.Level;
import com.whtss.assets.hex.HexPoint;

public class Player extends Entity
{
	final int speed = 5;
	int move = 0;
	static int health = 100;
    Color myNewBlue = new Color (5, 5, 5);
	
	public Player(HexPoint location, Level level)
	{
		super(location, level);
	}
	
	@Override
	public void endTurn()
	{
		move = 0;
	}
	public static int gethealth()
	{
		return health;
	}
	
	@Override
	public void move(int da, int db, int dhy)
	{
		move += Math.abs(da) + Math.abs(db) + Math.abs(dhy);
		if(move <= speed)
			super.move(da, db, dhy);
	}
	
	public UIAction _Q(int modifiers, HexPoint target)
	{
		move(-1, 0, 0);
		return null;
	}
	
	public UIAction _W(int modifiers, HexPoint target)
	{
		move(0, 0, 1);
		return null;
	}
	
	public UIAction _E(int modifiers, HexPoint target)
	{
		move(0, 1, 0);
		return null;
	}
	
	public UIAction _A(int modifiers, HexPoint target)
	{
		move(0, -1, 0);
		return null;
	}
	
	public UIAction _S(int modifiers, HexPoint target)
	{
		move(0, 0, -1);
		return null;
	}
	
	public UIAction _D(int modifiers, HexPoint target)
	{
		move(1, 0, 0);
		return null;
	}
}