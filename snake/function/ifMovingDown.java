/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

//
public class ifMovingDown extends Function {

	public ifMovingDown() {
		SubItems = new Program[2];
		symbol = "ifMovingDown";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		if (snake.ifMovingDown())
			return SubItems[0].eval();
		else
			return SubItems[1].eval();
	}

}