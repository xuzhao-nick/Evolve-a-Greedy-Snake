/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package kernel;

/**
 * Every individual include a syntax tree
 */
public class Individual {
	public Program program; // point to the syntax tree
	public double standardizedFitness;
	public double adjustedFitness;
	public double normalizedFitness;
	public int hits;

	public Individual() {
		standardizedFitness = 0.0;
		adjustedFitness = 0.0;
		normalizedFitness = 0.0;

	}

	public Individual(Program p) {
		program = p;
		program.setParent();
		// program = p;
		standardizedFitness = 0.0;
		adjustedFitness = 0.0;
		normalizedFitness = 0.0;
		hits = 0;
	}

	public String getParameters() {
		String str = "standardizedFitness = " + standardizedFitness + "\n";
		str = str + "adjustedFitness = " + adjustedFitness + "\n";
		str = str + "normalizedFitness = " + normalizedFitness + "\n";
		return str;
	}

	public String toString() {
		return program.toString();
	}

	public String toString(boolean isNo) {
		return program.toString(isNo, false);
	}

	void setParent() {
		program.setParent();
	}

	// return a copy
	Individual copy() {
		Program p = (Program) (program.DeepClone());
		p.setParent();
		Individual newInd = new Individual(p);
		newInd.standardizedFitness = this.standardizedFitness;
		newInd.adjustedFitness = this.adjustedFitness;
		newInd.normalizedFitness = this.normalizedFitness;
		newInd.hits = this.hits;
		return newInd;
	}

}
