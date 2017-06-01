package com.whtss.assets.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.SoundStuff;
import com.whtss.assets.render.UIEventHandle;
import com.whtss.assets.render.sprites.ImageSprite;

public class EnemySniper extends Enemy
{
	public EnemySniper(HexPoint location, Level level)
	{
		super(location, level);
		spr = new ImageSprite(this, "SwedSniper");
	}
	String name = " ";
	File file = new File("src/com/whtss/assets/core/swednames300");
	
	int r = (int) (Math.random()*299);
	public void setname(){
	Stream<String> lines = null;
	try {
		lines = Files.lines(Paths.get("src/com/whtss/assets/core/swednames300"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  name = lines.skip(r).findFirst().get();
	}
	
    public String getname(){
		
		return name;
	}
	@Override
	@UIEventHandle(value = "Next Turn", turn = "Enemy")
	public void onTurn()
	{
		if (!isActive())
			return;

		goModeretlyFar();
		attack();
	}

	public void attack()
	{
		Player weakest = null;
		
		for (Entity e : getLevel().getEntities())
			if (e.isActive() && e instanceof Player)
			{
				Player p = (Player) e;
				if(weakest == null || p.getHealth() < weakest.getHealth())
					weakest = p;
			}
		
		weakest.takeDamage(57);
		
		try
		{
			SoundStuff cam = new SoundStuff();
			cam.AWP();
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1)
		{
			e1.printStackTrace();
		}
		
//		getLevel().getUIInterface().startAnimation(new Laser(getLocation(), weakest.getLocation()));
	}

	public void goModeretlyFar()
	{
		Entity nearest = getNearestPlayer();

		if(nearest != null)
		{
			int da = (int) Math.signum(nearest.getLocation().getA() - getLocation().getA());
			int db = (int) Math.signum(nearest.getLocation().getB() - getLocation().getB());
			int dy = (int) Math.signum(nearest.getLocation().getY() - getLocation().getY());

			if (getLocation().dist(nearest.getLocation()) < 10)
			{
				da = -da;
				db = -db;
				dy = -dy;
			}

			if(move(da, db, dy))
				return;
		}
		
		move(RNG.nextInt(4) - 2, RNG.nextInt(4) - 2, RNG.nextInt(4) - 2);
	}



	@Override
	public int getHealth()
	{
		return health;
	}
	@Override
	public int getMaxHealth()
	{
		return 75;
	}

	@Override
	public void takeDamage(int amount)
	{
		health -= amount;
		if (health < 0)
			setActive(false);
	}
}
