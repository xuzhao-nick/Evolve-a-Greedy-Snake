/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package kernel;

public interface Evaluate {
	// Evaluate one individual
	public void Evaluate(Individual individual);

	public boolean Termination_Criterion(Individual individual, int current_generation, double best_fitness);
}