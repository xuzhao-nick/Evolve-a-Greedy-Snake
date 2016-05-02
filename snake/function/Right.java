/*Author:Xu Zhao*/
/*keystonexu@yahoo.com.cn*/
package function;

import kernel.*;

/**
 * Snake turn right node
 */
public class Right extends Terminal {

	public Right() {
		symbol = "Right";
	}

	public Object DeepClone() {
		return new Right();
	}

	public String toString() {
		return "Right";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		// snake turn right
		snake.currentMove = "Turn Right";
		snake.changeRight();
		// snake move one step
		boolean state = snake.moveOnShow();
		// if hit something then stop
		if (state == false)
			snake.running = false;
		return null;
	}

}