package com.whtss.assets.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RigidList<T extends Object> implements List<T>
{
	private Object[] list;

	private RigidList(Object[] list)
	{
		this.list = list;
	}

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
		for (Object n : this)
			if (n == null && o == null)
				return true;
			else if (n.equals(o))
				return true;
		return false;
	}

	@Override
	public Iterator<T> iterator()
	{
		return listIterator();
	}

	@Override
	public Object[] toArray()
	{
		return toArray(new Object[size()]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Q> Q[] toArray(Q[] a)
	{
		Q[] _return = a.length >= size() ? a : (Q[]) Array.newInstance(a.getClass(), size());
		for (int i = 0; i < size(); i++)
			_return[i] = (Q) get(i);
		return _return;
	}

	@Override
	public boolean add(T e)
	{
		throw new UnsupportedOperationException("This list is of fixed length");
	}

	@Override
	public boolean remove(Object o)
	{
		if (o == null)
			return false;

		int n = indexOf(o);
		if (n < 0)
			return false;

		set(indexOf(o), null);
		return true;
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
	public boolean addAll(Collection<? extends T> c)
	{
		throw new UnsupportedOperationException("This list is of fixed length");
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
	{
		throw new UnsupportedOperationException("This list is of fixed length");
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		boolean changed = false;
		for (Object o : this)
			changed |= remove(o);
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		boolean changed = false;
		for (Object o : this)
			if (!c.contains(o))
				changed |= remove(o);
		return changed;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < size(); i++)
			remove(i);
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
		throw new UnsupportedOperationException("This list is of fixed length");
	}

	@Override
	public T remove(int index)
	{
		T _return = get(index);
		set(index, null);
		return _return;
	}

	@Override
	public int indexOf(Object o)
	{
		for (int i = 0; i < size(); i++)
			if (get(i).equals(o))
				return i;
		return -1;
	}

	@Override
	public int lastIndexOf(Object o)
	{
		for (int i = size() - 1; i >= 0; i--)
			if (get(i).equals(o))
				return i;
		return -1;
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return new RigidListIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return new RigidListIterator(index);
	}

	@Override
	public RigidList<T> subList(int fromIndex, int toIndex)
	{
		return new SubList(fromIndex, toIndex);
	}

	@Override
	public String toString()
	{
		String s = "[";
		if (size() > 0)
			s += get(0);
		for (int i = 1; i < size(); i++)
			s += ", " + get(i);
		s += "]";

		return s;
	}

	class RigidListIterator implements ListIterator<T>
	{
		private int x = 0;
		private int lastRet = 0;

		public RigidListIterator(int startingIndex)
		{
			x = startingIndex;
		}

		public RigidListIterator()
		{
			this(0);
		}

		@Override
		public boolean hasNext()
		{
			return x < size();
		}

		@Override
		public T next()
		{
			return get(lastRet = x++);
		}

		@Override
		public T previous()
		{
			return get(lastRet = --x);
		}

		@Override
		public boolean hasPrevious()
		{
			return x > 0;
		}

		@Override
		public int nextIndex()
		{
			return x;
		}

		@Override
		public int previousIndex()
		{
			return lastRet;
		}

		@Override
		public void remove()
		{
			RigidList.this.remove(previousIndex());
		}

		@Override
		public void set(T e)
		{
			RigidList.this.set(previousIndex(), e);
		}

		@Override
		public void add(T e)
		{
			RigidList.this.add(e);
		}
	}

	class SubList extends RigidList<T>
	{
		private final int from, to;

		public SubList(int from, int to)
		{
			super(list);
			this.from = from;
			this.to = to;
		}

		@Override
		public T set(int index, T element)
		{
			return super.set(index + from, element);
		}

		@Override
		public T get(int index)
		{
			return super.get(index + from);
		}

		@Override
		public int size()
		{
			return to - from;
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof List<?>))
			return false;
		else
		{
			int i = 0;
			for (Object n : (Iterable<?>) obj)
				if (i > 0)
					return false;
				else if (n == null ? get(i++) != null : !get(i++).equals(n))
					return false;
			return i == size();
		}
	}

	@Override
	public int hashCode()
	{
		int hashcode = 1;
		for (T e : this)
			hashcode = 31 * hashcode + (e == null ? 0 : e.hashCode());
		return hashcode;
	}
}