package com.whtss.assets.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomIterator<T> implements Iterator<T>
{
	final private List<T> array;
	private int index = 0;

	public RandomIterator(Collection<T> c)
	{
		array = new ArrayList<T>(c.size());
		array.addAll(c);
		Collections.shuffle(array);
	}

	@Override
	public boolean hasNext()
	{
		return index < array.size();
	}

	@Override
	public T next()
	{
		return array.get(index++);
	}

}
