/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package function;

import kernel.*;

//Parameters
public class Parameter extends Parameters {
	public Parameter() {
		populationSize = 4000;
		maximum_generations = 100;
		max_depth_for_new_individuals = 4; // max depth for one syntax tree
		max_depth_for_individuals_after_crossover = 10; // after cross over, max
														// depth for one syntax
														// tree
		max_depth_for_new_subtrees_in_Mutants = 2; // after mutant, the max
													// syntax tree��s depth
		fitness_proportionate_reproduction_fraction = 0.1;// individule mutant
															// possibility
		crossover_at_any_point_fraction = 0.2; // possibility of do crossover on
												// one leaf node
		crossover_at_function_point_fraction = 0.7;// possibility of do
													// crossover on one function
													// node
		crossoverFraction = 90;
		fitnessProportionateReproFraction = 0;
		mutationFraction = 10;
		method_of_selection = TOURNAMENT;// the method of select best tree
		method_of_generation = RAMPED_HALF_AND_HALF;// the method of create tree
	}

}
