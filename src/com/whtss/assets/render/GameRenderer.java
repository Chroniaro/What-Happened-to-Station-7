package com.whtss.assets.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import com.whtss.assets.Game;
import com.whtss.assets.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.hex.HexRect;

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
				mouseMoved(e);
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics _g)
	{
		super.paintComponent(_g);
		Graphics2D g = (Graphics2D) _g;
		
		TStack tstack = new TStack(g);
		
		for(Renderer r : renderers)
			r.draw(g);
		
		tstack.revert();
		
		g.drawString(String.valueOf(mouse), 0, getHeight());
		
		g.translate(getWidth() / 2, getHeight() / 2);
		
		tstack.push();
		
		Level lvl = game.getCurrentLevel();
		
		g.setColor(Color.BLACK);
		int w = lvl.getWidth();
		int h = lvl.getHeight();
		int dw = -w/2;
		int dh = 1 - h;
		
		boolean drawMouse = false;
		
		HexRect viewRect = HexPoint.rect(HexPoint.origin.mXY(dw, dh + (dh + dw)%2), lvl.getWidth(), lvl.getHeight());
		for(HexPoint hex = viewRect.next(); viewRect.hasNext(); hex = viewRect.next())
		{
			tstack.push();
			
			g.drawString(lvl.getValue(viewRect.getRelX(), viewRect.getRelY()), hex.getVisualX(cellSize()), hex.getVisualY(cellSize()));
			
			if(mouse != null)
			{
				g.setColor(new Color(1f / (1 + hex.dist(mouse)), 2f / (2 + hex.dist(mouse)), 3f / (3 + hex.dist(mouse)), 1f / (1 + hex.dist(mouse))));
				if(!hex.equals(mouse))
					g.fill(hex.getBorder(cellSize()));
				else
					drawMouse = true;
			}
			
			tstack.pop();
		}
		
		if(drawMouse)
		{
			g.setColor(new Color(1f, 1f, 1f, .8f));
			g.setStroke(new BasicStroke(5));
			g.draw(mouse.getBorder(cellSize()));
		}
	}
	
	private int cellSize()
	{
		final double limiter = Math.min(getWidth() / 1000.0, getHeight() / (1000.0 * .6));
		return (int)Math.round(35 * limiter);
	}
}
