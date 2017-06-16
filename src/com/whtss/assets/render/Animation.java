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
	
	public int getLength()
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
	
	//These methods get overridden by subclasses to actually render their animations
	/**
	 * The method gets drawn underneath everything
	 * @param The graphics context to render onto
	 * @param The scale on which to draw
	 */
	public void drawUnderEntities(Graphics2D g, int s) {}
	
	/**
	 * The method gets drawn over entities but under UI elements like the tile selection
	 * @param The graphics context to render onto
	 * @param The scale on which to draw
	 */
	public void drawOverEntities(Graphics2D g, int s) {}
	
	/**
	 * The method gets drawn over everything
	 * @param The graphics context to render onto
	 * @param The scale on which to draw
	 */
	public void drawOverGUI(Graphics2D g, int s) {}
}
