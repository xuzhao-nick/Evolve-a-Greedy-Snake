/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

//
public class ifDangerTwoAhead extends Function {

	public ifDangerTwoAhead() {
		SubItems = new Program[2];
		symbol = "ifDangerTwoAhead";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		if (snake.ifDangerTwoAhead())
			return SubItems[0].eval();
		else
			return SubItems[1].eval();
	}

}