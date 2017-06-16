package com.whtss.assets.core;

import com.whtss.assets.Station7;
import com.whtss.assets.render.UIEventHandle;

/**
 * This interface is to be applied to any entity or thing in the game that can take damage.
 */
public interface Damageable
{
	int getHealth();
	int getMaxHealth();
	void takeDamage(int amount);
	
	/**
	 * Dev button to deal damage to any Damageable.
	 */
	@UIEventHandle(value = "Key_B", turn = "Player")
	public default void takeDamage()
	{
		if(Station7.DEV)
			takeDamage(10);
	}
}
