
/**************************************************************************
Author:Xu Zhao
email:keystonexu@gmail.com
*************************************************************************/
package gui;

import function.*;
import java.awt.*;
import java.util.*;
import java.net.URL;

//
//Draw snakes
//
public class GreedSnakeUI extends Panel implements Observer {
	public Image snake, floor, foods, snakehead;
	public Image backGround, offScreen;
	public Graphics drawOffScreen;
	boolean init = false;
	Label labelScore;// score board
	boolean running = false;
	SnakeModel snakeModel = null;
	int timeInterval = 200;
	public static int Width;
	public static int Height;
	public static int nodeWidth;
	public static int nodeHeight;
	int paintcount = 0;

	// ----------------------------------------------------------------------
	// GreedSnake():init game graphics
	// ----------------------------------------------------------------------
	public GreedSnakeUI(Label labelScore) {
		this.labelScore = labelScore;
		init = false;
	}

	public void init() {
		snakeModel = SnakeModel.getInstance();
		snakeModel.addObserver(this);
		// get panel height and width
		Width = snakeModel.maxX * 30;
		Height = snakeModel.maxY * 30;
		nodeWidth = 30;
		nodeHeight = 30;
		// mediaTracker for track the loading condition
		// after loading then display
		MediaTracker mediaTracker = new MediaTracker(this);
		// get URL
		URL snak = getClass().getResource("/gui/snake.gif");
		URL floo = getClass().getResource("/gui/floor.gif");
		URL food = getClass().getResource("/gui/foods.gif");
		URL hea = getClass().getResource("/gui/snakehead.gif");
		// get images
		snake = Toolkit.getDefaultToolkit().getImage(snak);
		floor = Toolkit.getDefaultToolkit().getImage(floo);
		foods = Toolkit.getDefaultToolkit().getImage(food);
		snakehead = Toolkit.getDefaultToolkit().getImage(hea);
		// put images to mediaTracker.
		mediaTracker.addImage(snake, 0);
		mediaTracker.addImage(floor, 0);
		mediaTracker.addImage(foods, 0);
		mediaTracker.addImage(snakehead, 0);
		try {
			// waiting for the end of loading
			mediaTracker.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// create drawing buffer
		offScreen = this.createImage(Width, Height);
		drawOffScreen = offScreen.getGraphics();
		// create background image
		backGround = createImage(Width, Height);
		Graphics g = backGround.getGraphics();
		// draw background
		for (int i = 0; i < snakeModel.maxX; i++) {
			for (int j = 0; j < snakeModel.maxY; j++) {
				// System.out.println("i=" + i + " j=" + j);
				g.drawImage(floor, i * nodeWidth, j * nodeHeight, nodeWidth, nodeHeight, null);
			}
		}
		init = true;
		render();
	}

	public void paint(Graphics g) {
		if (init == false)
			return;
		render();
	}

	// ----------------------------------------------------------------------
	// render()
	// ----------------------------------------------------------------------

	public void render() {
		if (init == false)
			return;
		// draw on buffer
		drawOffScreen.drawImage(backGround, 0, 0, null);
		if (snakeModel.showState()) {
			LinkedList na = snakeModel.nodeArray;
			Iterator it = na.iterator();
			int count = 0;
			while (it.hasNext()) {

				Node n = (Node) it.next();
				if (count == 0)
					drawOffScreen.drawImage(snakehead, n.x * nodeWidth, n.y * nodeHeight, nodeWidth - 1, nodeHeight - 1,
							null);
				else
					drawOffScreen.drawImage(snake, n.x * nodeWidth, n.y * nodeHeight, nodeWidth - 1, nodeHeight - 1,
							null);
				count++;
			}
			// draw food
			Node n = snakeModel.food;
			drawOffScreen.drawImage(foods, n.x * nodeWidth, n.y * nodeHeight, nodeWidth - 1, nodeHeight - 1, null);
		}
		Graphics g = getGraphics();
		// draw buffer on main screen
		g.drawImage(offScreen, 0, 0, null);

	}

	public void update(Observable ob, Object arg) {
		render();
		updateScore();
		try {
			Thread.sleep(timeInterval);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------------
	// drawNode() draw a node (snake part or food pard)
	// ----------------------------------------------------------------------
	private void drawNode(Graphics g, Node n) {
		Node n1 = new Node(3, 5);
		System.out.println("n.x=" + n.x + " n.y=" + n.y);
		g.fillRect(n1.x * nodeWidth, n1.y * nodeHeight, nodeWidth - 1, nodeHeight - 1);
	}

	// ----------------------------------------------------------------------
	// updateScore: change score
	// ----------------------------------------------------------------------
	public void updateScore() {
		String s = "Score: " + snakeModel.score + " Step: " + snakeModel.countMove + " Current Move: "
				+ snakeModel.currentMove;
		labelScore.setText(s);
	}

}
