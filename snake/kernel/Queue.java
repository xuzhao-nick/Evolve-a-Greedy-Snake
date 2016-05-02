/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package kernel;

import java.util.*;

//JAVA queue class 
class Queue {
	Vector v;

	Queue() {
		v = new Vector();
	}

	// insert element to the queue
	public void enq(Object x) {
		v.addElement(x);
	}

	// remove element from the queue
	Object deq() {
		Object x;
		if (v.isEmpty())
			return null;
		else
			x = v.elementAt(0);
		v.removeElementAt(0);
		return x;
	}

	// read an element from queue
	public Object front() {
		if (v.isEmpty())
			return null;
		return v.elementAt(0);
	}

	public boolean isEmpty() {
		return v.isEmpty();
	}

	public void clear() {
		v.removeAllElements();
	}
}
