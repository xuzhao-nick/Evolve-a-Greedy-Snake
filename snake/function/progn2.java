/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

//
public class progn2 extends Function {

	public progn2() {
		SubItems = new Program[2];
		symbol = "progn2";
	}

	public Object eval() {
		SnakeModel snake = SnakeModel.getInstance();
		if (!snake.running)
			return null;
		SubItems[0].eval();
		SubItems[1].eval();
		return null;
	}

}