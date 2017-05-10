package com.whtss.assets;

import java.awt.event.KeyEvent;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.GameRenderer.UIInterface;

public class Game
{
	private Level currentLevel;
	private boolean playersTurn = true;
	int floor = 1;
	private UIInterface uiinterface;
	
	public void init(UIInterface UIInterface)
	{
		uiinterface = UIInterface;
		currentLevel = new Level(UIInterface);
	}
	
	public Level getCurrentLevel()
	{
		return currentLevel;
	}
	
	public int getfloor()
	{
		return floor;
	}
	
	public void nextfloor()
	{
		floor++;
	}
	
	public void endPlayerTurn()
	{
		playersTurn = false;
		getCurrentLevel().nextTurn("Enemy");
		uiinterface.refresh();;
		endEnemyTurn();
	}
	
	public void endEnemyTurn()
	{
		playersTurn = true;
		getCurrentLevel().nextTurn("Player");
		uiinterface.refresh();
	}
	
	public void processAction(HexPoint select, HexPoint mouse, KeyEvent key)
	{
		switch(key.getKeyCode())
		{
			default:
				getCurrentLevel().processInput(select, mouse, key, isPlayersTurn() ? "Player" : "Enemy");
		}
	}
	
	public boolean isPlayersTurn()
	{
		return playersTurn;
	}
	
	public boolean isEnemyTurn()
	{
		return !playersTurn;
	}
}