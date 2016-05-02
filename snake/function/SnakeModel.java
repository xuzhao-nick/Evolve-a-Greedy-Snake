/**************************************************************************
*Author:Xu Zhao
*Main points:
*1)matrix[][] is used to store map,if nothing then value is false.
* if have snake part or food��set it true��
*2)nodeArray is a LinkedList, used to save all parts of snake
*3)food used to save position of food, Node used to save positions
*4��Important functions:
*a, changeDirection(int newDirection), used to change direction of snake.
  Just used to change direction of snake head.
*b, newDirection() this will not be the opposite direction of previous snake��s direction. so use direction%2!=newDirection%2 to test.
*c, moveOn() used to update snake��s position. If beyond the border then terminate the game. Otherwise detect if the head meet food or body.
*************************************************************************/
package function;

import java.util.*;

//----------------------------------------------------------------------
//SnakeModel:
//----------------------------------------------------------------------
public class SnakeModel extends Observable {
	public boolean[][] matrix;//
	public LinkedList nodeArray = new LinkedList();
	public Node food;
	public int maxX = 20;// max width
	public int maxY = 11;// max height
	public int direction = 1;// direction
	public boolean running = false;
	public boolean paused = false;// game status
	private boolean isShow = false;
	public int score = 0;
	public int countMove = 0;
	public int maxScore;// max score
	// UP and DOWN are even number, RIGHT and LEFT are odd number
	public static final int UP = 2;
	public static final int DOWN = 4;
	public static final int LEFT = 1;
	public static final int RIGHT = 3;
	private static SnakeModel instance;
	public String currentMove = "";

	public static synchronized SnakeModel getInstance() {
		if (instance == null)
			instance = new SnakeModel();
		return instance;
	}

	public void clearAll() {
		score = 0;
		countMove = 0;
		;
		direction = 1;
		paused = false;
		running = false;
		isShow = false;
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = false;
			}
		// initial snake
		int initArrayLength = maxX > 20 ? 10 : maxX / 2;
		nodeArray.clear();
		for (int i = 0; i < initArrayLength; ++i) {
			int x = maxX / 2 + i;
			int y = maxY / 2;
			nodeArray.addLast(new Node(x, y));
			matrix[x][y] = true;// snake part put to true
		}
		maxScore = maxX * maxY - initArrayLength;
		food = createFood();
		matrix[food.x][food.y] = true;// food part put to true

	}

	// ----------------------------------------------------------------------
	// GreedModel():initial UI interface
	// ----------------------------------------------------------------------
	private SnakeModel() {
		matrix = new boolean[maxX][];
		for (int i = 0; i < maxX; ++i) {
			matrix[i] = new boolean[maxY];
			Arrays.fill(matrix[i], false);// set false to all positions
		}
		clearAll();
		// this.gs=gs;
		// this.maxX=maxX;
		// this.maxY=maxY;

	}

	// if go ahead one step have dangerous, return true
	public boolean ifDangerAhead() {
		return ifDanger(direction);
	}

	// if go ahead two step have dangerous, return true
	public boolean ifDangerTwoAhead() {
		// if go ahead one step have dangerous, of course return true
		if (ifDangerAhead())
			return true;
		Node n = (Node) nodeArray.getFirst();
		int x = n.x;
		int y = n.y;
		// go ahead two steps
		switch (direction) {
		case UP:
			y -= 2;
			break;
		case DOWN:
			y += 2;
			break;
		case LEFT:
			x -= 2;
			break;
		case RIGHT:
			x += 2;
			break;
		}
		// check if the position have dangerous
		return isDanger(x, y);
	}

	// if in the direction have dangerous, return true
	public boolean ifDanger(int direction1) {
		Node n = (Node) nodeArray.getFirst();
		int x = n.x;
		int y = n.y;
		switch (direction1) {
		case UP:
			y--;
			break;
		case DOWN:
			y++;
			break;
		case LEFT:
			x--;
			break;
		case RIGHT:
			x++;
			break;
		}
		return isDanger(x, y);

	}

	// check if one position have dangerous
	private boolean isDanger(int x, int y) {
		if ((0 <= x && x < maxX) && (0 <= y && y < maxY)) {
			if (matrix[x][y]) // eat food or hit body
			{

				if (x == food.x && y == food.y) // eat food
				{
					return false; // no dangerous
				} else
					return true;// hit body, have dangerous
			}
			// hit nothing, no dangerous
			return false;
		} else
			return true;// beyond the border, have dangerous
	}

	// if the food at the upper side of the snake
	public boolean ifFoodUp() {
		Node n = (Node) nodeArray.getFirst();
		int x = n.x;
		int y = n.y;
		if (y > food.y)
			return true;
		return false;
	}

	// if the food at the right side of the snake
	public boolean ifFoodRight() {
		Node n = (Node) nodeArray.getFirst();
		int x = n.x;
		int y = n.y;
		if (x < food.x)
			return true;
		return false;
	}

	public boolean ifFoodAhead() {
		Node n = (Node) nodeArray.getFirst();
		int x = n.x;
		int y = n.y;
		// base on snake's direction, check if the food on that position
		switch (direction) {
		case UP:
			if ((x == food.x) && (y > food.y))
				return true;
			break;
		case DOWN:
			if ((x == food.x) && (y < food.y))
				return true;
			break;
		case LEFT:
			if ((y == food.y) && (x > food.x))
				return true;
			break;
		case RIGHT:
			if ((y == food.y) && (x < food.x))
				return true;
			break;
		}
		return false;

	}

	public boolean ifDangerRight() {
		Node n = (Node) nodeArray.getFirst();
		int x = n.x;
		int y = n.y;
		int direction1 = 0;
		// base on current direction check the real direction when snake turn
		// right, then test if dangerous
		switch (direction) {
		case UP:
			direction1 = RIGHT;
			break;
		case DOWN:
			direction1 = LEFT;
			break;
		case LEFT:
			direction1 = UP;
			break;
		case RIGHT:
			direction1 = DOWN;
			break;
		}
		return ifDanger(direction1);
	}

	public boolean ifDangerLeft() {
		Node n = (Node) nodeArray.getFirst();
		int x = n.x;
		int y = n.y;
		int direction1 = 0;
		// base on current direction, check the real direction when snake turn
		// left
		// then check if dangerous when turn left
		switch (direction) {
		case UP:
			direction1 = LEFT;
			break;
		case DOWN:
			direction1 = RIGHT;
			break;
		case LEFT:
			direction1 = DOWN;
			break;
		case RIGHT:
			direction1 = UP;
			break;
		}
		return ifDanger(direction1);

	}

	public boolean ifMovingRight() {
		if (direction == RIGHT)
			return true;
		return false;
	}

	public boolean ifMovingLeft() {
		if (direction == LEFT)
			return true;
		return false;
	}

	public boolean ifMovingUp() {
		if (direction == UP)
			return true;
		return false;
	}

	public boolean ifMovingDown() {
		if (direction == DOWN)
			return true;
		return false;
	}

	// the snake turn right
	public void changeRight() {
		switch (direction) {
		case UP:
			changeDirection(RIGHT);
			break;
		case DOWN:
			changeDirection(LEFT);
			break;
		case LEFT:
			changeDirection(UP);
			break;
		case RIGHT:
			changeDirection(DOWN);
			break;
		}

	}

	// the snake turn left
	public void changeLeft() {
		switch (direction) {
		case UP:
			changeDirection(LEFT);
			break;
		case DOWN:
			changeDirection(RIGHT);
			break;
		case LEFT:
			changeDirection(DOWN);
			break;
		case RIGHT:
			changeDirection(UP);
			break;
		}
	}

	// ----------------------------------------------------------------------
	// changeDirection():change snake's direction
	// ----------------------------------------------------------------------
	public void changeDirection(int newDirection) {
		// don't move to the opposite of current direction
		if (direction % 2 != newDirection % 2) {
			direction = newDirection;
		}
	}

	// call moving function, then if need display, display it
	public boolean moveOnShow() {
		boolean moveState = moveOn();
		// if need display, display it
		if (isShow) {
			setChanged();
			// inform the observer
			notifyObservers(this);
		}
		return moveState;
	}

	// ----------------------------------------------------------------------
	// moveOn(): snake moving function
	// ----------------------------------------------------------------------
	public boolean moveOn() {

		Node n = (Node) nodeArray.getFirst();
		int x = n.x;
		int y = n.y;
		switch (direction) {
		case UP:
			y--;
			break;
		case DOWN:
			y++;
			break;
		case LEFT:
			x--;
			break;
		case RIGHT:
			x++;
			break;
		}
		if ((0 <= x && x < maxX) && (0 <= y && y < maxY)) {
			if (matrix[x][y]) // eat food or hit body
			{
				if (x == food.x && y == food.y) // eat food
				{
					nodeArray.addFirst(food);// add a snake part to the head of
												// food
					// add score,count move step and create new food
					score++;
					countMove++;
					food = createFood();
					matrix[food.x][food.y] = true;
					if ((countMove > maxX * maxY * 1.5) && !isShow)
						return false;
					else
						return true;
				} else
					return false;// hit the body
			} else// the snake hit nothing, then move the snake body
			{
				nodeArray.addFirst(new Node(x, y));// add a part to snake��s
													// head
				matrix[x][y] = true;
				n = (Node) nodeArray.removeLast();// remove a part from snake��s
													// nail
				matrix[n.x][n.y] = false;
				countMove++;
				if ((countMove > maxX * maxY * 1.5) && !isShow)
					return false;
				else
					return true;
			}
		}
		return false;// beyond the border(hit the wall)
	}

	// ----------------------------------------------------------------------
	// createFood():create random food position
	// ----------------------------------------------------------------------
	private Node createFood() {
		int x = 0;
		int y = 0;
		do {
			Random r = new Random();
			x = r.nextInt(maxX);
			y = r.nextInt(maxY);
		} while (matrix[x][y]);
		return new Node(x, y);
	}

	// ----------------------------------------------------------------------
	// changePauseState(): change game status(pause of continue)
	// ----------------------------------------------------------------------
	public void changePauseState() {
		paused = !paused;
	}

	public void enableShow(boolean isShow) {
		this.isShow = isShow;
		if (isShow) {

			setChanged();
			// inform the observer
			notifyObservers(null);
		}

	}

	public boolean showState() {
		return isShow;
	}

}
