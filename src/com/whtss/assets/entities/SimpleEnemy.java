package com.whtss.assets.entities;

import com.whtss.assets.Entity;
import com.whtss.assets.Level;
//import com.whtss.assets.Entity.UIAction;
import com.whtss.assets.hex.HexPoint;

public class SimpleEnemy extends Entity
{
	final int speed = 50;
	int move = 0;
	int health = 100;
	public void enmove(){
		move(0,2,2);
	}
	public void goclose(){
		
		
	}
	public SimpleEnemy(HexPoint location, Level level)
	{
		super(location, level);
	}
	@Override
	public void endTurn()
	{
		
	}
}
