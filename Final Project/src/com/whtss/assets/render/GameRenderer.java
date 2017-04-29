package com.whtss.assets.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import com.whtss.assets.Game;
import com.whtss.assets.Level;

public class GameRenderer extends JComponent
{
	private Game game;
	private Renderer renderLevel, renderUI;
	
	private final Renderer[] renderers;
	
	public GameRenderer(Game game)
	{
		this.game = game;
		
		renderLevel = new LevelRenderer();
		renderUI = new UIRenderer(game);
		
		renderers = new Renderer[]
				{
						renderLevel,
						renderUI
				};
	}
	
	@Override
	protected void paintComponent(Graphics _g)
	{
		super.paintComponent(_g);
		Graphics2D g = (Graphics2D) _g;
//		for(Renderer r : renderers)
//			r.draw(g);
		Level lev = game.getCurrentLevel();
		for(int x = 0; x < lev.getWidth(); x++)
			for(int y = 0; y < lev.getHeight(); y++)
			{
			}
	}
}
