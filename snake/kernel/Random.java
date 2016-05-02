/*Author:Xu Zhao*/
/*keystonexu@gmail.com */
package kernel;

/**
 * random number generator, return random number from 0 to n-1, base on the seed
 */
class Random extends java.util.Random {
	public Random(int seed) {
		super(seed);
	}

	public Random() {
		super();
	}

	public int nextInt(int n) {

		return (int) (nextDouble() * n);
	}
}
