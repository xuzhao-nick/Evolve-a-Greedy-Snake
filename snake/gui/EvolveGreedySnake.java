/*Author Xu Zhao*/
/*keystonexu@gmail.com*/
package gui;

import kernel.*;
import function.*;
import java.awt.*;

import java.awt.event.*;
import java.util.Observer;
import java.util.Observable;

public class EvolveGreedySnake extends Frame implements ActionListener, Observer, WindowListener {
	Kernel kernel;
	Evaluation evaluation;
	TextArea textOfSyntaxTree;
	Label labelEvolveMessage;
	Button evolveButton;
	Button playButton;
	Thread evolveThread;
	SnakeModel snakemodel;
	Label labelScore;
	GreedSnakeUI greedSnakeUI;
	Individual individual;
	MessageGp message;
	Game game;
	final String START_PLAY = "Start Play";
	final String STOP_PLAY = "Stop Play";
	final String START_EVOLVE = "Start Evolve";
	final String STOP_EVOLVE = "Stop Evolve";

	public EvolveGreedySnake() {
		init();
	}

	public void init() {

		snakemodel = SnakeModel.getInstance();
		// setBackground(Color.lightGray);
		this.kernel = new Kernel(new Parameter(), new Sets(), new Evaluation());
		kernel.addObserver(this);
		evaluation = (Evaluation) (kernel.evaluate);
		setLayout(null);
		textOfSyntaxTree = new TextArea(80, 20);
		textOfSyntaxTree.setText("");
		textOfSyntaxTree.setFont(new Font("Arial", Font.PLAIN, 10));
		labelScore = new Label("Score:   Step:   Current Move:");
		labelScore.setBounds(220, 40, 300, 20);
		greedSnakeUI = new GreedSnakeUI(labelScore);
		greedSnakeUI.setBounds(220, 60, snakemodel.maxX * 30, snakemodel.maxY * 30);
		textOfSyntaxTree.setBounds(20, 60, 200, 500);
		evolveButton = new Button(START_EVOLVE);
		evolveButton.setBounds(250, 420, 120, 30);
		// start.enable(false); //for testing
		evolveButton.addActionListener(this);
		playButton = new Button(START_PLAY);
		playButton.addActionListener(this);
		// play.enable(false);
		playButton.setBounds(390, 420, 120, 30);
		playButton.setEnabled(false);
		Label labelBestSynTaxTree = new Label("Evolved Best Program");
		labelBestSynTaxTree.setBounds(20, 40, 200, 20);
		add(greedSnakeUI, BorderLayout.CENTER);
		add(textOfSyntaxTree);
		add(evolveButton);
		add(labelBestSynTaxTree);
		add(playButton);
		labelEvolveMessage = new Label("Generations:   Best Score:  ");
		labelEvolveMessage.setBounds(20, 570, 230, 20);
		add(labelEvolveMessage);
		addWindowListener(this);
		add(labelScore);
		Label labelUsage = new Label();
		String stringUsage = "Use \"<b>Genetic Programming</b>\" algorithm to evolve a greedy snake. \nAfter many generations evolving, this app will base on very basic \"syntax node\", automatic evolve a \"<b>best program</b>\" for snake to follow.\nThe higher the \"<b>Best score</b>\", the \"<b>Smarter</b>\" the snake.\nFirst, press \"<b>Start Evolve</b>\" button to evolve the snake.\nWhen you feel satisfy about the \"<b>Best Score</b>\"（maximum eaten food),\npress \"<b>Stop Evolve</b>\",then press \"<b>Start Play</b>\" to see how \"<b>clever</b>\" the snake is!\n";
		labelUsage.setText(convertToMultiLine(stringUsage));
		labelUsage.setBounds(250, 450, 450, 150);
		add(labelUsage);
		setSize(1000, 700);
		setTitle("Evolve a Greedy Snake		Author: Xu Zhao");
		setVisible(true);
		greedSnakeUI.init();
		// testIndividual();

	}
	
	public static String convertToMultiLine(String original)
	{
	    return "<html>" + original.replaceAll("\n", "<br>");
	}

	public void update(Observable ob, Object arg) {
		message = (MessageGp) arg;
		textOfSyntaxTree.setText("");
		textOfSyntaxTree.setText(message.individual.program.toString(false));
		labelEvolveMessage.setText( "Generations: " + message.currentGeneration + "  Best Score: " + message.individual.hits);
		individual = message.individual;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(START_EVOLVE)) {
			evolveButton.setLabel(STOP_EVOLVE);
			evolveThread = new Thread(kernel);
			playButton.setEnabled(false);;
			evolveThread.start();

		}
		if (e.getActionCommand().equals(STOP_EVOLVE)) {
			evolveButton.setLabel(START_EVOLVE);
			evolveThread.stop();
			playButton.setEnabled(true);
		}
		if (e.getActionCommand().equals(START_PLAY)) {
			playButton.setLabel(STOP_PLAY);
			evolveButton.setEnabled(false);
			evolveButton.setLabel(START_EVOLVE);
			game = new Game(greedSnakeUI);
			game.individual = individual;
			game.start();
		}
		if (e.getActionCommand().equals(STOP_PLAY)) {
			playButton.setLabel(START_PLAY);
			evolveButton.setEnabled(true);
			snakemodel.enableShow(false);
			snakemodel.running = false;
		}

	}

	public void testIndividual() {
		individual = new Individual();
		individual.program = new ifMovingLeft();
		individual.program.SubItems[0] = new Right();
		individual.program.SubItems[1] = new Left();

		textOfSyntaxTree.setText(individual.toString(false));

	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		dispose();
		System.exit(0);
	}

	public static void main(String args[]) {
		new EvolveGreedySnake();
	}
}
