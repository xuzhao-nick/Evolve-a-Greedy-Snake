/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

/**
 * go forward one step
 */
public class Forward extends Terminal {

	public Forward() {
		symbol = "Forward";
	}

	public Object DeepClone() {
		return new Forward();
	}

	public String toString() {
		return "Forward";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		// snake go ahead one step
		boolean state = snake.moveOnShow();
		snake.currentMove = "Go Ahead";
		// if snake hit wall or hit body, terminate it
		if (state == false)
			snake.running = false;
		return null;
	}

}