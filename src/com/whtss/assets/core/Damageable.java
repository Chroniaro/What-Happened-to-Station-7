package com.whtss.assets.core;

import com.whtss.assets.render.UIEventHandle;

public interface Damageable
{
	int getHealth();
	int getMaxHealth();
	void takeDamage(int amount);
	
	@UIEventHandle(value = "Key_B", turn = "Player")
	public default void takeDamage()
	{
		takeDamage(10);
	}
}
