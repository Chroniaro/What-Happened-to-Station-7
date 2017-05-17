package com.whtss.assets.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class RigidCollection<E> implements Collection<E>
{
	private final Object[] array;

	@SafeVarargs
	public RigidCollection(E... things)
	{
		array = things;
	}

	@Override
	public int size()
	{
		return array.length;
	}

	@Override
	public boolean isEmpty()
	{
		return size() > 0;
	}

	@Override
	public boolean contains(Object o)
	{
		for (Object n : this)
			if ((n == null && o == null) || n.equals(o))
				return true;
		return false;
	}

	@Override
	public Iterator<E> iterator()
	{
		return new Iterator<E>()
		{
			int index = 0;

			@Override
			public boolean hasNext()
			{
				return index < array.length;
			}

			@SuppressWarnings("unchecked")
			@Override
			public E next()
			{
				return (E) array[index++];
			}

		};
	}

	@Override
	public Object[] toArray()
	{
		return Arrays.copyOf(array, array.length);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a)
	{
		T[] _return = a.length >= array.length ? a : (T[]) Array.newInstance(a.getClass(), array.length);
		for (int i = 0; i < size(); i++)
			_return[i] = (T) array[i];
		return _return;
	}

	@Override
	public boolean add(E e)
	{
		throw new UnsupportedOperationException("This collection is immutable.");
	}

	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException("This collection is immutable.");
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		for (Object o : c)
			if (!contains(o))
				return false;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		throw new UnsupportedOperationException("This collection is immutable.");
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("This collection is immutable.");
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("This collection is immutable.");
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException("This collection is immutable.");
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Collection<?>))
			return false;
		
		Collection<?> c = (Collection<?>) obj;
		for(Object o : c)
			if(!contains(o))
				return false;
		for(Object o : this)
			if(!c.contains(o))
				return false;
		
		return true;
	}
	
	@Override
	public int hashCode()
	{
		int hashcode = 1;
		for(E e : this)
			hashcode ^= e.hashCode();
		return hashcode;
	}
	
	@Override
	public String toString()
	{
		String s = "[";
		if(size() > 0)
			s += array[0];
		for(int i = 1; i < size(); i++)
			s += ", " + array[i];
		s += "]";
		
		return s;
	}
}
