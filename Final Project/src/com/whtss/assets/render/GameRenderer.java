package com.whtss.assets.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import com.whtss.assets.Game;
import com.whtss.assets.HexPoint;
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
		
		final double aspectRatio = .6;
		final double limiter = Math.min(getWidth() / 1000.0, getHeight() / (1000.0 * aspectRatio));
		final double cellSize = 35 * limiter;
		
		Level lvl = game.getCurrentLevel();
		
		g.setColor(Color.BLACK);
		g.translate(getWidth() / 2, getHeight() / 2);
		HexPoint.iterateRectangle(HexPoint.XY(-lvl.getWidth() / 2, -lvl.getHeight() / 2), lvl.getWidth() / 2 + 1, lvl.getHeight() / 2 + 1, (HexPoint hex, int x, int y) -> 
		{
			int color = lvl.getValue(x, y);
			g.setColor(new Color(color & 255, (color >> 8) & 255, (color >> 16) & 255));
			g.fill(hex.getBorder(cellSize));
		});
	}
}
