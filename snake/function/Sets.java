/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package function;
import kernel.*;
/**
*Terminal set and function set generation
*/

public class Sets extends SetInterface
{
	public Sets()
	{
		TerminalSet = new Class[3];
        TerminalSet[0] = new Right().getClass();
        TerminalSet[1] = new Left().getClass();
        TerminalSet[2] = new Forward().getClass();

		FunctionSet = new Class[12];
        FunctionSet[0] = new ifFoodAhead().getClass();
        FunctionSet[1] = new ifDangerAhead().getClass();
        FunctionSet[2] = new ifDangerRight().getClass();
        FunctionSet[3] = new ifDangerLeft().getClass();
        FunctionSet[4] = new progn2().getClass();
        FunctionSet[5] = new ifDangerTwoAhead().getClass();
        FunctionSet[6] = new ifFoodUp().getClass();
        FunctionSet[7] = new ifFoodRight().getClass();
        FunctionSet[8] = new ifMovingRight().getClass();
        FunctionSet[9] = new ifMovingLeft().getClass();
        FunctionSet[10] = new ifMovingUp().getClass();
        FunctionSet[11] = new ifMovingDown().getClass();
			
	}
}
