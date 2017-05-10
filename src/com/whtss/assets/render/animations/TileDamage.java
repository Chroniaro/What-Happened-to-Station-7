package com.whtss.assets.render.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.Animation;

public class TileDamage extends Animation
{
	private HexPoint pos;
	
	public TileDamage(HexPoint location)
	{
		super(500);
		this.pos = location;
	}

	@Override
	public void drawOverEntities(Graphics2D g, int s)
	{
		Shape bound = pos.getBorder(s);
		Rectangle border = bound.getBounds();
		Point2D.Double f = new Point2D.Double(border.getCenterX(), border.getCenterY());
		Paint grad = new RadialGradientPaint(f, s / 3, new float[] {0, .7f, 1}, new Color[]{
				new Color(1, 0, 0, 0), 
				new Color(1, 0, 0, .2f * (getLength() - T()) / getLength()), 
				new Color(1, 0, 0, Math.min(1, .8f * (getLength() - T()) / getLength()))});
		g.setPaint(grad);
		g.fill(bound);
	}
}
