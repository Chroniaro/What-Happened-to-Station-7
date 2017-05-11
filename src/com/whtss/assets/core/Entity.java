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
	
	public Entity(HexPoint location, Level level)
	{
		setLocation(location);
		lvl = level;
	}
	
	public int light() { return 0; }
	
	public final void input(KeyEvent key, HexPoint target, String turn)
	{
		methodsWithUIHandle("Key_" + KeyEvent.getKeyText(key.getKeyCode()), turn,
				(Method m) -> 
				{
					Class<?>[] mparams = m.getParameterTypes();
					Object[] params = new Object[mparams.length];
					for(int i = 0; i < mparams.length; i++)
						if(int.class.isAssignableFrom(mparams[i]))
							params[i] = key.getModifiers();
						else if(HexPoint.class.isAssignableFrom(mparams[i]))
							params[i] = target;
						else if(Entity.class.isAssignableFrom(mparams[i]))
						{
							params[i] = null;
							for(Entity e : lvl.getEntities())
								if(e.getLocation().equals(target))
								{
									params[i] = e;
									break;
								}
						}
						else
							params[i] = null;
					return m.invoke(this, params);
				}
			);
	}
	
	public final void doTurn(String turn)
	{
		methodsWithUIHandle("Next Turn", turn, (Method m) -> m.invoke(this, new Object[m.getParameterCount()]));
	}
	
	private static interface MethodRunnable<T> { T run(Method m) throws Exception; }
	private <T> T methodsWithUIHandle(String handle, String turn, MethodRunnable<T> r)
	{
		T l = null;
		for(Method m : getClass().getDeclaredMethods())
			try
			{
				UIEventHandle eHandle = m.getAnnotation(UIEventHandle.class);
				if(eHandle.value().equals(handle) && eHandle.turn().matches(turn))
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

	void setLocation(HexPoint location)
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
	
	@Documented
	@Retention(RUNTIME)
	@Target(METHOD)
	protected @interface UIEventHandle
	{
		String value();
		String turn() default "";
		int priority() default 0;
	}
}
