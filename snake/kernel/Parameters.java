/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package kernel;

public class Parameters {
	// Parameters for generate syntax tree
	public static final int GROW = 0;
	public static final int FULL = 1;
	public static final int RAMPED_HALF_AND_HALF = 2;
	public static final int FITNESS_PROPORTIONATE = 0;
	public static final int TOURNAMENT = 1;

	public static int populationSize = 1000;
	public static int maximum_generations = 20;
	public static double best_fitness = 0.01;
	public static int number_of_fitness_cases = 10;
	public static int max_depth_for_new_individuals = 6;
	public static int max_depth_for_individuals_after_crossover = 10;
	public static int max_depth_for_new_subtrees_in_Mutants = 4;
	public static double fitness_proportionate_reproduction_fraction = 0.1;
	public static double crossover_at_any_point_fraction = 0.2;
	public static double crossover_at_function_point_fraction = 0.7;
	public static int crossoverFraction = 80;
	public static int fitnessProportionateReproFraction = 0;
	public static int mutationFraction = 20;
	public static int method_of_selection = TOURNAMENT;
	public static int method_of_generation = RAMPED_HALF_AND_HALF;

}