package com.whtss.assets.entities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.core.SoundStuff;
import com.whtss.assets.hex.HexPoint;

public class Player extends Entity implements Damageable
{
	final int speed = 5;
	int move = 0;
	int health = 100;

	public Player(HexPoint location, Level level)
	{
		super(location, level);
	}

	@UIEventHandle("Next Turn")
	public void resetMoves() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		SoundStuff cam = new SoundStuff();
		cam.swnat();
		move = 0;
	}

	public int gethealth()
	{
		
		return health;
	}

	public UIAction walk(int da, int db, int dhy)
	{
		int dist = Math.abs(da) + Math.abs(db) + Math.abs(dhy);
		if (getLocation().mABY(da, db, 2 * dhy).equals(getLevel().getEnd()))
		{
			setActive(false);
			return success;
		}
		if (dist + move <= speed)
		{
			if (move(da, db, dhy))
				move += dist;

			return null;
		}
		else
			return success;
	}

	@UIEventHandle("Key_Q")
	public UIAction walkNA(int modifiers, HexPoint target)
	{
		return walk(-1, 0, 0);
	}

	@UIEventHandle("Key_W")
	public UIAction walkPY(int modifiers, HexPoint target)
	{
		return walk(0, 0, 1);
	}

	@UIEventHandle("Key_E")
	public UIAction walkPB(int modifiers, HexPoint target)
	{
		return walk(0, 1, 0);
	}

	@UIEventHandle("Key_A")
	public UIAction walkNB(int modifiers, HexPoint target)
	{
		return walk(0, -1, 0);
	}

	@UIEventHandle("Key_S")
	public UIAction walkNY(int modifiers, HexPoint target)
	{
		return walk(0, 0, -1);
	}

	@UIEventHandle("Key_D")
	public UIAction walkPA(int modifiers, HexPoint target)
	{
		return walk(1, 0, 0);
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
		if(health < 0)
			setActive(false);
	}
}
