package com.whtss.assets.entities;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.whtss.assets.Station7;
import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.Renderable;
import com.whtss.assets.render.SoundStuff;
import com.whtss.assets.render.Sprite;
import com.whtss.assets.render.UIEventHandle;
import com.whtss.assets.render.animations.BigDamage;
import com.whtss.assets.render.animations.CompoundAnimation;
import com.whtss.assets.render.animations.Laser;
import com.whtss.assets.render.animations.TileDamage;
import com.whtss.assets.render.sprites.ImageSprite;

public class Player extends Entity implements Damageable, Renderable
{
	public final static File namesList = new File("src/com/whtss/assets/core/playernames80");
	Sprite spr = new ImageSprite(this, "Player");
	int speed = 7;
	int move = 0;
	int health = getMaxHealth();

	public Player(HexPoint location, Level level)
	{
		super(location, level);
		setName(namesList, 80);
	}

	public int getspeed()
	{
		return speed - move;
	}

	@UIEventHandle(value = "Next Turn", turn = "Player")
	public void resetMoves()
	{
		move = 0;
	}

	public int gethealth()
	{
		return health;
	}

	/**
	 * Tries to move to the specified tile, failing if there are insufficient action points or if that isn't an
	 * available tile
	 * 
	 * @param da A relative A coordinate to move to
	 * @param db A relative B coordinate to move to
	 * @param dhy Half the relative Y coordinate to move to (halved because of the intricacies of the coordinate system,
	 * a value of one for this parameter corresponds to one tile up.)
	 */
	public void walk(int da, int db, int dhy)
	{
		HexPoint target = getLocation().mABY(da, db, 2 * dhy);
		int dist = getLocation().dist(target);
		if (target.equals(getLevel().getEnd()))
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

			if (move >= speed)
				getLevel().getUIInterface().selectTile(null);
		}
		else
			getLevel().getUIInterface().selectTile(null);
	}
	
	/**
	 * A dev key to kill the selected player
	 */
	@UIEventHandle(value = "Key_M", turn = "Player")
	public void kill()
	{
		if (Station7.DEV)
		{
			setActive(false);
			getLevel().deadPlayer();
		}
	}

	/**
	 * A dev key to send the selected player to the next level
	 */
	@UIEventHandle(value = "Key_N", turn = "Player")
	public void complete()
	{
		if (Station7.DEV)
		{
			setActive(false);
			getLevel().addPersistantPlayer(this);
			getLevel().getUIInterface().selectTile(getLocation());
		}
	}

	/**
	 * Attack the enemy under the mouse
	 */
	@UIEventHandle(value = "Key_P", turn = "Player")
	public void attack(Entity target)
	{
		if (target == null) { return; }
		if (!(target instanceof Damageable)) { return; }
		if (!target.isActive()) { return; }
		if (getLevel().isThroughWall(getLocation(), target.getLocation())) { return; }

		if (move + 2 > speed)
		{
			getLevel().getUIInterface().selectTile(null);
			return;
		}

		final int d = getLocation().dist(target.getLocation());
		if (d > 4) { return; }

		move += 2;

		SoundStuff cam;
		try
		{
			cam = new SoundStuff();
			cam.Phazing();
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
			e.printStackTrace();
		}

		((Damageable) target).takeDamage(8 * (5 - d));
		getLevel().getUIInterface().startAnimation(new CompoundAnimation.Sequential(new Laser(getLocation(), target.getLocation()), new TileDamage(target.getLocation())));
		if (move >= speed)
			getLevel().getUIInterface().selectTile(null);
	}

	//Various move methods that receive key events
	
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

	@UIEventHandle(value = "Key_D", turn = "Player")
	public void walkPA(int modifiers, HexPoint target)
	{
		walk(1, 0, 0);
	}

	//Same as the ones above but in a loop
	
	@UIEventHandle(value = "Key_U", turn = "Player")
	public void walkNAf(int modifiers, HexPoint target)
	{
		for (int f = 0; isActive() && f <= 5; f++)
		{
			walk(-1, 0, 0);
		}
	}

	@UIEventHandle(value = "Key_I", turn = "Player")
	public void walkPYf(int modifiers, HexPoint target)
	{
		for (int f = 0; isActive() && f <= 5; f++)
		{
			walk(0, 0, 1);
		}
	}

	@UIEventHandle(value = "Key_O", turn = "Player")
	public void walkPBf(int modifiers, HexPoint target)
	{
		for (int f = 0; isActive() && f <= 5; f++)
		{
			walk(0, 1, 0);
		}
	}

	@UIEventHandle(value = "Key_J", turn = "Player")
	public void walkNBf(int modifiers, HexPoint target)
	{
		for (int f = 0; isActive() && f <= 5; f++)
		{
			walk(0, -1, 0);
		}
	}

	@UIEventHandle(value = "Key_K", turn = "Player")
	public void walkNYf(int modifiers, HexPoint target)
	{
		for (int f = 0; isActive() && f <= 5; f++)
		{
			walk(0, 0, -1);
		}
	}

	@UIEventHandle(value = "Key_L", turn = "Player")
	public void walkPAf(int modifiers, HexPoint target)
	{
		for (int f = 0; isActive() && f <= 5; f++)
		{
			walk(1, 0, 0);
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
		return 100;
	}

	@Override
	public void takeDamage(int amount)
	{
		health -= amount;

		if (getHealth() < 0)
		{
			setActive(false);
			getLevel().getUIInterface().startAnimation(new BigDamage());
			getLevel().deadPlayer();
		}
	}

	@Override
	public Sprite getSprite()
	{
		return spr;
	}
}
