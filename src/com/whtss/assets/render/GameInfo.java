package com.whtss.assets.render;

import java.awt.Color;
import java.awt.Font;
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

		g.setFont(new Font("Sans Serif", Font.BOLD, 30));
		int lvl = game.getLevel().getLevelNumber();
		Color lvlTextColor = new Color(Math.max(255 - (lvl * 8), 0), 255, Math.max(255 - (lvl * 8), 0));
		g.setColor(lvlTextColor);
		g.drawString("Floor: " + game.getLevel().getLevelNumber(), getWidth() - 150, getHeight() - 40);

	}

	public void addListeners()
	{
		addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (getNextTurnButton().contains(e.getPoint()))
					game.endPlayerTurn();
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
			}
		});
	}

	private Rectangle getNextTurnButton()
	{
		return nextTurnBut;
	}

	public class UIInterface
	{
		public void refresh()
		{
			repaint();
		}
	}
}
