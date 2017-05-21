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
import com.whtss.assets.entities.Enemy;
import com.whtss.assets.entities.HealBox;
import com.whtss.assets.entities.Player;
import com.whtss.assets.entities.PlayerSniper;
import com.whtss.assets.entities.Sniper;
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
						select = fromVisual(e);
				}
				else
					select = null;

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
		tstack.push();

		if (activeAnimation != null)
			activeAnimation.drawUnderEntities(g, s);

		tstack.revert();
		//		for (HexPoint yyz: Entity.getflametiles()){
		//			
		//		g.setColor(Color.ORANGE);
		//		g.fill(yyz.getBorder(s));
		//		}

		for (Entity e : lvl.getEntities())
		{
			//			if(e == null)
			//				continue;
			if (!e.isActive())
				continue;
			
			if(e instanceof PlayerSniper)
			{
				g.setColor(new Color(125, 25, 150));
				g.fill(e.getLocation().getBorder(s));
			}
			else if (e instanceof Player)
			{
//				int y = Math.min(((Player) e).gethealth(), 100);
//				Color myNewP = new Color(255 - y, y / 2, 100 + y);
//				g.setColor(myNewP);
				g.setColor(new Color(155, 50, 200));
				g.fill(e.getLocation().getBorder(s));
			}
			else if (e instanceof Sniper)
			{
				g.setColor(Color.GREEN);
				g.fill(e.getLocation().getBorder(s));
			}
			else if (e instanceof Enemy)
			{
//				int y = Math.min(100, ((Enemy) e).getHealth());
//				int k = (int) (250 - ((Math.pow((100 - y) / 2, 2)) / 10));
//				int u = (int) ((Math.pow((int) (100 - y) / 2, 2)) / 10);
//				int t = Math.abs(u);
//				int z = Math.abs(k);
//				Color myNewBlue = new Color(t, t, z);
//				g.setColor(myNewBlue);
				g.setColor(new Color(0, 0, 250));
				g.fill(e.getLocation().getBorder(s));
			}
			else if (e instanceof HealBox)
			{
//				int y = ((HealBox) e).getHealth();
//				Color myNewBlue = new Color(255 - (y / 2), 0, 255 - (y / 2));
//				g.setColor(myNewBlue);
				g.setColor(new Color(155, 0, 155));
				g.fill(e.getLocation().getBorder(s));
			}
		}

		//		tstack.pop();
		//		tstack.revert();

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
			select = sel;
		}

		public void startAnimation(Animation a)
		{
			playAnimation(a);
		}

		public boolean isInAnimation()
		{
			return activeAnimation != null;
		}
	}
}
