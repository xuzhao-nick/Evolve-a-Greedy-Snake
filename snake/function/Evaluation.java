/*Author Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

/**
 * check if this syntax tree can survive, get the average score on 4 times
 * running
 */
public class Evaluation implements Evaluate {

	public Evaluation() {
	}

	// return average moving steps
	public int Evaluate(Program program) {

		int score[] = new int[4];
		int sum = 0;
		for (int i = 0; i < score.length; i++) {
			score[i] = getOneEvaluate(program);
			sum = sum + score[i];
		}
		System.out.print(score[0] + " " + score[1] + " " + score[2] + " " + score[3] + " ");
		System.out.println((int) (sum * 1.0 / score.length));
		return (int) (sum * 1.0 / score.length);

	}

	public void Evaluate(Individual individual) {
		SnakeModel snake = SnakeModel.getInstance();
		individual.hits = getOneEvaluate(individual.program);
		if (individual.hits == 0)
			individual.standardizedFitness = snake.maxScore + 100;
		else
			individual.standardizedFitness = snake.maxScore - individual.hits;
	}

	public int getOneEvaluate(Program program) {
		SnakeModel snake = SnakeModel.getInstance();
		snake.clearAll();
		snake.running = true;
		while (snake.running) {
			program.eval();
		}
		return snake.score;
	}

	// evaluate terminate condition
	public boolean Termination_Criterion(Individual individual, int current_generation, double best_fitness) {
		if (current_generation >= Parameter.maximum_generations)
			return true;
		return false;

	}

}