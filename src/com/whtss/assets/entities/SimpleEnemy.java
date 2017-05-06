package com.whtss.assets.entities;

import java.util.Random;
import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;

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
	@UIEventHandle("Next Turn")
	public void Turn() {

		// takeDamage(10);
		for(int i=0;i<=7;i++){
		goveryclose();
		Player best = null;
		for (Entity e : getLevel().getEntities()) {
			e.getLocation();
           if(e instanceof Player){
			if (6 > getLocation().dist(e.getLocation()) && getLocation() != e.getLocation())
				best = (Player)e;
			    best.takeDamage(5);
		}
		}
		best = null;
		}
	}

	public void goveryclose() {
		Entity best = null;
		int min = 12345678;
		for (Entity e : getLevel().getEntities()) {
			e.getLocation();

			if (min > getLocation().dist(e.getLocation()) && getLocation() != e.getLocation())
				best = e;
		}
		int a = 0;
		int b = 0;
		int y = 0;
		if (best.getLocation().getA() <= getLocation().getA()) {
			a = best.getLocation().getA() - getLocation().getA();
		}
		if (best.getLocation().getB() <= getLocation().getB()) {
			b = best.getLocation().getB() - getLocation().getB();
		}
		if (best.getLocation().getY() <= getLocation().getY()) {
			y = best.getLocation().getY() - getLocation().getY();
		}
		if (best.getLocation().getA() < getLocation().getA()) {
			a = getLocation().getA() - best.getLocation().getA();
		}
		if (best.getLocation().getB() < getLocation().getB()) {
			b = getLocation().getB() - best.getLocation().getB();
		}
		if (best.getLocation().getY() < getLocation().getY()) {
			y = getLocation().getY() - best.getLocation().getY();
		}		   
		int u =0;
		int uu = 0;
		int uuu = 0;
		if (y>=1){
			uuu = 1;
		}
		if (a>=1){
			u = -1;
		}
		if (b>=1){
			uu = -1;
		}
		if (y<1){
			uuu = -1;
		}
		if (a<1){
			u = 1;
		}
		if (b<1){
			uu = 1;
		} 
		
		move(u,uu,uuu);
	
		System.out.print("best");
		System.out.print(" " + best.getLocation().getA());
		System.out.print(" " + best.getLocation().getB());
		System.out.print(" " + best.getLocation().getY());
		System.out.println(" ");
		System.out.print("current");
		System.out.print(" " + getLocation().getA());
		System.out.print(" " + getLocation().getB());
		System.out.print(" " + getLocation().getY());
		System.out.println(" ");
		System.out.print(a);
		System.out.print("," + b);
		System.out.print("," + y);
		System.out.println(" ");
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
