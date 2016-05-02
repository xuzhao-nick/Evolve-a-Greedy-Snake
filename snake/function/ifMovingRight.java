/*Author Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

//
public class ifMovingRight extends Function {

	public ifMovingRight() {
		SubItems = new Program[2];
		symbol = "ifMovingRight";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		if (snake.ifMovingRight())
			return SubItems[0].eval();
		else
			return SubItems[1].eval();
	}

}