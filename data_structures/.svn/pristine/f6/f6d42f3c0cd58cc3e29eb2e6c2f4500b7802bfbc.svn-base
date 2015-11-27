
public class LinkedList 
{

	/*----------------------------------------------------- */
	/* Private Data Members -- LinkedList                   */
	/*----------------------------------------------------- */

	private Link head;
	private Link tail;
	private int length;

	/*----------------------------------------------------- */
	/* Constructor -- LinkedList                            */
	/*----------------------------------------------------- */

	LinkedList() 
	{
		head = tail = new Link();
		length = 0;
	}

	/*----------------------------------------------------- */
	/* Public Methods -- LinkedList                         */
	/*----------------------------------------------------- */

	public void clear() 
	{
		head.setNext(null);
		tail = head;
		length = 0;
	}

	public int size() 
	{
		return length;
	}

	public void add(Object elem) 
	{
		tail.setNext(new Link(elem, null));
		tail = tail.next();
		length++;
	}

	public void add(int index, Object elem) 
	{
		//Assert.notFalse(index >= 0 && index <= length,"Index not in list");
		Link tmp = head;
		for (int i = 0; i < index; i++)
		{
			tmp = tmp.next;
		}
		tmp.next = new Link(elem, tmp.next);
		length++;
	}

	public void remove(int index)
	{
		//Assert.notFalse(index >= 0 && index < length);
		Link tmp = head;
		for (int  i = 0; i < index; i++)
		{
			tmp = tmp.next;
		}
		tmp.next = tmp.next.next;
		length--;
	}

	public void remove(Object elem)
	{
		Link tmp = head;
		while (tmp.next != null && !tmp.next.element.equals(elem))
		{
			tmp = tmp.next;
		}
		if (tmp.next != null)
		{
			tmp.next = tmp.next.next;
			length--;
		}

	}

	public Object get(int index)
	{
		//Assert.notFalse(index >= 0 && index < length,"Index not in list");
		Link tmp = head.next;
		for (int i = 0; i < length; i++)
		{
			tmp = tmp.next;
		}
		return tmp.element;
	}
	
	public void selectionSortLL(Link list, boolean reversed) {
		
	}
	
	public void print() {
		Link current = head.next();
		while (current != null) {
			//System.out.println("Begin");
			System.out.println(current.element());
			current = current.next();
		}
	}
	
	public static void main(String args[]) {
		LinkedList l = new LinkedList();
		//Link header = new Link();
		//Link header = l.new Link(6);
		l.add(5);
		l.add(3);
		l.add(45);
		l.add(9);
		l.add(2);
		l.print();
		//l.selectionSortLL(, reversed)
	}

//	public ListIterator listIterator() 
//	{
//		return new InnerIterator(0);
//	}
//
//	public ListIterator listIterator(int index) 
//	{
//		return new InnerIterator(index);
//	}


	/*----------------------------------------------------- */
	/* Nested class -- Link                                 */
	/*----------------------------------------------------- */


	private class Link {

		/*----------------------------------------------------- */
		/*  Private Data Members -- Link                        */ 
		/*----------------------------------------------------- */

		private Object element;
		private Link next;

		/*----------------------------------------------------- */
		/*  Constructors -- Link                                */ 
		/*----------------------------------------------------- */

		Link(Object elem, Link nextelem) {
			element = elem;
			next = nextelem;
		}

		Link(Link nextelem) {
			next = nextelem;
		}

		Link() { }

		/*----------------------------------------------------- */
		/*  Access Methods -- Link                              */ 
		/*----------------------------------------------------- */

		Link next() {
			return next; 
		}

		Object element() {
			return element;
		}

		void setNext(Link nextelem) {
			next = nextelem;
		}

		void setElement(Object elem) {
			element = elem;
		}
	}
}