package com.whtss.assets.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import com.whtss.assets.Game;
import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.entities.Enemy;
import com.whtss.assets.entities.Player;
import com.whtss.assets.entities.PlayerSniper;

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
		
		GameRenderer.UIInterface grui = game.uiinterface;
		
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
		
		int entity_health= 0;
		Damageable erer = new Player(null, null);
		String potato = "not a player";
		for (Entity e : game.getLevel().getEntities()){
			if (e.isActive() && e instanceof Damageable && grui.getSelectedTile() != null)
			{		
				if(grui.getSelectedTile().equals(e.getLocation())){
					erer = (Damageable) e;
					entity_health = erer.getHealth();
					potato = String.valueOf(entity_health);
		
					g.drawString("Health>>> "+ potato, getWidth() /2 - 150, getHeight() - 40);
				}
			}
		}
		g.setFont(new Font("Sans Serif", Font.BOLD, 20));
		
		int mp = 0;
		int ammo=0;
		Player ere = new Player(null, null);
		for (Entity e : game.getLevel().getEntities()){
			if (e.isActive() && e instanceof Damageable && grui.getSelectedTile() != null && e instanceof Player)
			{		
				if(grui.getSelectedTile().equals(e.getLocation())){
					if(e instanceof PlayerSniper){
					ammo = ((PlayerSniper) e).getammo();
					g.drawString("Ammo>>> "+ ammo,300, getHeight() - 40);
					}
					ere = (Player) e;
					mp = ere.getspeed();
		
					g.drawString("AP>>> "+ mp,150, getHeight() - 40);
				}
			}
		}
		g.setFont(new Font("Sans Serif", Font.BOLD, 20));
		for (Entity e : game.getLevel().getEntities()){
			if (e.isActive() && e instanceof Damageable && grui.getSelectedTile() != null )
			{		
				if(grui.getSelectedTile().equals(e.getLocation())){
					if(e instanceof Player){
					g.drawString("Name>>> "+((Player)e).getname(),getWidth() /2+150,getHeight() - 40);
					}
					if(e instanceof Enemy){
						g.drawString("Name>>> "+((Enemy)e).getname(),getWidth() /2+150,getHeight() - 40);
						}
				}
			}
		}
		g.setFont(new Font("Sans Serif", Font.BOLD, 20));

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
