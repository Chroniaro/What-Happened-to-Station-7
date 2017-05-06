package com.whtss.assets.entities;

import java.util.Random;

import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
// import com.whtss.assets.Entity.UIAction;
import com.whtss.assets.hex.HexPoint;

public class SimpleEnemy extends Entity implements Damageable
{
	Random rand = new Random(); 
	int speed = 5;
	int move = 0;
	//hi
	int health = 100;
	int playersleft = 0;

	public void enmove()
	{
		move(0, 2, 2);
	}
	
//	@Override
	@UIEventHandle("Next Turn")
	public void Turn()
	{ 
		System.out.print("adgasdgfa");
		takeDamage(10);
	}
	
	
	
	public void goclose()
	{
	  Entity best = null;
	  int min = 12345678;
	  for(Entity e : getLevel().getEntities())
	  {
		  e.getLocation();
		  
		if(min > getLocation().dist(e.getLocation()))
		   best = e;
	  }
	   int a = best.getLocation().getA()-getLocation().getA();
	   int b = best.getLocation().getB()- getLocation().getB();
	   int y = getLocation().getY()-best.getLocation().getY();
	   move(a-1,b,y);
	   System.out.print("aerg");
	   
	}

	public int gethealth()
	{
		return health;
	}

	public int getdist(int da,int db,int dhy)
	{
		int dist = Math.abs(da) + Math.abs(db) + Math.abs(dhy);	
		return dist;
	}
	public SimpleEnemy(HexPoint location, Level level)
	{
		super(location, level);
	}
	public void noone()
	{ 
		
		speed++;
//		if (move(rand.nextInt(2),rand.nextInt(2),rand.nextInt(2)) == false){
//			while(move(rand.nextInt(4),rand.nextInt(2),rand.nextInt(2)) == false){
//				move(rand.nextInt(2),rand.nextInt(2),rand.nextInt(2));
//				speed++;
//			}
//		}
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
