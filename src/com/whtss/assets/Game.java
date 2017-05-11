package com.whtss.assets;

import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.whtss.assets.entities.Player;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.GameRenderer.UIInterface;
import com.whtss.assets.core.*;
import com.whtss.assets.core.SoundStuff;

public class Game
{
	
	private Level currentLevel;
	private boolean playersTurn = true;
	int floor = 1;
	private UIInterface uiinterface;
	private boolean Game = true;
	
	public void init(UIInterface UIInterface)
	{
		uiinterface = UIInterface;
		currentLevel = new Level(UIInterface);
	}
	
	public Level getCurrentLevel()
	{
		return currentLevel;
	}
	public boolean GameGo()
	{
		int x = 0;
		for(Entity e : getCurrentLevel().getEntities()){
			if(e instanceof Player){
				x++;
			}
		}
		if(x == 0){
		Game = false;	
		}
		return Game;
	}
	
	public int getfloor()
	{
		return floor;
	}
	
	public void nextfloor()
	{
		floor++;
	}
	
	public void endPlayerTurn() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		playersTurn = false;
		getCurrentLevel().nextTurn("Enemy");
		uiinterface.refresh();;
		endEnemyTurn();
	}
	
	public void endEnemyTurn() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{ 
		SoundStuff cam = new SoundStuff();
		playersTurn = true;
		getCurrentLevel().nextTurn("Player");
		uiinterface.refresh();
		if(GameGo() == false){
			cam.swnat();
		}
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