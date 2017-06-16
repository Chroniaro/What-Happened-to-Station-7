package com.whtss.assets.entities;

import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.Animation;
import com.whtss.assets.render.Renderable;
import com.whtss.assets.render.Sprite;
import com.whtss.assets.render.UIEventHandle;
import com.whtss.assets.render.animations.Laser;
import com.whtss.assets.render.sprites.ImageSprite;

public class Enemy extends Entity implements Damageable, Renderable
{
	public static File names = new File("src/com/whtss/assets/core/swednames300");
	final static Random RNG = new Random();

	Sprite spr = new ImageSprite(this, "SwedTank");
	int health = getMaxHealth();

	public Enemy(HexPoint location, Level level)
	{
		super(location, level);
		setName(names, 300);
	}

	//The stuff that happens every turn to make the guy attack the player
	@UIEventHandle(value = "Next Turn", turn = "Enemy")
	public void onTurn() throws InterruptedException
	{
		if (!isActive())
			return;

		for (int i = 0; i <= 4; i++)
			goveryclose();

		for (Entity e : getLevel().getEntities())
			if (e instanceof Player && getLocation().dist(e.getLocation()) < 6 && e.isActive())
				if (!getLevel().isThroughWall(getLocation(), e.getLocation()))
					((Player) e).takeDamage(20);
	}

	//A helper method to move toward nearest the player
	protected void goveryclose()
	{
		Player nearest = getNearestPlayer();

		if (nearest != null)
		{
			int da = (int) Math.signum(nearest.getLocation().getA() - getLocation().getA());
			int db = (int) Math.signum(nearest.getLocation().getB() - getLocation().getB());
			int dy = (int) Math.signum(nearest.getLocation().getY() - getLocation().getY());

			if (move(da, db, dy))
				return;
		}

		move(RNG.nextInt(4) - 2, RNG.nextInt(4) - 2, RNG.nextInt(4) - 2);
	}

	//Finds the nearest player
	Player getNearestPlayer()
	{
		Entity nearest = null;
		int nearestDist = 0;
		for (Entity e : getLevel().getEntities())
			if (e instanceof Player && e.isActive())
			{
				int d = getLocation().dist(e.getLocation());
				if (nearest == null || d < nearestDist)
				{
					nearest = e;
					nearestDist = d;
				}
			}

		return (Player) nearest;
	}

	@Override
	public int getHealth()
	{
		return health;
	}

	@Override
	public int getMaxHealth()
	{
		return 100;
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
