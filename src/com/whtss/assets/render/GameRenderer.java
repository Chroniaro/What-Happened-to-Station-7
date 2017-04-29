package com.whtss.assets.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import com.whtss.assets.Game;
import com.whtss.assets.HexPoint;
import com.whtss.assets.Level;

public class GameRenderer extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1380847482506652728L;
	
	private Game game;
	private Renderer renderLevel, renderUI;
	
	private final Renderer[] renderers;
	
	private HexPoint mouse;
	
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
		
		addMouseMotionListener(new MouseMotionListener()
		{
			
			@Override
			public void mouseMoved(MouseEvent e)
			{	
				mouse = HexPoint.fromVisual(e.getX() - getWidth() / 2, e.getY() - getHeight() / 2, cellSize());
			}
			
			@Override
			public void mouseDragged(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics _g)
	{
		super.paintComponent(_g);
		Graphics2D g = (Graphics2D) _g;
//		for(Renderer r : renderers)
//			r.draw(g);
		
		g.drawString(String.valueOf(mouse), 0, getHeight());
		
		g.translate(getWidth() / 2, getHeight() / 2);
		
		Level lvl = game.getCurrentLevel();
		
		g.setColor(Color.BLACK);
		HexPoint.iterateRectangle(HexPoint.XY(-lvl.getWidth() / 2 + 1, -lvl.getHeight() / 2), lvl.getWidth(), lvl.getHeight(), (HexPoint hex, int x, int y) -> 
		{
			if(hex.equals(mouse))
			{
				int color = lvl.getValue(x, y);
				g.setColor(new Color(color & 255, (color >> 8) & 255, (color >> 16) & 255));
				g.fill(hex.getBorder(cellSize()));
				g.setColor(Color.BLACK);
			}
			g.draw(hex.getBorder(cellSize()));
		});
	}
	
	private int cellSize()
	{
		final double limiter = Math.min(getWidth() / 1000.0, getHeight() / (1000.0 * .6));
		return (int)Math.round(35 * limiter);
	}
}
