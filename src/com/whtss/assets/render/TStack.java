package com.whtss.assets.render;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import com.whtss.assets.util.Stack;

public class TStack
{
	private Stack<AffineTransform> tStack;
	private Graphics2D g;
	
	public TStack(Graphics2D g)
	{
		this.g = g;
		push();
	}
	
	public void push()
	{
		tStack.push(g.getTransform());
	}
	
	public void revert()
	{
		g.setTransform(tStack.peek());
	}
}