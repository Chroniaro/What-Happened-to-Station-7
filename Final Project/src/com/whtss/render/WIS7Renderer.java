package com.whtss.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import com.whtss.Game;

public class WIS7Renderer extends JComponent
{
	private Game game;
	private Renderer renderLevel, renderUI;
	;
	
	private final Renderer[] renderers;
	
	public WIS7Renderer(Game game)
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
		for(Renderer r : renderers)
			r.draw(g);
	}
}
