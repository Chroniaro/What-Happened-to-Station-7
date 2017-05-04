package com.whtss.assets.entities;

import com.whtss.assets.Entity;
import com.whtss.assets.Level;
import com.whtss.assets.hex.HexPoint;

public class SimpleEnemy extends Entity
{
	public SimpleEnemy(HexPoint location, Level level)
	{
		super(location, level);
	}
	
	@Override
	public void endTurn()
	{
		
	}
}
