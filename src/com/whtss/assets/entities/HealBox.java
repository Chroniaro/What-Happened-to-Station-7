package com.whtss.assets.entities;

import java.util.Random;

import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;

public class HealBox extends Entity implements Damageable {
	public HealBox(HexPoint location, Level level) {
		super(location, level);
		// TODO Auto-generated constructor stub
	}

	Random rand = new Random();
	int speed = 5;
	int move = 0;
	// hi
	int health = 300;
	int playersleft = 0;

	public void enmove() {
		move(0, 2, 2);
	}

	// @Override
	@UIEventHandle("Next Turn")
	public void Turn() {

		// takeDamage(10);
		for(int i=0;i<=5;i++){
		
		Player best = null;
		for (Entity e : getLevel().getEntities()) {
			e.getLocation();
           if(e instanceof Player){
			if (3 > getLocation().dist(e.getLocation()) && getLocation() != e.getLocation())
				best = (Player)e;
			    best.takeDamage(-37);
			    takeDamage(40);
		}
		}
		best.takeDamage(-37);
		takeDamage(40);
		}
	}

	

	public int gethealth() {
		return health;
	}

	public int getdist(int da, int db, int dhy) {
		int dist = Math.abs(da) + Math.abs(db) + Math.abs(dhy);
		return dist;
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
