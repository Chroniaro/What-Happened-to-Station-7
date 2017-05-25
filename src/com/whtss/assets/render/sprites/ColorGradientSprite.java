package com.whtss.assets.render.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.render.Sprite;

public class ColorGradientSprite implements Sprite
{
	private Supplier<Color> cp;
	Entity e;
	
	public ColorGradientSprite(Entity e, Supplier<Color> cp)
	{
		this.e = e;
		this.cp = cp;
	}
	
	public ColorGradientSprite(Entity e, Color start, Color end, DoubleSupplier variable)
	{
		this(e, () -> 
		{
			float val = (float) variable.getAsDouble();
			if(val > 1)
				val = 1;
			else if(val < 0)
				val = 0;
			return new Color(
					(1 - val) * start.getRed() / 255 + val * end.getRed() / 255,
					(1 - val) * start.getGreen() / 255 + val * end.getGreen() / 255,
					(1 - val) * start.getBlue() / 255 + val * end.getBlue() / 255);
		});
	}
	
	public <DamageableEntity extends Entity & Damageable> ColorGradientSprite(DamageableEntity e, Color start, Color end)
	{
		this(e, start, end, () -> 1 - (double) e.getHealth() / e.getMaxHealth());
	}
	
	@Override
	public void draw(Graphics2D g, int s)
	{
		g.setColor(cp.get());
		g.fill(e.getLocation().getBorder(s));
	}
}
