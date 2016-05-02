/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package function;
import kernel.*;
//if turn right have danger, do the first branch, otherwise do the second branch
public class ifDangerLeft extends Function {

	public ifDangerLeft() {
		SubItems = new Program[2];
		symbol = "ifDangerLeft";
	}

	public Object eval() 
	{
		SnakeModel snake = SnakeModel.getInstance();
		if(!snake.running)
		    return null;
		if(snake.ifDangerLeft())
		    return SubItems[0].eval();
		else
		    return SubItems[1].eval();
	}

}