/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package kernel;

/**
 * base class for leaf node
 */
abstract public class Terminal extends Program {
	public int countNodes(boolean OnlyFunction) {
		if (OnlyFunction)
			return 0;
		else
			return 1;
	}

	public String toString() {

		return symbol;
	}

}