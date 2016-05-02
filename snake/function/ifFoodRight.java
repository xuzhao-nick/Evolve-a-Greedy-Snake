/*Author: Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

//
public class ifFoodRight extends Function {

	public ifFoodRight() {
		SubItems = new Program[2];
		symbol = "ifFoodRight";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		if (snake.ifFoodRight())
			return SubItems[0].eval();
		else
			return SubItems[1].eval();
	}

}