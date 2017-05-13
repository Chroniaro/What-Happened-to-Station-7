package com.whtss.assets;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;

import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.GameInfo;
import com.whtss.assets.render.GameRenderer;
import com.whtss.assets.render.SoundStuff;
import com.whtss.assets.render.GameRenderer.UIInterface;

public class Game
{
	
	private Level lvl;
	private boolean playersTurn = true;
	private UIInterface uiinterface;
	public static List<HexPoint> flametiles;
	private HexPoint locatione;
	
	public void init(GameRenderer.UIInterface gameInterface, GameInfo.UIInterface infoInterface)
	{
		uiinterface = gameInterface;
		lvl = new Level(gameInterface, infoInterface);
	}
	
	public Level getLevel()
	{
		return lvl;
	}

	public static void addflametile(HexPoint tile) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		SoundStuff cam = new SoundStuff();
		cam.flame();
		flametiles.add(tile);
	}
	public int flameer(int da, int db, int dhy) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		// one means theire is a thing there
		// two means that is no one there 
		// zero means that their is a wall there
		
		HexPoint newLocation = locatione.mABY(da, db, 2 * dhy);
		flametiles.add(newLocation);
		SoundStuff cam = new SoundStuff();
		cam.flame();
		if(lvl.getCells().contains(newLocation) && lvl.getFloorTile(newLocation) % 2 == 0)
		{
			for(Entity e : lvl.getEntities())
				if(e.isActive() && e.getLocation().equals(newLocation))
					
					return 1;
			return 2;
		}
		return 0;
	}
	
	public void endPlayerTurn() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		playersTurn = false;
		getLevel().nextTurn("Enemy");
		uiinterface.refresh();
		endEnemyTurn();
}
	public void endEnemyTurn() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{ 
		playersTurn = true;
		getLevel().nextTurn("Player");
		uiinterface.refresh();
	}
	
	public void processAction(HexPoint select, HexPoint mouse, KeyEvent key)
	{
		switch(key.getKeyCode()) //Lets game have buttons to pause and stuff that don't get passed down to the entities
		{
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
		return !playersTurn;
	}
}