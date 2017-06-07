package com.whtss.assets.entities;


import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.SoundStuff;
import com.whtss.assets.render.UIEventHandle;
import com.whtss.assets.render.sprites.ImageSprite;

public class Shotgun_Enemy extends Enemy
{	
	public Shotgun_Enemy(HexPoint location, Level level)
	{
		super(location, level);
		spr = new ImageSprite(this, "SwedSniper");
	}

	@Override
	@UIEventHandle(value = "Next Turn", turn = "Enemy")
	public void onTurn()
	{
		if (!isActive())
			return;

		goveryclose();
		attack();
	}

	public void attack()
	{

		for (Entity e : getLevel().getEntities()){
			if (e.isActive() && e instanceof Player)
				
				if (!getLevel().isThroughWall(getLocation(), e.getLocation()) && getLocation().dist(((Player) e).getLocation()) < 4)
				{
					Player p = (Player) e;
					p.takeDamage(80);
				}


		try
		{
			SoundStuff cam = new SoundStuff();
			cam.AWP();
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1)
		{
			e1.printStackTrace();
		}
		}
		//		getLevel().getUIInterface().startAnimation(new Laser(getLocation(), weakest.getLocation()));
	}


	@Override
	public int getHealth()
	{
		return health;
	}

	@Override
	public int getMaxHealth()
	{
		return 125;
	}

	@Override
	public void takeDamage(int amount)
	{
		health -= amount;
		if (health < 0)
			setActive(false);
	}
}