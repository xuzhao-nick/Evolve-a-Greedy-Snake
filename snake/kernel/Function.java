/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package kernel;

/**
 * Base class for function node
 */
abstract public class Function extends Program {

	public Object DeepClone() {
		Function temp = null;
		try {
			temp = (Function) getClass().newInstance(); // copy current instance
			for (int i = 0; i < SubItems.length; i++) // copy child instances
			{
				temp.SubItems[i] = (Program) SubItems[i].DeepClone();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	public int countNodes(boolean OnlyFunction) {
		int count = 1;
		for (int a = 0; a < SubItems.length; a++) {
			count = count + SubItems[a].countNodes(OnlyFunction);
		}
		return count;
	}
}
