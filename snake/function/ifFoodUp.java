/*Author: Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

//
public class ifFoodUp extends Function {

	public ifFoodUp() {
		SubItems = new Program[2];
		symbol = "ifFoodUp";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		if (snake.ifFoodUp())
			return SubItems[0].eval();
		else
			return SubItems[1].eval();
	}

}