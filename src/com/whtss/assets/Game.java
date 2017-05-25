package com.whtss.assets;

import java.awt.event.KeyEvent;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.GameInfo;
import com.whtss.assets.render.GameRenderer;
import com.whtss.assets.render.GameRenderer.UIInterface;

public class Game
{
	private Level lvl;
	private boolean playersTurn = true;
	public UIInterface uiinterface;
	public GameInfo.UIInterface infouiinterface;

	public void init(GameRenderer.UIInterface gameInterface, GameInfo.UIInterface infoInterface)
	{
		uiinterface = gameInterface;
		infouiinterface = infoInterface;
		uiinterface.updateInfoInterface();
		lvl = new Level(gameInterface, infoInterface);
	}

	public Level getLevel()
	{
		return lvl;
	}

	public void endPlayerTurn()
	{
		playersTurn = false;
		getLevel().nextTurn("Enemy");
		uiinterface.refresh();
		endEnemyTurn();
	}

	public void endEnemyTurn()
	{
		playersTurn = true;
		getLevel().nextTurn("Player");
		uiinterface.refresh();
	}

	public void processAction(HexPoint select, HexPoint mouse, KeyEvent key)
	{
		switch (key.getKeyCode()) //Lets game have buttons to pause and stuff in the future that don't get passed down to the entities
		{ //We don't have anything like that yet so there aren't any cases here
			default:
				getLevel().processInput(select, mouse, key, isPlayersTurn() ? "Player" : "Enemy");
		}
	}

	public boolean isPlayersTurn()
	{
		return playersTurn;
	}

	public boolean isEnemyTurn()
	{
		return !isPlayersTurn();
	}
}