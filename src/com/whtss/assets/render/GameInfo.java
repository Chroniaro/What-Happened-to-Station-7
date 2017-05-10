package com.whtss.assets.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.whtss.assets.Game;

public class GameInfo extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3940045383436910526L;
	
	private Game game;
	private static final Rectangle nextTurnBut = new Rectangle(10, 10, 50, 50);
	
	public GameInfo(Game game)
	{
		this.game = game;
	}

	@Override
	protected void paintComponent(Graphics _g)
	{
		super.paintComponent(_g);
		Graphics2D g = (Graphics2D) _g;
		g.setColor(Color.BLACK);
		g.fill(g.getClip());
		g.setColor(Color.GREEN);
		g.fill(getNextTurnButton());
		
		
		
	}
	
	public void addListeners(JFrame container)
	{
		addMouseListener(
				new MouseListener()
				{
					@Override public void mouseClicked(MouseEvent e) 
					{
						if(getNextTurnButton().contains(e.getPoint()))
							game.endPlayerTurn();
					}
					
					@Override public void mousePressed(MouseEvent e) {}
					@Override public void mouseReleased(MouseEvent e) {}
					@Override public void mouseEntered(MouseEvent e) {}
					@Override public void mouseExited(MouseEvent e) {}
				}
			);
	}
	
	private Rectangle getNextTurnButton()
	{
		return nextTurnBut;
	}
}
