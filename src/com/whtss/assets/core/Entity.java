package com.whtss.assets.core;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.stream.Stream;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.UIEventHandle;

/**
 * Any entity in the level.
 */
public abstract class Entity
{
	private HexPoint location;
	private boolean active = true;
	private final Level lvl;
	String name;

	public Entity(HexPoint location, Level level)
	{
		setLocation(location);
		lvl = level;
	}
	
	/**
	 * Chooses a random name for the entity from a file.
	 * 
	 * @param names The file containing the potential name choices
	 * @param numOfNames The number of names in the file
	 */
	public void setName(File names, int numOfNames)
	{
		Stream<String> lines = null;
		try
		{
			lines = Files.lines(names.toPath());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		name = lines.skip((int) (Math.random() * numOfNames)).findFirst().get();
	}
	
	public String getName()
	{
		return name;
	}

	public int light()
	{
		return 0;
	}
	
	/**
	 * Generic method for handling key input. All key input is sent to this method, and subclasses of Entity can mark
	 * methods with @UIEventHandle to have them get automatically called and receive input.
	 * 
	 * @param key The KeyEvent object generated when the key was pressed
	 * @param target The tile that the mouse is hovering over when the key was pressed
	 * @param turn Whose turn it is (enemy or player)
	 */
	public final void input(KeyEvent key, HexPoint target, String turn)
	{
		methodsWithUIHandle("Key_" + KeyEvent.getKeyText(key.getKeyCode()), turn, (Method m) ->
		{
			//Get the parameters for the methods and fill them with appropriate data
			
			Class<?>[] mparams = m.getParameterTypes();
			Object[] params = new Object[mparams.length];
			for (int i = 0; i < mparams.length; i++)
				if (int.class.isAssignableFrom(mparams[i])) //Integers are supplied with the key press modifiers
					params[i] = key.getModifiers();
				else if (HexPoint.class.isAssignableFrom(mparams[i])) //HexPoints are supplied with the location that the mouse is hovering over
					params[i] = target;
				else if (Entity.class.isAssignableFrom(mparams[i])) //Entities are supplied with the Entity in the cell that the mouse is hovering over
				{
					params[i] = null;
					for (Entity e : lvl.getEntities())
						if (e.getLocation().equals(target))
						{
							params[i] = e;
							break;
						}
				}
				else
					params[i] = null;
			return m.invoke(this, params);
		});
	}

	public final void doTurn(String turn)
	{
		methodsWithUIHandle("Next Turn", turn, (Method m) -> m.invoke(this, new Object[m.getParameterCount()]));
	}

	private static interface MethodRunnable<T>
	{
		T run(Method m) throws ReflectiveOperationException;
	}

	/**
	 * Calls every method on THIS INSTANCE that fits the parameters. Exists to add more generic behavior of subclasses.
	 * 
	 * @param handle The specific event that they wish to receive, i.e. "next turn" or "Key_Space"
	 * @param turn The turn that they wish to receive this event for, either player or enemy
	 * @param r A lambda for code to run involving that method
	 * @return The value returned my the last method run
	 */
	private final <T> T methodsWithUIHandle(String handle, String turn, MethodRunnable<T> r)
	{
		T l = null;
		for (Method m : getClass().getMethods())
			try
			{
				UIEventHandle eHandle = m.getAnnotation(UIEventHandle.class);
				if (eHandle.value().equals(handle) && eHandle.turn().matches(turn))
					l = r.run(m);
			}
			catch (ReflectiveOperationException | NullPointerException e) //These exceptions come up during normal execution and should just be ignored
			{
			}
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

	/**
	 * Moves to the given tile if possible
	 * 
	 * @param da The relative A coordinate of the desired tile
	 * @param db The relative B coordinate of the desired tile
	 * @param dhy The relative Y coordinate of the desired tile
	 * @return Whether it successfully moved to the new tile
	 */
	public boolean move(int da, int db, int dhy)
	{
		HexPoint newLocation = location.mABY(da, db, 2 * dhy);
		if (lvl.getCells().contains(newLocation) && lvl.getFloorTile(newLocation) % 2 == 0)
		{
			for (Entity e : lvl.getEntities())
				if (e.isActive() && e.location.equals(newLocation))
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
}
