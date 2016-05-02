/*Author:Xu Zhao*/
/*keystonexu@gmail.com*/
package kernel;

public class MessageGp {
	public Evaluate evaluate;
	public Individual individual;
	public int currentGeneration;

	public MessageGp(int currentGeneration, Evaluate evaluate, Individual individual) {
		this.currentGeneration = currentGeneration;
		this.evaluate = evaluate;
		this.individual = individual;
	}
}