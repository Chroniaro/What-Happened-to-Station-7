package com.whtss.assets.render.animations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import com.whtss.assets.hex.HexPoint;
import com.whtss.assets.render.Animation;

public class Laser extends Animation
{
	private HexPoint start, end;

	public Laser(HexPoint start, HexPoint end)
	{
		super(200);
		this.start = start;
		this.end = end;
	}

	@Override
	public void drawOverGUI(Graphics2D g, int s)
	{
		g.setColor(new Color(1, 0, 0, .7f));
		g.setStroke(new BasicStroke(10f, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND));

		double progress = 2 * (double) T() / getLength();
		Point st, en;
		if (progress < 1)
		{
			st = new Point(start.getVisualX(s), start.getVisualY(s));
			en = new Point();
			en.x = (int) ((1 - progress) * st.x + progress * end.getVisualX(s));
			en.y = (int) ((1 - progress) * st.y + progress * end.getVisualY(s));
		}
		else
		{
			progress -= 1;
			en = new Point(end.getVisualX(s), end.getVisualY(s));
			st = new Point();
			st.x = (int) (progress * en.x + (1 - progress) * start.getVisualX(s));
			st.y = (int) (progress * en.y + (1 - progress) * start.getVisualY(s));
		}
		g.drawLine(st.x, st.y, en.x, en.y);
	}
	
	@Override
	public int hashCode()
	{
		return start.hashCode() ^ ~end.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Laser && ((Laser) obj).start.equals(start) && ((Laser) obj).end.equals(end);
	}
}
