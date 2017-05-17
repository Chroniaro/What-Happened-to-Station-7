package com.whtss.assets.entities;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.SoundStuff;

public class Flame_thrower extends Entity implements Damageable {
	Random rand = new Random();
    boolean temp = true;
	int speed = 5;
	int move = 0;
	// hi
	int health = 100;
	int playersleft = 0;
	public List<HexPoint> flametilesprime;

	public void enmove() {
		move(0, 2, 2);
	}

	// @Override
	@UIEventHandle(value = "Next Turn", turn = "Enemy")
	public void Turn() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		if (!isActive())
			return;
		// System.out.println("Turn");

		// takeDamage(10);
		for (int i = 0; i <= 10; i++) {
			goveryclose();
			flame();

		}
	}

	public void goveryclose() {
		Entity bestest = null;
		int min = 11111111;
		HexPoint current = getLocation();
		for (Entity e : getLevel().getEntities()) {
			// e.getLocation();

			if (min > getLocation().dist(e.getLocation()) && e.isActive() == true && e instanceof Player)
				bestest = e;
		}
		int a = 0;
		int b = 0;
		int y = 0;
		if (bestest.getLocation().getA() <= getLocation().getA()) {
			a = bestest.getLocation().getA() - getLocation().getA();
		}
		if (bestest.getLocation().getB() <= getLocation().getB()) {
			b = bestest.getLocation().getB() - getLocation().getB();
		}
		if (bestest.getLocation().getY() <= getLocation().getY()) {
			y = bestest.getLocation().getY() - getLocation().getY();
		}
		if (bestest.getLocation().getA() < getLocation().getA()) {
			a = getLocation().getA() - bestest.getLocation().getA();
		}
		if (bestest.getLocation().getB() < getLocation().getB()) {
			b = getLocation().getB() - bestest.getLocation().getB();
		}
		if (bestest.getLocation().getY() < getLocation().getY()) {
			y = getLocation().getY() - bestest.getLocation().getY();
		}
		int u = 0;
		int uu = 0;
		int uuu = 0;
		if (y >= 1) {
			uuu = 1;
		}
		if (a >= 1) {
			u = -1;
		}
		if (b >= 1) {
			uu = -1;
		}
		if (y < 1) {
			uuu = -1;
		}
		if (a < 1) {
			u = 1;
		}
		if (b < 1) {
			uu = 1;
		}

		move(u, uu, uuu);
		if (getLocation() == current) {
			int eu = rand.nextInt(4) - 2;
			int euu = rand.nextInt(4) - 2;
			int euuu = rand.nextInt(4) - 2;
			move(eu, euu, euuu);
		}
	}

	public int gethealth() {
		return health;
	}

	public int getdist(int da, int db, int dhy) {
		int dist = Math.abs(da) + Math.abs(db) + Math.abs(dhy);
		return dist;
	}

	public Flame_thrower(HexPoint location, Level level) {
		super(location, level);
	}

	public void flame() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		boolean ittt = false;
		if(temp == true){
		for (int ii = 0; ii <= 5; ii++) {
			Player best = null;
			for (Entity e : getLevel().getEntities())
				if (getLocation().dist(e.getLocation()) < 6 && e.isActive() == true) {
					if (e instanceof Player) {
						best = (Player) e;
						addflametile(best.getLocation());
						ittt = true;
						SoundStuff cam = new SoundStuff();
						cam.flame();
					}
				}
		    } 
		}
		if(ittt==false){
			int eu = rand.nextInt(4) -2;
			int euu = rand.nextInt(4) -2;
			int euuu = rand.nextInt(4) -2;
			flameer(eu, euu, euuu);
			SoundStuff cam = new SoundStuff();
			cam.flame();
			
		}
		
	}

	public void noone() {

		speed++;

		if (move(rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)) == false) {
			while (move(rand.nextInt(4), rand.nextInt(2), rand.nextInt(2)) == false) {
				move(rand.nextInt(2), rand.nextInt(2), rand.nextInt(2));
				speed++;
			}
		}
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void takeDamage(int amount) {
		health -= amount;
		if (health < 0)
			setActive(false);
	}
}