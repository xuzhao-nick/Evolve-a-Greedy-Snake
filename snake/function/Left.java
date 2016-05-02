/*Author:Xu Zhao*/
/*keystonexu@gmail.cn*/
package function;

import kernel.*;

/**
 * snake turn left
 */
public class Left extends Terminal {

	public Left() {
		symbol = "Left";
	}

	public Object DeepClone() {
		return new Left();
	}

	public String toString() {
		return "Left";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		// turn left
		snake.changeLeft();
		snake.currentMove = "Turn Left";
		// go one step
		boolean state = snake.moveOnShow();
		// if hit body or wall, terminate it
		if (state == false)
			snake.running = false;
		return null;
	}

}