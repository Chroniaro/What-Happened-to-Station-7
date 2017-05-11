package com.whtss.assets.entities;

import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.animations.BigDamage;
import com.whtss.assets.render.animations.CompoundAnimation;
import com.whtss.assets.render.animations.Laser;
import com.whtss.assets.render.animations.TileDamage;

public class Player extends Entity implements Damageable
{
	final int speed = 70000;
	int move = 0;
	int health = 100;

	public Player(HexPoint location, Level level)
	{
		super(location, level);
	}

	@UIEventHandle(value = "Next Turn", turn = "Player")
	public void resetMoves() //throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException
	{
		//SoundStuff cam = new SoundStuff();
		//cam.swnat();
		move = 0;
	}

	public int gethealth()
	{
		return health;
	}

	public void walk(int da, int db, int dhy)
	{
		int dist = Math.abs(da) + Math.abs(db) + Math.abs(dhy);
		if (getLocation().mABY(da, db, 2 * dhy).equals(getLevel().getEnd()))
		{
			setActive(false);
			getLevel().getUIInterface().selectTile(null);
			getLevel().addPersistantPlayer(this);
		}
		else if (dist + move <= speed)
		{
			if (move(da, db, dhy))
			{
				move += dist;
				getLevel().getUIInterface().selectTile(getLocation());
			}

			if(move >= speed)
				getLevel().getUIInterface().selectTile(null);
		} 
		else
			getLevel().getUIInterface().selectTile(null);
	}

	@UIEventHandle(value = "Key_P", turn = "Player")
	public void attack(Entity target)
	{	
		if(move + 2 > speed)
		{
			getLevel().getUIInterface().selectTile(null);
			return;
		}
		if(target == null)
			return;
		if(!(target instanceof Damageable))
			return;
		if(!target.isActive())
			return;
		
		final int d = getLocation().dist(target.getLocation());
		if(d > 4)
			return;
		move += 2;
		((Damageable)target).takeDamage(10 * (5 - d));
		getLevel().getUIInterface().startAnimation(new CompoundAnimation.Sequential(
				new Laser(getLocation(), target.getLocation()), 
				new TileDamage(target.getLocation()))
			);
		if(move >= speed)
			getLevel().getUIInterface().selectTile(null);
		else
			getLevel().getUIInterface().selectTile(getLocation());
	}
	
	@UIEventHandle(value = "Key_Q", turn = "Player")
	public void walkNA(int modifiers, HexPoint target)
	{
		walk(-1, 0, 0);
	}

	@UIEventHandle(value = "Key_W", turn = "Player")
	public void walkPY(int modifiers, HexPoint target)
	{
		walk(0, 0, 1);
	}

	@UIEventHandle(value = "Key_E", turn = "Player")
	public void walkPB(int modifiers, HexPoint target)
	{
		walk(0, 1, 0);
	}

	@UIEventHandle(value = "Key_A", turn = "Player")
	public void walkNB(int modifiers, HexPoint target)
	{
		walk(0, -1, 0);
	}

	@UIEventHandle(value = "Key_S", turn = "Player")
	public void walkNY(int modifiers, HexPoint target)
	{
		walk(0, 0, -1);
	}

	@UIEventHandle(value = "Key_D", turn="Player")
	public void walkPA(int modifiers, HexPoint target)
	{
		walk(1, 0, 0);
	}

	@Override
	public int getHealth()
	{
		return health;
	}

	@Override
	public void takeDamage(int amount)
	{
//		System.out.println(getHealth());
		health -= amount;
		if(getHealth() < 0)
		{
			setActive(false);
			getLevel().getUIInterface().startAnimation(new BigDamage());
			getLevel().deadPlayer();
		}
	}
}
