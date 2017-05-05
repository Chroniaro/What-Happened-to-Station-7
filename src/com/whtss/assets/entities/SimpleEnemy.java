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
	int health = 100;

	public void enmove()
	{
		move(0, 2, 2);
	}

	public void goclose()
	{

	}

	public int gethealth()
	{
		return health;
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
