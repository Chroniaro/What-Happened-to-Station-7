package com.whtss.assets.core;

public interface Damageable
{
	int getHealth();
	int getMaxHealth();
	void takeDamage(int amount);
}
