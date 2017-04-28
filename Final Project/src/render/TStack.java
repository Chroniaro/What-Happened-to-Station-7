package render;

public class TStack
{

}

class Stack <T>
{
	Node<T> head = null;
	
	public void push(T value)
	{
		Node<T> p = head;
		head = new Node<T>(value);
		head.next = p;
	}
	
	public T pop()
	{
		Node<T> p = head;
		head = head.next;
		return p.getValue();
	}
	
	public T peek()
	{
		return head.getValue();
	}
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