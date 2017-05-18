package com.whtss.assets.render;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import com.whtss.assets.util.Stack;

public class TStack
{
	private Stack<gInfo> tStack;
	private Graphics2D g;

	public TStack(Graphics2D g)
	{
		this.g = g;
		tStack = new Stack<>();
		push();
	}

	public void push()
	{
		tStack.push(new gInfo(g));
	}

	public void revert()
	{
		tStack.peek().apply(g);
	}

	public void pop()
	{
		tStack.pop().apply(g);
	}
}

class gInfo
{
	final private AffineTransform transform;
	final private Paint paint;
	final private Color bgrnd;
	final private Stroke stroke;
	final private Font font;
	final private Composite composite;
	final private Shape shape;
	final private RenderingHints hints;

	public gInfo(Graphics2D g)
	{
		transform = g.getTransform();
		paint = g.getPaint();
		stroke = g.getStroke();
		font = g.getFont();
		composite = g.getComposite();
		bgrnd = g.getBackground();
		shape = g.getClip();
		hints = g.getRenderingHints();
	}

	public void apply(Graphics2D g)
	{
		g.setTransform(transform);
		g.setPaint(paint);
		g.setStroke(stroke);
		g.setFont(font);
		g.setComposite(composite);
		g.setBackground(bgrnd);
		g.setClip(shape);
		g.setRenderingHints(hints);
	}
}