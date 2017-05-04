package com.whtss.assets.entities;

import com.whtss.assets.Entity;
import com.whtss.assets.Level;
import com.whtss.assets.hex.HexPoint;

public class Player extends Entity
{
	public Player(HexPoint location, Level level)
	{
		super(location, level);
	}
	
	public UIAction _Q(int modifiers, HexPoint target)
	{
		setLocation(getLocation().mA(-1));
		return null;
	}
	
	public UIAction _W(int modifiers, HexPoint target)
	{
		setLocation(getLocation().mY(-2));
		return null;
	}
	
	public UIAction _E(int modifiers, HexPoint target)
	{
		setLocation(getLocation().mB(1));
		return null;
	}
	
	public UIAction _A(int modifiers, HexPoint target)
	{
		setLocation(getLocation().mB(-1));
		return null;
	}
	
	public UIAction _S(int modifiers, HexPoint target)
	{
		setLocation(getLocation().mY(2));
		return null;
	}
	
	public UIAction _D(int modifiers, HexPoint target)
	{
		setLocation(getLocation().mA(1));
		return null;
	}
}