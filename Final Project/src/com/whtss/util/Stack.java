package com.whtss.util;

import java.util.Collection;
import java.util.Iterator;

public class Stack <T> implements Collection<T>, Iterator<T>
{
	private Node<T> head = null;
	private int length = 0;
	
	public void push(T value)
	{
		Node<T> p = head;
		head = new Node<T>(value);
		head.next = p;
		length ++;
	}
	
	public T pop()
	{
		Node<T> p = head;
		head = head.next;
		length --;
		return p.getValue();
	}
	
	public T peek()
	{
		return head.getValue();
	}
	
	public boolean hasNext()
	{
		return head != null;
	}
	
	public int size() { return length; }
	
	private Node<T> _get(int index)
	{
		Node<T> n = head;
		for(int i = 0; i < index; i++)
			n = n.next;
		return n;
	}
	
	public T get(int index) { return _get(index).getValue(); }
	
	public void insert(int index, T value)
	{
		if(index == 0)
		{
			push(value);
			return;
		}
		
		Node<T> n = _get(index - 1);
		Node<T> p = n.next;
		n.next = new Node<T>(value);
		n.next.next = p;
	}
	
	public void remove(int index)
	{
		if(index == 0)
		{
			pop();
			return;
		}
		
		Node<T> n = _get(index - 1);
		n.next = n.next.next;
	}
	
	public int indexOf(Object value)
	{
		Node<T> n = head;
		int index = 0;
		while(n != null && !n.getValue().equals(value))
		{
			n = n.next;
			index ++;
		}
		if(n == null)
			return -1;
		return index;
	}
	
	public boolean remove(Object value)
	{
		int i = indexOf(value);
		if(i != -1)
			remove(i);
		else
			return false;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public final boolean add(Object value) 
	{ 
		try
		{
			push((T)value);
		}
		catch(ClassCastException e)
		{
			return false;
		}
		return true;
	}
	
	public final void add(int index, T value) { insert(index, value); }
	
	public boolean isEmpty() { return !hasNext(); }
	
	@Override
	public boolean removeAll(Collection<?> c)
	{
		boolean success = true;
		for(Object value : c) {
			success |= remove(value);
		}
		return success;
	}
	
	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		boolean success = true;
		for(Object value : c) {
			success |= add(value);
		}
		return success;
	}
	
	public T next() { return pop(); }
	public void clear() { head=null; }
	
	public boolean contains(Object value)
	{
		Node<T> n = head;
		while(n != null && !n.getValue().equals(value))
		{
			n = n.next;
		}
		return n != null;
	}
	
	@Override
	public boolean containsAll(Collection<?> c)
	{
		for(Object value : c)
			if(! contains(value))
				return false;
		return true;
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return this;
	}
	
	@Override
	public boolean retainAll(Collection<?> c)
	{
		boolean changed = false;
		
		while(head !=null && !c.contains(head))
		{
			head = head.next;
			changed = true;
		}
		
		Node<T> n = head;
		while(n != null)
		{
			while(n.next != null && !c.contains(n.next))
			{
				n.next = n.next.next;
				changed = true;
			}
			n = n.next;
		}
		return changed;
	}
	
	@SuppressWarnings("unchecked")
	public <U extends Object> U[] toArray(U[] a) 
	{
		Node<T> n = head;
		for(int i = 0; i < a.length; i++)
		{
			if(n == null)
				a[i] = null;
			else
				try
				{
					a[i] = (U)n.getValue();
				}
				catch(ClassCastException e)
				{
					a[i] = null;
				}
			
			if(n != null)
				n = n.next;
		}
		return a;
	}
	
	@Override
	public Object[] toArray() { return toArray(new Object[size()]); }
}

class Node <T>
{
	public Node<T> next = null;
	private T val;
	
	public Node(T value)
	{
		this.val = value;
	}
	
	public T getValue() { return val; }
}