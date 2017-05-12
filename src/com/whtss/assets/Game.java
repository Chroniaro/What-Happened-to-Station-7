package com.whtss.assets;

import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.GameRenderer.UIInterface;

public class Game
{
	
	private Level lvl;
	private boolean playersTurn = true;
	private UIInterface uiinterface;
	
	public void init(UIInterface UIInterface)
	{
		uiinterface = UIInterface;
		lvl = new Level(UIInterface);
	}
	
	public Level getCurrentLevel()
	{
		return lvl;
	}
//	public boolean GameGo()
//	{
//		int x = 0;
//		for(Entity e : getCurrentLevel().getEntities()){
//			if(e instanceof Player && e.isActive()){
//				x++;
//			}
//		}
//		if(x == 0){
//		Game = false;	
//		}
//		return Game;
//	}
	
	public void endPlayerTurn() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		playersTurn = false;
		getCurrentLevel().nextTurn("Enemy");
		uiinterface.refresh();
		endEnemyTurn();
	}
	
	public void endEnemyTurn() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{ 
//		SoundStuff cam = new SoundStuff();
		playersTurn = true;
		getCurrentLevel().nextTurn("Player");
		uiinterface.refresh();
//		if(GameGo() == false){
//			cam.dbc();
//			cam.swnat();
//		}
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