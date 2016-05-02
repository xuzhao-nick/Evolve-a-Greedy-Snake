/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

//
public class ifMovingUp extends Function {

	public ifMovingUp() {
		SubItems = new Program[2];
		symbol = "ifMovingUp";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		if (snake.ifMovingUp())
			return SubItems[0].eval();
		else
			return SubItems[1].eval();
	}

}