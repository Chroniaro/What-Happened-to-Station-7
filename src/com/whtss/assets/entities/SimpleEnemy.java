package com.whtss.assets.entities;

import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
// import com.whtss.assets.Entity.UIAction;
import com.whtss.assets.hex.HexPoint;

public class SimpleEnemy extends Entity implements Damageable
{
	final int speed = 50;
	int move = 0;
	//hi
	int health = 100;
	int playersleft = 0;

	public void enmove()
	{
		move(0, 2, 2);
	}
	public void goclose()
	{
	  for(Entity e : getLevel().getEntities())
	  {
		  e.getLocation();
		  
	  }
	}

	public int gethealth()
	{
		return health;
	}

	public int getdist(int da,int db,int dhy)
	{
		int dist = Math.abs(da) + Math.abs(db) + Math.abs(dhy);	
		return dist;
	}

	public SimpleEnemy(HexPoint location, Level level)
	{
		super(location, level);
	}

	@Override
	public void endTurn()
	{

	}

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
