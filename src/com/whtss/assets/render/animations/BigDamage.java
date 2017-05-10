package com.whtss.assets.render.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import com.whtss.assets.render.Animation;

public class BigDamage extends Animation
{
	public BigDamage()
	{
		super(1000);
	}

	@Override
	public void drawOverGUI(Graphics2D g, int s)
	{
		g.setColor(new Color(1f, 0f, 0f, 0.7f * (getLength() - T()) / getLength()));
		g.fill(g.getClip());
	}
}
