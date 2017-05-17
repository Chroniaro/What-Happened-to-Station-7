package com.whtss.assets.entities;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.core.Entity.UIEventHandle;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.SoundStuff;
import com.whtss.assets.render.animations.CompoundAnimation;
import com.whtss.assets.render.animations.Laser;
import com.whtss.assets.render.animations.TileDamage;

public class Player_sniper extends Player{
int shots = 2;
int ammo = 0;
int move = 4;

	public Player_sniper(HexPoint location, Level level) {
		super(location, level);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void resetMoves() {
		super.resetMoves();
		ammo = 0;
	}
	
	@Override
	@UIEventHandle(value = "Key_P", turn = "Player")
	public void attack(Entity target) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		SoundStuff cam = new SoundStuff();
		cam.AWP();
		if (ammo + 1 > shots) {
			getLevel().getUIInterface().selectTile(null);
			return;
		}
		if (target == null) {
			return;
		}
		if (!(target instanceof Damageable)) {
			return;
		}
		if (!target.isActive()) {
			return;
		}
		final int d = getLocation().dist(target.getLocation());
		if (d > 15){
			return;
		}
		move += 2;
		ammo += 1;
		if (d > 10){
		((Damageable) target).takeDamage(11 * (15 - d));
		}
		if (d < 10){
			((Damageable) target).takeDamage(50);
			}
		getLevel().getUIInterface().startAnimation(new CompoundAnimation.Sequential(new Laser(getLocation(), target.getLocation()), new TileDamage(target.getLocation())));
		if (move >= speed)
			getLevel().getUIInterface().selectTile(null);
     		else
			getLevel().getUIInterface().selectTile(getLocation());
	}


}
