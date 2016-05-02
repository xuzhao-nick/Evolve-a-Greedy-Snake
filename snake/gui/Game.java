/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package gui;

import function.*;
import kernel.*;

//Play the game
public class Game extends Thread {
	GreedSnakeUI g;
	SnakeModel snakemodel;
	Individual individual;

	Game(GreedSnakeUI g) {
		snakemodel = SnakeModel.getInstance();
		this.g = g;
	}

	public void run() {
		snakemodel.clearAll();
		snakemodel.running = true;
		snakemodel.enableShow(true);
		while (snakemodel.running) {
			individual.program.eval();
		}
		snakemodel.enableShow(false);
		g.render();
	}

}