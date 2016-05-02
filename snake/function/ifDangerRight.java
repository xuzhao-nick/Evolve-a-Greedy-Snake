/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

////if go to right have danger, do the first branch, otherwise do the second branch
public class ifDangerRight extends Function {

	public ifDangerRight() {
		SubItems = new Program[2];
		symbol = "ifDangerRight";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		if (snake.ifDangerRight())
			return SubItems[0].eval();
		else
			return SubItems[1].eval();
	}

}