/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package kernel;

import java.util.*;
import java.util.Observer;
import java.util.Observable;

//core functions of genetic programming

public class Kernel extends Observable implements Runnable {
	Individual[] programs;
	Parameters para;
	public Evaluate evaluate;
	SetInterface set;
	boolean stop = false;
	public int currentGeneration = 0;
	// best individual
	public Individual bestOfRunIndividual = null;
	// best generation
	int generationOfBestOfRunIndividual = 0;
	/**
	 * random generator
	 */
	public static final int SEED = 45678;
	public static Random random = new Random();

	public Kernel(Parameters para, SetInterface set, Evaluate evaluate) {

		this.para = para;
		this.set = set;
		this.evaluate = evaluate;
	}

	/**
	 * get a random leaf node
	 */
	Terminal chooseFromTerminalSet() {
		Terminal choice;
		try {
			choice = null;
			int index = random.nextInt(set.TerminalSet.length);
			Class cls = set.TerminalSet[index];
			choice = (Terminal) (cls.newInstance());
		} catch (Exception e) {
			choice = null;
		}
		return choice;
	}

	/**
	 * get a random function node
	 */
	Function chooseFromFunctionSet() {
		Function choice;
		try {

			int index = random.nextInt(set.FunctionSet.length);
			Class cls = set.FunctionSet[index];
			choice = (Function) (cls.newInstance());
		} catch (Exception e) {
			choice = null;
		}
		return choice;
	}

	/**
	 * set parameters for a function node
	 */
	void createArgumentsForFunction(Function function, int allowableDepth, boolean fullP) {
		for (int i = 0; i < function.SubItems.length; i++) {
			function.SubItems[i] = createIndividualProgram(allowableDepth, false, fullP);
		}
	}

	/**
	 * recursively crete a syntax tree, return the head of that tree
	 * allowableDepth is the max depth for that tree. if it is zero, then can
	 * only create leaf node if topNodeP is true, can only create function node
	 * as top node fullP indicate if create a full tree or not
	 */
	Program createIndividualProgram(int allowableDepth, boolean topNodeP, boolean fullP) {
		Program p;
		int choice;
		Function function;
		if (allowableDepth <= 0) {
			// We've reached maxdepth, so just pack a terminal.
			p = chooseFromTerminalSet();
		} else {
			// if create a full tree, or topNode must be function node
			if (fullP || topNodeP) {
				// select a function node
				function = chooseFromFunctionSet();
				createArgumentsForFunction(function, allowableDepth - 1, fullP);
				p = function;
			} else {
				// from FunctionSet or TerminalSet, select a node
				choice = random.nextInt(set.FunctionSet.length + set.TerminalSet.length);
				if (choice < set.FunctionSet.length) {
					// select a function node
					try {
						Class cls = set.FunctionSet[choice];
						function = (Function) cls.newInstance();
					} catch (Exception e) {
						function = null;
					}
					createArgumentsForFunction(function, allowableDepth - 1, fullP);
					p = function;
				} else {
					// select a leaf node
					try {
						Class cls = set.TerminalSet[choice - set.FunctionSet.length];
						p = (Terminal) cls.newInstance();
					} catch (Exception e) {
						p = null;
					}

				}
			}
		}
		return p;
	}

	/**
	 * create one generation's program(syntax tree)
	 */
	void createPopulation() {
		int allowableDepth;
		boolean fullP;
		// create a hash table for compare if create a duplicate tree
		Hashtable generation0UniquifierTable = new Hashtable();
		programs = new Individual[para.populationSize];
		int minimumDepthOfTrees = 1;
		// use half-half method, first time create a full tree, second time
		// create a none full tree
		boolean fullCycleP = false;
		int maxDepthForNewIndivs = para.max_depth_for_new_individuals;
		int attemptsAtThisIndividual = 0;
		int individualIndex = 0;
		while (individualIndex < programs.length) {
			switch (para.method_of_generation) {
			// FULL (create full tree)
			case Parameters.FULL:
				allowableDepth = maxDepthForNewIndivs;
				fullP = true;
				break;
			// GROW (a none full tree)
			case Parameters.GROW:
				allowableDepth = maxDepthForNewIndivs;
				fullP = false;
				break;
			case Parameters.RAMPED_HALF_AND_HALF:
				// depth will increase from low to high then from high to low
				allowableDepth = minimumDepthOfTrees
						+ (individualIndex % (maxDepthForNewIndivs - minimumDepthOfTrees + 1));
				if (attemptsAtThisIndividual == 0
						&& individualIndex % (maxDepthForNewIndivs - minimumDepthOfTrees + 1) == 0) {
					fullCycleP = !fullCycleP;
				}
				fullP = fullCycleP;
				break;
			default:
				allowableDepth = maxDepthForNewIndivs;
				fullP = false;
				break;
			}
			// base on parameter, create a new syntax tree
			Program newProgram = createIndividualProgram(allowableDepth, true, fullP);

			// check if crete a duplicate one
			// if don't, then add it to the array
			// checking method: use toString get the represent of one tree,
			// then compare it with trees stored in hash table.
			String hashKey = newProgram.toString();
			if (!generation0UniquifierTable.containsKey(hashKey)) {

				programs[individualIndex] = new Individual(newProgram);
				programs[individualIndex].setParent();
				individualIndex++;
				generation0UniquifierTable.put(hashKey, newProgram);
				attemptsAtThisIndividual = 0;
			} else {
				attemptsAtThisIndividual++;
				if (attemptsAtThisIndividual > 20) {
					// if the duplicate time more than 20, that means that
					// all of that depth��s syntax tree already be generated.
					// then increase the minimum depth for generate syntax tree
					minimumDepthOfTrees++;
					maxDepthForNewIndivs = Math.max(maxDepthForNewIndivs, minimumDepthOfTrees);
					attemptsAtThisIndividual = 0;
				}
			}
		}
	}

	/**
	 * do mutate operation to random one of the node on syntax tree,then return
	 * that syntax tree
	 */
	Program mutate(Program program) {
		// get random index
		program.setParent();
		int mutationPoint = random.nextInt(program.countNodes(false)) + 1;
		// create new syntax tree, paste to that mutate point
		Program newSubtree = createIndividualProgram(para.max_depth_for_new_subtrees_in_Mutants, true, false);
		// if is head node
		if (mutationPoint == 1) {
			return newSubtree;
		}
		Program p = null;
		// get mutationPoint's reference
		p = program.indexOf(mutationPoint, false);
		p.parent.SubItems[p.index] = newSubtree;
		return program;
	}

	/**
	 * do crossover on two syntax tree (simulate male and female breed new
	 * generation) then breed two child tree
	 */
	Program[] crossover(Program male, Program female) {
		// CrossoverAtFunctionFraction:possibility when do crossover on function
		// node
		double CrossoverAtFunctionFraction = 0.9;
		boolean crossoverAtFunction;
		// first copy then crossover
		Program[] offspring = new Program[2];
		offspring[0] = (Program) male.DeepClone();
		offspring[1] = (Program) female.DeepClone();
		offspring[0].setParent();
		offspring[1].setParent();
		int malePoint, femalePoint;
		Program maleHook, femaleHook;
		// if crossover on function node
		crossoverAtFunction = (random.nextDouble() < CrossoverAtFunctionFraction);
		if (crossoverAtFunction) {
			malePoint = random.nextInt(offspring[0].countNodes(true)) + 1;
			maleHook = offspring[0].indexOf(malePoint, true);
		} else {
			malePoint = random.nextInt(offspring[0].countNodes(false)) + 1;
			maleHook = offspring[0].indexOf(malePoint, false);
		}
		crossoverAtFunction = (random.nextDouble() < CrossoverAtFunctionFraction);
		if (crossoverAtFunction) {
			femalePoint = random.nextInt(offspring[1].countNodes(true)) + 1;
			femaleHook = offspring[1].indexOf(femalePoint, true);
		} else {
			femalePoint = random.nextInt(offspring[1].countNodes(false)) + 1;

			femaleHook = offspring[1].indexOf(femalePoint, false);
		}
		// do crossover
		if (maleHook.parent == null) {
			femaleHook.parent = null;
			offspring[0] = femaleHook;
		} else {
			maleHook.parent.SubItems[maleHook.index] = femaleHook;
		}
		if (femaleHook.parent == null) {
			maleHook.parent = null;
			offspring[1] = maleHook;
		} else {
			femaleHook.parent.SubItems[femaleHook.index] = maleHook;
		}
		offspring[0].setParent();
		offspring[1].setParent();
		validateCrossover(male, female, offspring);
		return offspring;
	}

	/**
	 * return max depth of one tree
	 */
	int maxDepthOfTree(Program tree) {
		int maxDepth = 0;
		if (tree.SubItems != null) {
			for (int i = 0; i < tree.SubItems.length; i++) {
				Program s = tree.SubItems[i];
				int depth = maxDepthOfTree(s);
				maxDepth = Math.max(maxDepth, depth);
			}
			return (1 + maxDepth);
		} else {
			return 0;
		}
	}

	/**
	 * /* check the depth after crossover,if over the max depth, /* then use one
	 * of parent's syntax tree to replace it
	 */
	void validateCrossover(Program male, Program female, Program[] offspring) {
		int depth;
		for (int i = 0; i < offspring.length; i++) {
			// if have not had child
			if (offspring[i] == null) {
				depth = 0;
			} else {
				depth = maxDepthOfTree(offspring[i]);
			}
			if (depth < 1 || depth > para.max_depth_for_individuals_after_crossover) {
				int whichParent = random.nextInt(2);
				if (whichParent == 0) {
					offspring[i] = (Program) male.DeepClone();
				} else {
					offspring[i] = (Program) female.DeepClone();
				}
			}
		}
	}

	/**
	 * clear the fitness
	 */
	void zeroizeFitnessMeasuresOfPopulation() {
		for (int i = 0; i < programs.length; i++) {
			programs[i].standardizedFitness = 0.0;
			programs[i].adjustedFitness = 0.0;
			programs[i].normalizedFitness = 0.0;
			programs[i].hits = 0;
		}
	}

	/**
	 * eval the generation
	 */
	void evaluateFitnessOfPopulation() {
		for (int i = 0; i < programs.length; i++) {
			evaluate.Evaluate(programs[i]);
		}
	}

	/**
	 * get normalizedFitenss parameter (0-1) and adjustedFitness
	 */
	void normalizeFitnessOfPopulation() {
		double sumOfAdjustedFitnesses = 0.0;
		for (int i = 0; i < programs.length; i++) {
			programs[i].adjustedFitness = 1.0 / (programs[i].standardizedFitness + 1.0);

			sumOfAdjustedFitnesses = sumOfAdjustedFitnesses + programs[i].adjustedFitness;
		}
		for (int i = 0; i < programs.length; i++) {
			programs[i].normalizedFitness = programs[i].adjustedFitness / sumOfAdjustedFitnesses;
		}
	}

	/**
	 * sort the population by survival rate on descendent order
	 */
	private void sort(int low, int high) {
		int index1, index2;
		double pivot;
		Individual temp;

		index1 = low;
		index2 = high;
		pivot = programs[(low + high) / 2].normalizedFitness;
		do {
			while (programs[index1].normalizedFitness > pivot) {
				index1++;
			}
			while (programs[index2].normalizedFitness < pivot) {
				index2--;
			}
			if (index1 <= index2) {
				temp = programs[index2];
				programs[index2] = programs[index1];
				programs[index1] = temp;
				index1++;
				index2--;
			}
		} while (index1 <= index2);
		if (low < index2) {
			sort(low, index2);
		}
		if (index1 < high) {
			sort(index1, high);
		}
	}

	/**
	 * sort the population
	 */
	void sortprogramsByFitness() {
		sort(0, programs.length - 1);
	}

	/**
	 * from father generation to child generation
	 */
	void evolve() {
		Individual bestOfGeneration;
		// if is not the first generation, then create the new generation
		if (currentGeneration > 0) {
			breedNewPopulation();
		}
		// clear fitness
		zeroizeFitnessMeasuresOfPopulation();
		// eval fitness
		evaluateFitnessOfPopulation();
		normalizeFitnessOfPopulation();
		// descendent sort by normalised fitness
		sortprogramsByFitness();
		// keep the best generation
		bestOfGeneration = programs[0];
		if (bestOfRunIndividual == null
				|| bestOfRunIndividual.standardizedFitness > bestOfGeneration.standardizedFitness) {
			bestOfRunIndividual = bestOfGeneration.copy();
			generationOfBestOfRunIndividual = currentGeneration;
		}
		currentGeneration++;
	}

	/**
	 * create new generation
	 */
	void breedNewPopulation() {
		Program[] newPrograms;
		double fraction;
		int index;
		Individual individual1, individual2 = null;
		int i;
		double sumOfFractions = para.crossoverFraction + para.fitnessProportionateReproFraction + para.mutationFraction;
		double crossoverFraction = para.crossoverFraction / sumOfFractions;
		double reproductionFraction = para.fitnessProportionateReproFraction / sumOfFractions;
		double mutationFraction = para.mutationFraction / sumOfFractions;

		newPrograms = new Program[programs.length];
		fraction = 0.0;
		index = 0;

		// save the best tree at position of index = 0
		newPrograms[index] = (Program) bestOfRunIndividual.program.DeepClone();
		index++;

		while (index < programs.length) {
			fraction = (double) index / (double) programs.length;
			individual1 = findIndividual();
			if (fraction < crossoverFraction) {
				individual2 = findIndividual();
				Program[] offspring = crossover(individual1.program, individual2.program);
				newPrograms[index] = offspring[0];
				index++;
				if (index < programs.length) {
					newPrograms[index] = offspring[1];
					index = index++;
				}
			} else {
				if (fraction < reproductionFraction + crossoverFraction) {
					newPrograms[index] = (Program) individual1.program.DeepClone();
					index++;
				} else {
					newPrograms[index] = mutate(individual1.program);
					index++;
				}
			}
			individual1.setParent();
			individual2.setParent();
		}
		for (index = 0; index < programs.length; index++) {
			programs[index].program = newPrograms[index];
		}
	}

	/**
	 * if meet terminate condition, return true
	 */
	boolean terminationPredicate(int currentGeneration) {
		for (int i = 0; i < programs.length; i++) {
			if (evaluate.Termination_Criterion(programs[i], currentGeneration, para.best_fitness))
				return true;
		}
		return false;
	}

	/**
	 * get the individual which drop on one proportion
	 */
	Individual findFitnessProportionateIndividual(double afterThisFitness) {

		int indexOfSelectedIndividual;
		double sumOfFitness = 0.0;
		int index = 0;
		while (index < programs.length && sumOfFitness < afterThisFitness) {
			sumOfFitness = sumOfFitness + programs[index].normalizedFitness;
			index++;
		}
		if (index >= programs.length) {
			indexOfSelectedIndividual = programs.length - 1;
		} else {
			indexOfSelectedIndividual = index - 1;
		}
		return programs[indexOfSelectedIndividual];
	}

	/**
	 * random select 7 of them then get the best, return it
	 */
	Individual findIndividualUsingTournamentSelection() {
		int TournamentSize = Math.min(programs.length, 7);

		Hashtable table = new Hashtable();
		while (table.size() < TournamentSize) {
			int key = random.nextInt(programs.length);
			table.put(new Integer(key), programs[key]);
		}
		Enumeration e = table.elements();
		Individual best = (Individual) e.nextElement();
		double bestFitness = best.standardizedFitness;
		while (e.hasMoreElements()) {
			Individual individual = (Individual) e.nextElement();
			double thisFitness = individual.standardizedFitness;
			if (thisFitness < bestFitness) {
				best = individual;
				bestFitness = thisFitness;
			}
		}
		return best;
	}

	/**
	 * find best individule
	 */
	Individual findIndividual() {
		Individual ind = null;
		switch (para.method_of_selection) {
		case Parameters.TOURNAMENT:
			ind = findIndividualUsingTournamentSelection();
			break;
		case Parameters.FITNESS_PROPORTIONATE:
			ind = findFitnessProportionateIndividual(random.nextDouble());
			break;
		}
		return ind;
	}

	public void stopEvolve() {
		stop = true;
	}
	public void startEvolve() {
		stop = false;
	}
	/**
	 * evolve the syntax tree
	 */
	public void run() {
		random.setSeed(SEED);

		generationOfBestOfRunIndividual = 0;
		bestOfRunIndividual = null;
		// initial the populations
		createPopulation();
		currentGeneration = 0;
		stop = false;
		while (!terminationPredicate(currentGeneration) || !stop) {
			evolve();
			setChanged();
			notifyObservers(new MessageGp(currentGeneration, evaluate, bestOfRunIndividual));
		}
	}

}
