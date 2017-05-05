package com.whtss.assets;

import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.whtss.assets.hex.HexPoint;

public abstract class Entity implements LightSource
{
	private HexPoint location;
	private boolean active = true;
	protected final Level lvl;
	protected final UIAction success = new UIAction() 
	{
		public HexPoint selectTile() 
		{
			return null;
		};
	};
	
	public Entity(HexPoint location, Level level)
	{
		setLocation(location);
		lvl = level;
	}
	
	public int light() { return 0; }
	
	final HexPoint input(KeyEvent key, HexPoint target)
	{	
		String meth = "_" + KeyEvent.getKeyText(key.getKeyCode());
		Method m;
		try
		{
			m = getClass().getMethod(meth, int.class, HexPoint.class);
			if(m != null && UIAction.class.equals(m.getReturnType()))
			{
				UIAction action = (UIAction) m.invoke(this, key.getModifiers(), target);
				if(action == null)
					return getLocation();
				else
					return action.selectTile();
			}
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
		
		return getLocation();
	}
	
	public abstract void endTurn();
	
	public HexPoint getLocation()
	{
		return location;
	}

	private void setLocation(HexPoint location)
	{
		this.location = location;
	}
	
	public boolean move(int da, int db, int dhy)
	{
		HexPoint newLocation = location.mABY(da, db, 2 * dhy);
		if(lvl.getCells().contains(newLocation) && lvl.getFloorTile(newLocation) % 2 == 0)
		{
			for(Entity e : lvl.getEntities())
				if(e.isActive() && e.location.equals(newLocation))
					return false;
			setLocation(newLocation);
			return true;
		}
		return false;
	}
	
	protected Level getLevel()
	{
		return lvl;
	}

	public boolean isActive()
	{
		return active;
	}

	protected void setActive(boolean active)
	{
		this.active = active;
	}

	public abstract class UIAction
	{
		public HexPoint selectTile()
		{
			return location;
		}
	}
}
