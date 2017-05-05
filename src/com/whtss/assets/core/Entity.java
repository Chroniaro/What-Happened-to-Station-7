package com.whtss.assets.core;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.awt.event.KeyEvent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.whtss.assets.LightSource;
import com.whtss.assets.hex.HexPoint;

public abstract class Entity implements LightSource
{
	private HexPoint location;
	private boolean active = true;
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
		setLocation(location);
		lvl = level;
	}
	
	public int light() { return 0; }
	
	public final HexPoint input(KeyEvent key, HexPoint target)
	{
		final String k = KeyEvent.getKeyText(key.getKeyCode());
		for(Method m : getClass().getDeclaredMethods())
			try
			{
				if(k.equals(m.getAnnotation(UIEventHandle.class).value()))
				{
					UIAction action = (UIAction) m.invoke(this, key.getModifiers(), target);
					if(action == null)
						return getLocation();
					else
						return action.selectTile();
				}
			}
			catch(NullPointerException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
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
		protected HexPoint selectTile() { return location; }
	}
	
	/**
	 * Use this annotation to make this event respond to key presses when this entity is selected.
	 * Note that the method must have the parameters (int, HexPoint) to be run, and will be passed 
	 * the modifier mask for the key press and the location of the mouse, respectively.
	 */
	@Documented
	@Retention(RUNTIME)
	@Target(METHOD)
	protected @interface UIEventHandle
	{
		String value();
	}
}
