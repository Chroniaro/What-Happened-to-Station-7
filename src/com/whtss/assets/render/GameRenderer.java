package com.whtss.assets.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import com.whtss.assets.Entity;
import com.whtss.assets.Game;
import com.whtss.assets.Level;
import com.whtss.assets.entities.Player;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.hex.HexRect;

public class GameRenderer extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1380847482506652728L;
	
	private Game game;
	private HexPoint mouse, select = null;
	
	public GameRenderer(Game game)
	{
		this.game = game;
	}
	
	public void addListeners(JFrame container)
	{
		addMouseMotionListener(
				new MouseMotionListener()
				{
					@Override
					public void mouseMoved(MouseEvent e)
					{	
						mouse = fromVisual(e);
					}
					
					@Override
					public void mouseDragged(MouseEvent e)
					{
						mouseMoved(e);
					}
				}
			);
			
		addMouseListener(
				new MouseListener()
				{
					@Override 
					public void mouseClicked(MouseEvent e) 
					{
						if(select == null)
						{
							if(mouseIn())
								select = fromVisual(e);
						}
						else
							select = null;
					}
					
					@Override public void mousePressed(MouseEvent e) {}
					@Override public void mouseReleased(MouseEvent e) {}
					@Override public void mouseEntered(MouseEvent e) {}
					@Override public void mouseExited(MouseEvent e) {}
				}
			);
			
		container.addKeyListener(
					new KeyListener()
					{
						@Override public void keyTyped(KeyEvent e) {}
						
						@Override public void keyPressed(KeyEvent e) {}

						@Override
						public void keyReleased(KeyEvent e)
						{
							select = game.processAction(select, mouseIn() ? mouse : null, e);
						}
					}
				);
	}
	
	private HexPoint fromVisual(MouseEvent e)
	{
		return HexPoint.fromVisual(e.getX() - getWidth() / 2, e.getY() - getHeight() / 2, cellSize());
	}
	
	@Override
	protected void paintComponent(Graphics _g)
	{
		super.paintComponent(_g);
		Graphics2D g = (Graphics2D) _g;
		
		TStack tstack = new TStack(g);
		Level lvl = game.getCurrentLevel();
		HexRect viewRect = lvl.getCells();
		
		g.setColor(Color.black);
		g.fill(g.getClip());
		
		tstack.revert();
		
		final boolean mouseIn = mouseIn();
		if(mouseIn)
			g.setColor(Color.white);
		else
			g.setColor(Color.red);
		g.drawString(String.valueOf(mouse), 0, getHeight());
		
		int floor = game.getfloor();
		g.setColor(Color.BLUE);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		drawStringCenteredly(g, "Floor " + floor, getWidth() / 2, 5);
		
		g.translate(getWidth() / 2, getHeight() / 2);
		
		tstack.push();
		
		g.setColor(Color.red);
		g.fill(lvl.getstart().getBorder(cellSize()));
		
		tstack.revert();
		
		HexRect.Iterator iterator = viewRect.new Iterator();
		for(HexPoint hex = iterator.next(); iterator.hasNext(); hex = iterator.next())
		{
			tstack.push();
			
			g.setColor(Color.WHITE);
			g.fill(hex.getBorder(cellSize()));
//			g.drawString(lvl.getValue(iterator.x(), iterator.y()), hex.getVisualX(cellSize()), hex.getVisualY(cellSize()));
			
			if(mouse != null)
			{
				g.setColor(new Color(1f - 1f / (1 + hex.dist(mouse)), 1f - 4f / (4 + hex.dist(mouse)), 1f - 3f / (3 + hex.dist(mouse))));
				if(!hex.equals(mouse))
					g.fill(hex.getBorder(cellSize()));
			}
			
			tstack.pop();
		}
		
		if(mouseIn)
		{
			g.setColor(new Color(1f, 1f, 1f, .8f));
			g.setStroke(new BasicStroke(5));
			g.draw(mouse.getBorder(cellSize()));
		}
		
		if(select != null)
		{
			g.setColor(new Color(1f, .5f, .5f, .9f));
			g.setStroke(new BasicStroke(7));
			g.draw(select.getBorder(cellSize()));
		}
		
		tstack.revert();
		tstack.push();
		
		for(Entity e : lvl.getEntities())
		{
			if(e.getClass().equals(Player.class))
			{ 
				int y = Player.gethealth();
				Color myNewBlue = new Color (128,y,128);
				g.setColor(myNewBlue);
				g.fill(e.getLocation().getBorder(cellSize()));
			}
		}
	}
	
	private int cellSize()
	{
		final double limiter = Math.min(getWidth() / 1000.0, getHeight() / (1000.0 * .6));
		return (int)Math.round(35 * limiter);
	}
	
	private boolean mouseIn()
	{
		return mouse != null && game.getCurrentLevel().getCells().contains(mouse);
	}
	
	private static void drawStringCenteredly(Graphics2D g, String str, int cx, int cy)
	{
		final FontMetrics m = g.getFontMetrics();
		final int w = m.stringWidth(str);
		final int h = m.getHeight();
		g.drawString(str, cx - w/2, cy + h/2);
	}
}
