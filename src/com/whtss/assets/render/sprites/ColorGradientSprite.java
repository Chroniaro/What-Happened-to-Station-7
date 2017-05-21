package com.whtss.assets.render.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import com.whtss.assets.core.Entity;
import com.whtss.assets.render.Sprite;

public class ColorGradientSprite implements Sprite
{
	private ColorPicker cp;
	private Entity e;
	
	public static interface ColorPicker
	{
		Color getColor();
	}
	
	public ColorGradientSprite(Entity e, ColorPicker cp)
	{
		this.e = e;
		this.cp = cp;
	}
	
	@Override
	public void draw(Graphics2D g, int s)
	{
		g.setColor(cp.getColor());
		g.fill(e.getLocation().getBorder(s));
	}
}
