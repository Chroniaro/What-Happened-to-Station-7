package com.whtss.assets.entities;

import java.util.Random;
import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.animations.Laser;

public class SimpleEnemy extends Entity implements Damageable {
	Random rand = new Random();
	
	int speed = 5;
	int move = 0;
	// hi
	int health = 100;
	int playersleft = 0;

	public void enmove() {
		move(0, 2, 2);
	}

	// @Override
	@UIEventHandle(value = "Next Turn", turn = "Enemy")
	public void Turn() throws InterruptedException {

		if(!isActive())
			return;
		// System.out.println("Turn");

		// takeDamage(10);
		for (int i = 0; i <= 4; i++) {
			goveryclose();
			for (int ii = 0; ii <= 5; ii++) {
				Player best = null;
				for (Entity e : getLevel().getEntities())
					if (getLocation().dist(e.getLocation()) < 6 && e.isActive() == true) {
						if (e instanceof Player) {
							best = (Player) e;
							new Laser(best.getLocation(),getLocation());
							best.takeDamage(1);
//							System.out.println(best + " " + getLocation().dist(best.getLocation()));
						}
					}
			}

		}
	}

	public void goveryclose() {
		Entity bestest = null;
		int min = 11111111;
		HexPoint current = getLocation();
		for (Entity e : getLevel().getEntities()) {
//			e.getLocation();

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

		// System.out.print("best");
		// System.out.print(" " + best.getLocation().getA());
		// System.out.print(" " + best.getLocation().getB());
		// System.out.print(" " + best.getLocation().getY());
		// System.out.println(" ");
		// System.out.print("current");
		// System.out.print(" " + getLocation().getA());
		// System.out.print(" " + getLocation().getB());
		// System.out.print(" " + getLocation().getY());
		// System.out.println(" ");
		// System.out.print(a);
		// System.out.print("," + b);
		// System.out.print("," + y);
		// System.out.println(" ");
		if (getLocation() == current){
			int eu = rand.nextInt(4) -2;
			int euu = rand.nextInt(4) -2;
			int euuu = rand.nextInt(4) -2;
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

	public SimpleEnemy(HexPoint location, Level level) {
		super(location, level);
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
