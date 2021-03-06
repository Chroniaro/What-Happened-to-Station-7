package com.whtss.assets.entities;

import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.Renderable;
import com.whtss.assets.render.Sprite;
import com.whtss.assets.render.UIEventHandle;
import com.whtss.assets.render.sprites.ImageSprite;

public class HealBox extends Entity implements Damageable, Renderable
{
	int health = getMaxHealth();
	Sprite spr = new ImageSprite(this, "HealthBox");
	boolean padd;
	
	public HealBox(HexPoint location, Level level)
	{
		super(location, level);
	}
	public boolean getpadd(){
		return padd;	
	}

	/**
	 * Every turn, if there's a nearby entity, heal them and take damage.
	 */
	@UIEventHandle(value = "Next Turn", turn = "Enemy")
	public void onTurn()
	{
		if(!isActive())
			return;
		
		for (int i = 0; i <= 5; i++)
		{
			for (Entity e : getLevel().getEntities())
				
					if (getLocation().dist(e.getLocation()) <= 3 && ((Damageable) e).getHealth()< 75)
					{
						((Damageable) e).takeDamage(-20);
						takeDamage(40);
						if(!isActive())
							return;
					}
		}
	}

	@Override
	public int getHealth()
	{
		return health;
	}
	
	@Override
	public int getMaxHealth()
	{
		return 300;
	}

	@Override
	public void takeDamage(int amount)
	{
		health -= amount;
		if (health < 0)
			setActive(false);
	}
	
	@Override
	public Sprite getSprite()
	{
		return spr;
	}
}
