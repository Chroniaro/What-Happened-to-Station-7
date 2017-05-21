package com.whtss.assets.render;

import java.awt.Graphics2D;

public abstract class Animation
{	
	final private int length;
	private long startTime;
	
	public Animation(int length)
	{
		this.length = length;
	}
	
	public void resetTimer()
	{
		startTime = System.currentTimeMillis();
	}
	
	public void resetTimer(int offset)
	{
		resetTimer();
		startTime += offset;
	}
	
	public final int getLength()
	{
		return length;
	}
	
	public final boolean stillGoing()
	{
		return T() < length;
	}
	
	public final boolean hasStarted()
	{
		return T() > 0;
	}
	
	protected final long T()
	{
		return System.currentTimeMillis() - startTime;
	}
	
	public void drawUnderEntities(Graphics2D g, int s) {}
	public void drawOverEntities(Graphics2D g, int s) {}
	public void drawOverGUI(Graphics2D g, int s) {}
}
