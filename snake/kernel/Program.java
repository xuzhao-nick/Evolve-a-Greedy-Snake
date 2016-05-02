/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package kernel;

/**
 * base class for node
 */
public abstract class Program {
	public String symbol;// symbol represent that node
	public Program SubItems[]; // sub node of that node
	Program parent; // point to father node
	int no; // number of sub nodes
	int noFunction;// number of functions on sub nodes
	int index;//

	abstract public Object eval();

	/**
	 * get sub tree's node count, if OnlyFunction is true, return only function
	 * node count
	 */
	abstract public int countNodes(boolean OnlyFunction);

	/**
	 * deep copy sub tree
	 */
	abstract public Object DeepClone();

	// display tree

	public String displayTree(Program p) {
		String s = "";
		if (p != null) {

			if (p.SubItems != null) {
				s = s + "(" + p.symbol + " ";
				for (int i = 0; i < p.SubItems.length; i++) {
					s = s + " ";
					s = s + displayTree(p.SubItems[i]);
					s = s + " ";
				}
				s = s + ")";
				return s;
			} else {
				s = s + p.symbol;
				return s;
			}

		} else
			return s;
	}

	// set parent's reference and set the index of every sub node
	// There have two kind of index:
	// Index 1:
	// no: index for every node
	// Index 2:
	// noFunction: only for function node, set index
	void setParent(Program p, int[] count, int[] countFunction) {
		p.no = count[0];
		count[0]++;
		if (p != null) {
			if (p.SubItems != null) {
				// the node have sub node, then this is not a leaf node.
				// then increase the number of function node,and set index
				p.noFunction = countFunction[0];
				countFunction[0]++;
				for (int i = 0; i < p.SubItems.length; i++) {
					p.SubItems[i].parent = p;
					p.SubItems[i].index = i;
					setParent(p.SubItems[i], count, countFunction);
				}
			}
		}
	}

	// set point to parent
	public void setParent() {
		int[] count = { 1 };
		int[] countFunction = { 1 };
		this.parent = null;
		setParent(this, count, countFunction);
	}

	// display tree,isNo means if display node index,isFunction means if display
	// function index
	public String toString(boolean isNo, boolean isFunction) {
		setParent();
		String str = displayTree1(this, isNo, isFunction);
		return str;
	}

	public String toString(boolean isNo) {
		return toString(isNo, false);
	}

	public String toString() {
		return displayTree(this);
	}

	// Show indent for tree
	String getMargin(Program p, boolean isFirst) {
		if (p.parent == null)
			return "";
		else {
			// last child index
			int lastChild = p.parent.SubItems.length - 1;
			int thisChild = 0;
			for (int i = 0; i < p.parent.SubItems.length; i++) {
				if (p.parent.SubItems[i].equals(p))
					thisChild = i;
			}
			if (isFirst) // if first time call this method
				// if this child is the last child
				if (lastChild == thisChild)
					return getMargin(p.parent, false) + "|_";
				else
					return getMargin(p.parent, false) + "|-";
			else if (lastChild == thisChild)
				return getMargin(p.parent, false) + "  ";
			else
				return getMargin(p.parent, false) + "| ";
		}

	}

	// display node
	String displayNode(Program p, boolean isNo, boolean isFunction) {
		String str = getMargin(p, true) + p.symbol;
		if (isNo)
			str = str + "(" + p.no + ")";
		if (isFunction)
			str = str + "(" + p.noFunction + ")";
		str = str + "\n";
		return str;
	}

	// display node
	String displayTree1(Program p, boolean isNo, boolean isFunction) {
		String str = "";
		if (p != null) {
			str = str + displayNode(p, isNo, isFunction);
			if (p.SubItems != null) {
				for (int i = 0; i < p.SubItems.length; i++)
					str = str + displayTree1(p.SubItems[i], isNo, isFunction);
			}
		}
		return str;
	}
	// get the node which index is no.
	// if isFunction is true, get the function node based on noFunction.

	Program indexOf(int no, boolean isFunction) {
		if (this.no == no && !isFunction)
			return this;
		if (this.noFunction == no && isFunction)
			return this;
		Queue queue = new Queue();
		queue.enq(this);
		Program p;
		while (!queue.isEmpty()) {
			p = (Program) (queue.front());
			if (p.SubItems != null) {
				for (int i = 0; i < p.SubItems.length; i++) {
					if (p.SubItems[i].no == no && !isFunction)
						return p.SubItems[i];
					if (p.SubItems[i].noFunction == no && isFunction)
						return p.SubItems[i];
					queue.enq(p.SubItems[i]);
				}
			}
			queue.deq();
		}
		return null;
	}

}
