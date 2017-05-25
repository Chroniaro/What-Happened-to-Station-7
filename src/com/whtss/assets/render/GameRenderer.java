package com.whtss.assets.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import com.whtss.assets.Game;
import com.whtss.assets.core.Entity;
import com.whtss.assets.core.Level;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.hex.HexRect;

public class GameRenderer extends JComponent
{

	public static final boolean dev = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1380847482506652728L;

	private Game game;
	private HexPoint mouse, select = null;
	private Animation activeAnimation = null;
	private GameInfo.UIInterface giinterface;

	public GameRenderer(Game game)
	{
		this.game = game;
	}

	public void addListeners(JFrame container)
	{
		addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				mouse = fromVisual(e);
				repaint();
			}

			@Override
			public void mouseDragged(MouseEvent e)
			{
				mouseMoved(e);
			}
		});

		addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (select == null)
				{
					if (mouseIn())
						select(fromVisual(e));
				}
				else
					select(null);

				repaint();
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

		container.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (activeAnimation != null)
					return;

				game.processAction(select, mouseIn() ? mouse : null, e);

				repaint();
			}
		});
	}
	
	public void select(HexPoint p)
	{
		select = p;
		if(giinterface != null)
			giinterface.refresh();
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

		final int s = cellSize();

		Level lvl = game.getLevel();
		if (lvl == null)
			return;
		TStack tstack = new TStack(g);
		HexRect viewRect = lvl.getCells();

		g.setColor(Color.black);
		g.fill(g.getClip());

		tstack.revert();

		final boolean mouseIn = mouseIn();

		//Dev info
		if (mouseIn)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.RED);
		g.drawString(String.valueOf(mouse), 0, getHeight());
		if (mouse != null)
		{
			g.drawString(mouse.abCoords(), 0, getHeight() - 20);
			g.drawString("(" + viewRect.X(mouse) + ", " + viewRect.Y(mouse) + ")", 0, getHeight() - 40);
			if (mouseIn)
				g.drawString("Room " + lvl.getRoom(mouse), 0, getHeight() - 60);
		}

		g.translate(getWidth() / 2, getHeight() / 2);

		tstack.push();

		HexRect.Iterator iterator = viewRect.new Iterator();
		for (HexPoint hex : iterator)
		{
			tstack.push();

			int d;
			if (game.getLevel().getFloorTile(iterator.x(), iterator.y()) % 2 != 0)
				g.setColor(Color.GRAY);
			else if (dev)
			{
				final Color[] colors = { Color.LIGHT_GRAY, Color.WHITE, Color.red, Color.GREEN, Color.BLUE, Color.orange, Color.pink, Color.CYAN, Color.MAGENTA };
				g.setColor(colors[(lvl.getRoom(hex) % (colors.length - 2)) + 2]);
			}
			else if (mouseIn && (d = hex.dist(mouse)) < 25)
			{
				d *= d;
				g.setColor(new Color(1f - 3f / (12 + d), 1f - 3f / (12 + d * d), 1f - 1f / (4 + d)));
			}
			else
			{
				g.setColor(Color.WHITE);
			}

			g.fill(hex.getBorder(s));

			tstack.pop();
		}

		if (lvl.getEnd() != null)
		{
			g.setColor(Color.CYAN);
			g.fill(lvl.getEnd().getBorder(s));
		}

		tstack.revert();

		if (activeAnimation != null)
			activeAnimation.drawUnderEntities(g, s);

		tstack.revert();

		for (Entity e : lvl.getEntities())
		{
			if (!e.isActive())
				continue;
			if(e instanceof Renderable)
				((Renderable)e).getSprite().draw(g, s);
			
		}

		tstack.revert();

		if (activeAnimation != null)
			activeAnimation.drawOverEntities(g, s);

		tstack.revert();

		if (mouseIn)
		{
			g.setColor(new Color(1f, 1f, 1f));
			g.setStroke(new BasicStroke(5));
			g.draw(mouse.getBorder(s));
		}

		if (select != null)
		{
			g.setColor(new Color(1f, .5f, .5f));
			g.setStroke(new BasicStroke(7));
			g.draw(select.getBorder(s));
		}

		if (activeAnimation != null)
			activeAnimation.drawOverGUI(g, s);
	}

	private int cellSize()
	{
		final double limiter = Math.min(getWidth() / 1000.0, getHeight() / (1000.0 * .55));
		return (int) Math.round(30 * limiter);
	}

	private boolean mouseIn()
	{
		return game.getLevel().getCells().contains(mouse);
	}

	public void playAnimation(Animation a)
	{
		if (a == null)
			return;

		activeAnimation = a;
		a.resetTimer();
		new Thread(() ->
		{
			while (a.stillGoing())
			{
				repaint();
				try
				{
					Thread.sleep(20);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			activeAnimation = null;
			repaint();
		}).start();
	}

	public class UIInterface
	{
		public void refresh()
		{
			repaint();
		}

		public void selectTile(HexPoint sel)
		{
			select(sel);
		}
		
		public HexPoint getSelectedTile()
		{
			return select;
		}

		public void startAnimation(Animation a)
		{
			playAnimation(a);
		}

		public boolean isInAnimation()
		{
			return activeAnimation != null;
		}
		
		public void updateInfoInterface()
		{
			giinterface = game.infouiinterface;
		}
	}
}
