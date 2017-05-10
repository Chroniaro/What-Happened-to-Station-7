package com.whtss.assets.render.animations;

import java.awt.Graphics2D;
import java.util.Iterator;
import com.whtss.assets.render.Animation;

public abstract class CompoundAnimation extends Animation implements Iterable<Animation>
{
	private Animation[] animations;
	
	public CompoundAnimation(Animation[] animations, TimeCascade cascade)
	{
		super(totalLength(animations, cascade));
		this.animations = animations;
	}
	
	protected interface TimeCascade
	{
		public int time(int oldTime, int nextTime);
	}

	private static int totalLength(Animation[] animations, TimeCascade cascade)
	{
		int sum = 0;
		for (Animation a : animations)
			sum = cascade.time(sum, a.getLength());
		return sum;
	}
	
	@Override
	public Iterator<Animation> iterator()
	{
		return new Iterator<Animation>()
				{
					int x = 0;
			
					@Override
					public boolean hasNext()
					{
						return x < animations.length;
					}

					@Override
					public Animation next()
					{
						return animations[x++];
					}
			
				};
	}
	
	public static class Concurrent extends CompoundAnimation
	{
		public Concurrent(Animation... animations)
		{
			super(animations, Math::max);
		}

		@Override
		public void resetTimer()
		{
			for (Animation a : this)
				a.resetTimer();
			super.resetTimer();
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

		@Override
		public void resetTimer()
		{
			int t = 0;
			for (Animation a : this)
			{
				a.resetTimer(t);
				t += a.getLength();
			}
			if(t != getLength())
				throw new Error("Times don't match");
			super.resetTimer();
		}

		public Animation getCurrentAnimation()
		{
			for(Animation a : this)
				if(a.hasStarted())
					return a;
			return null;
		}
		
		@Override
		public void drawUnderEntities(Graphics2D g, int s)
		{
			getCurrentAnimation().drawUnderEntities(g, s);
		}

		@Override
		public void drawOverEntities(Graphics2D g, int s)
		{
			getCurrentAnimation().drawOverEntities(g, s);
		}

		@Override
		public void drawOverGUI(Graphics2D g, int s)
		{
			getCurrentAnimation().drawOverGUI(g, s);
		}

	}
}
