package com.whtss.assets.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RigidList<T extends Object> implements List<T>
{
	private Object[] list;
	
	public RigidList(int size)
	{
		list = new Object[size];
	}
	
	@Override
	public int size()
	{
		return list.length;
	}

	@Override
	public boolean isEmpty()
	{
		return size() > 0;
	}

	@Override
	public boolean contains(Object o)
	{
		for(Object n : list)
			if(n == null && o == null)
				return true;
			else if(n.equals(o))
				return true;
		return false;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
				{
					int x = 0;
					
					@Override
					public boolean hasNext()
					{
						return x < size();
					}

					@SuppressWarnings("unchecked")
					@Override
					public T next()
					{
						return (T) list[x++];
					}
				};
	}

	@Override
	public Object[] toArray()
	{
		return Arrays.copyOf(list, list.length);
	}

	@Override
	public <Q> Q[] toArray(Q[] a)
	{
		return null;
	}

	@Override
	public boolean add(T e)
	{
		throw new UnsupportedOperationException("This list is of fixed length");
	}

	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException("This list is of fixed length");
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
	{
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return false;
	}

	@Override
	public void clear()
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(int index)
	{
		return (T) list[index];
	}

	@Override
	public T set(int index, T element)
	{
		@SuppressWarnings("unchecked")
		T e = (T) list[index];
		list[index] = element;
		return e;
	}

	@Override
	public void add(int index, T element)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public T remove(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(Object o)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int lastIndexOf(Object o)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return null;
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return null;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		return null;
	}
	
	@Override
	public String toString()
	{
		String s = "[";
		if(list.length > 0)
			s += list[0];
		for(int i = 1; i < list.length; i++)
			s += ", " + list[i];
		s += "]";
		
		return s;
	}
}
