/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

//
public class ifMovingLeft extends Function {

	public ifMovingLeft() {
		SubItems = new Program[2];
		symbol = "ifMovingLeft";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		if (snake.ifMovingLeft())
			return SubItems[0].eval();
		else
			return SubItems[1].eval();
	}

}