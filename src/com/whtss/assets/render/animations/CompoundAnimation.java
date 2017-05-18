package com.whtss.assets.render.animations;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.whtss.assets.render.Animation;

public abstract class CompoundAnimation extends Animation implements Collection<Animation>
{
	private List<Animation> animations;

	public CompoundAnimation(Animation[] animations, TimeCascade cascade)
	{
		super(totalLength(animations, cascade));
		this.animations = new LinkedList<>();
		for (Animation a : animations)
			this.animations.add(a);
	}
	
	private static int totalLength(Animation[] animations, TimeCascade cascade)
	{
		int sum = 0;
		for (Animation a : animations)
			sum = cascade.time(sum, a.getLength());
		return sum;
	}
	
	@Override
	public void resetTimer()
	{
		super.resetTimer();
	}

	protected interface TimeCascade
	{
		public int time(int oldTime, int nextTime);
	}

	@Override
	public Iterator<Animation> iterator()
	{
		return animations.iterator();
	}

	@Override
	public boolean add(Animation e)
	{
		return animations.add(e);
	}

	@Override
	public boolean remove(Object o)
	{
		return animations.remove(o);
	}

	@Override
	public boolean addAll(Collection<? extends Animation> c)
	{
		return animations.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return animations.removeAll(c);
	}

	@Override
	public void clear()
	{
		animations.clear();
	}

	@Override
	public boolean contains(Object o)
	{
		return animations.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return animations.containsAll(c);
	}

	@Override
	public boolean isEmpty()
	{
		return animations.isEmpty();
	}
	
	@Override
	public boolean retainAll(Collection<?> c)
	{
		return animations.retainAll(c);
	}
	
	@Override
	public int size()
	{
		return animations.size();
	}
	
	@Override
	public Object[] toArray()
	{
		return animations.toArray();
	}
	
	@Override
	public <T> T[] toArray(T[] a)
	{
		return animations.toArray(a);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof CompoundAnimation)
			return animations.equals((CompoundAnimation) obj);
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return animations.hashCode();
	}

	public static class Concurrent extends CompoundAnimation
	{
		public Concurrent(Animation... animations)
		{
			super(animations, Math::max);
		}
		
		public Concurrent(Collection<Animation> animations)
		{
			this(animations.toArray(new Animation[animations.size()]));
		}

		@Override
		public void resetTimer()
		{
			super.resetTimer();
			for (Animation a : this)
				a.resetTimer();
		}

		@Override
		public void drawUnderEntities(Graphics2D g, int s)
		{
			for (Animation a : this)
				if (a.stillGoing())
					a.drawUnderEntities(g, s);
		}

		@Override
		public void drawOverEntities(Graphics2D g, int s)
		{
			for (Animation a : this)
				if (a.stillGoing())
					a.drawOverEntities(g, s);
		}

		@Override
		public void drawOverGUI(Graphics2D g, int s)
		{
			for (Animation a : this)
				if (a.stillGoing())
					a.drawOverGUI(g, s);
		}

	}

	public static class Sequential extends CompoundAnimation
	{
		public Sequential(Animation... animations)
		{
			super(animations, (int a, int b) -> a + b);
		}
		
		public Sequential(Collection<Animation> animations)
		{
			this(animations.toArray(new Animation[animations.size()]));
		}

		@Override
		public void resetTimer()
		{
			super.resetTimer();

			int t = 0;
			for (Animation a : this)
			{
				a.resetTimer(t);
				t += a.getLength();
			}
			if (t != getLength())
				throw new Error("Times don't match");
		}

		public Animation getCurrentAnimation()
		{
			for (Animation a : this)
				if (a.stillGoing())
					return a;
			return null;
		}

		@Override
		public void drawUnderEntities(Graphics2D g, int s)
		{
			Animation c = getCurrentAnimation();
			if (c != null)
				c.drawUnderEntities(g, s);
		}

		@Override
		public void drawOverEntities(Graphics2D g, int s)
		{
			Animation c = getCurrentAnimation();
			if (c != null)
				c.drawOverEntities(g, s);
		}

		@Override
		public void drawOverGUI(Graphics2D g, int s)
		{
			Animation c = getCurrentAnimation();
			if (c != null)
				c.drawOverGUI(g, s);
		}
	}
}
