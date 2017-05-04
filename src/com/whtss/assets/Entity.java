package com.whtss.assets;

import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.whtss.assets.hex.HexPoint;

public class Entity implements LightSource
{
	private HexPoint location;
	private final Level lvl;
	protected final UIAction success = new UIAction() 
	{
		public HexPoint selectTile() 
		{
			return null;
		};
	};
	
	public Entity(HexPoint location, Level level)
	{
		this.location = location;
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
	
	public HexPoint getLocation()
	{
		return location;
	}

	public void setLocation(HexPoint location)
	{
		if(lvl.getCells().contains(location))
			this.location = location;
	}

	protected abstract class UIAction
	{
		public HexPoint selectTile()
		{
			return location;
		}
	}
}
