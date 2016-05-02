/*Author:Xu Zhao*/
/*keystonexu@yahoo.com.cn*/
package function;

import kernel.*;

////if have food ahead, do the second branch, then do the first branch
public class ifFoodAhead extends Function {

	public ifFoodAhead() {
		SubItems = new Program[2];
		symbol = "ifFoodAhead";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		if (snake.ifFoodAhead())
			return SubItems[0].eval();
		else
			return SubItems[1].eval();
	}

}