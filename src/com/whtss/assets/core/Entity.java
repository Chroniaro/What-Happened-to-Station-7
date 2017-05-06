package com.whtss.assets.core;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.awt.event.KeyEvent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
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
		return methodsWithUIHandle("Key_" + KeyEvent.getKeyText(key.getKeyCode()), 
				(Method m) -> 
				{
					Class<?>[] mparams = m.getParameterTypes();
					Object[] params = new Object[mparams.length];
					for(int i = 0; i < mparams.length; i++)
						if(int.class.isAssignableFrom(mparams[i]))
							params[i] = key.getModifiers();
						else if(HexPoint.class.isAssignableFrom(mparams[i]))
							params[i] = target;
						else
							params[i] = null;
					UIAction action = (UIAction) m.invoke(this, params);
					if(action == null)
						return getLocation();
					else
						return action.selectTile();
				}
			);
	}
	
	public final void endTurn()
	{
		methodsWithUIHandle("Next Turn", (Method m) -> m.invoke(this, new Object[m.getParameterCount()]));
	}
	
	private static interface MethodRunnable<T> { T run(Method m) throws Exception; }
	private <T> T methodsWithUIHandle(String handle, MethodRunnable<T> r)
	{
		T l = null;
		for(Method m : getClass().getDeclaredMethods())
			try
			{
				if(handle.equals(m.getAnnotation(UIEventHandle.class).value()))
				{
					try
					{
						l = r.run(m);
					}
					catch(Exception e) {}
				}
			}
			catch(NullPointerException e) {}
		return l;
	}
	
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
	 * Note that the method must have the parameters (int, HexPoint) and return type UIAction to be run, 
	 * and will be passed the modifier mask for the key press and the location of the mouse, respectively.
	 */
	@Documented
	@Retention(RUNTIME)
	@Target(METHOD)
	protected @interface UIEventHandle
	{
		String value();
	}
}
