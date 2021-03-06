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
import com.whtss.assets.render.animations.CompoundAnimation;
import com.whtss.assets.render.animations.Laser;
import com.whtss.assets.render.animations.TileDamage;
import com.whtss.assets.render.sprites.ImageSprite;

public class PlayerSniper extends Player
{
	private final int total_shots = 2;
	private int shots_taken = 0;

	public PlayerSniper(HexPoint location, Level level)
	{
		super(location, level);
		spr = new ImageSprite(this, "PlayerSniper");
		speed = 5;
	}

	@Override
	@UIEventHandle(value = "Next Turn", turn = "Player")
	public void resetMoves()
	{
		super.resetMoves();
		shots_taken = 0;
	}

	@Override
	public int getMaxHealth()
	{
		return 75;
	}

	public int getspeed()
	{
		return speed-move;
	}

	public int getammo()
	{
		return total_shots-shots_taken;
	}

	/**
	 * Attack the enemy from a distance
	 */
	@Override
	@UIEventHandle(value = "Key_P", turn = "Player")
	public void attack(Entity target)
	{
		if (target == null) { return; }
		if (!(target instanceof Damageable)) { return; }
		if (!target.isActive()) { return; }
		if (getLevel().isThroughWall(getLocation(), target.getLocation())) { return; }

		final int d = getLocation().dist(target.getLocation());
		if (d > 15) { return; }

		if (shots_taken + 1 > total_shots || move + 2 > speed)
		{
			getLevel().getUIInterface().selectTile(null);
			return;
		}

		move += 2;
		shots_taken += 1;

		try
		{
			SoundStuff cam = new SoundStuff();
			cam.AWP();
		}
		catch (IOException | LineUnavailableException | UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}

			((Damageable) target).takeDamage(40);

		getLevel().getUIInterface().startAnimation(new CompoundAnimation.Sequential(new Laser(getLocation(), target.getLocation()), new TileDamage(target.getLocation())));
		if (move >= speed && shots_taken >= total_shots)
			getLevel().getUIInterface().selectTile(null);
	}

}
