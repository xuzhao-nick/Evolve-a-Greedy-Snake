/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

//if go ahead have danger, do the first branch, otherwise do the second branch
public class ifDangerAhead extends Function {

	public ifDangerAhead() {
		SubItems = new Program[2];
		symbol = "ifDangerAhead";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		if (snake.ifDangerAhead())
			return SubItems[0].eval();
		else
			return SubItems[1].eval();
	}

}