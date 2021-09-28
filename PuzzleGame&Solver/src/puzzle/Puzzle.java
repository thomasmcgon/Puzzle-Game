package puzzle;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Class to hold an 8-puzzle, 15-puzzle, or similar puzzle with
 * n^2 - 1 numbered tiles and an empty tile.
 */
@SuppressWarnings("serial")
public class Puzzle extends JFrame {	
	
	// Set look and feel to platform to look more like a native application
	static {		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {			
		}		
	}	

	/**
	 * Enum values for puzzle moves.
	 * Each value has a corresponding row change
	 * and column change.
	 */
	enum Direction { UP, DOWN, LEFT, RIGHT;

		/**
		 * Accessor for row change for the direction.
		 * @return change in row (-1, 0, 1)
		 */
		public int getRowChange() {
			switch(this) {			
			case UP: return 1;
			case DOWN: return -1;
			default: return 0;			
			}		
		}

		/**
		 * Accessor for column change for the direction.
		 * @return change in column (-1, 0, 1)
		 */
		public int getColumnChange() {
			switch(this) {			
			case LEFT: return 1;
			case RIGHT: return -1;
			default: return 0;			
			}		
		}	
	};

	/**
	 * Constant member giving number of rows and columns.
	 */
	public final int SIZE;
	
	private NumberButton[][] buttons;
	private int blankRow;
	private int blankColumn;
	
	/**
	 * Method that creates a new Puzzle.
	 * If command line arguments are present, it will try to convert
	 * the first argument to an integer and use it as the size.
	 * Otherwise, the size of the puzzle defaults to 4 rows by 4 columns.
	 * @param args command line arguments for size
	 */
	public static void main(String[] args) {
		int size = 4;

		if(args.length == 2) {
			try {
				size = Integer.parseInt(args[1]);
			}
			catch(NumberFormatException e) {}
		}

		new Puzzle(size);
	}

	/**
	 * Constructor to build a puzzle of a given size.
	 * @param size number of rows and columns
	 */
	public Puzzle(int size) {
		super((size*size - 1) + " Puzzle");
		SIZE = size;
		buttons = new NumberButton[SIZE][SIZE];
		
		JPanel panel = new JPanel(new GridLayout(SIZE,SIZE));
		for( int row = 0; row < SIZE; row++ )
			for( int column = 0; column < SIZE; column++ ) {
				// Makes SIZE*SIZE == 0, the empty spot
				int number = (row*SIZE + column + 1) % (SIZE*SIZE); 	
				NumberButton button = new NumberButton(number, row, column);
				panel.add(button);
				buttons[row][column] = button;

				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent event) {
						NumberButton swap = getSwap(button);
						if( swap != null ) {
							int swapNumber = swap.getNumber();
							int buttonNumber = button.getNumber();
							swap.setNumber(buttonNumber);							
							button.setNumber(swapNumber);
							blankRow = button.getRow();
							blankColumn = button.getColumn();
							checkForWin();		
						}				
					}

				});	
				
				button.setFocusable(false);
			}

		blankRow = SIZE - 1;
		blankColumn = SIZE - 1;
		
		addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {

				switch( e.getKeyCode() ) {
				case KeyEvent.VK_UP: move(Direction.UP); break;
				case KeyEvent.VK_DOWN: move(Direction.DOWN); break;
				case KeyEvent.VK_LEFT: move(Direction.LEFT); break;
				case KeyEvent.VK_RIGHT: move(Direction.RIGHT); break;
				}	
			}

			@Override
			public void keyReleased(KeyEvent e) {			
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		
		add(panel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		setPreferredSize(new Dimension(500,500));
		pack();		
		setResizable(false);
		scramble();
		setVisible(true);
	}

	/**
	 * Checks to see if the player has won.
	 */
	private void checkForWin() {
		int count = 0;
		for( int i = 0; i < SIZE; ++i )
			for( int j = 0; j < SIZE; ++j )
				if( isCorrectlyPlaced(buttons[i][j], i, j) )
					count++;

		if( count == SIZE*SIZE - 1 ) {
			for( int i = 0; i < SIZE; ++i )
				for( int j = 0; j < SIZE; ++j )
					buttons[i][j].setEnabled(false);
			JOptionPane.showMessageDialog(this, "You solved the puzzle!", "You win!", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	/**
	 * Determines whether a given button is in its solved position.
	 * @param button button to check
	 * @param row row of button
	 * @param column column of button
	 * @return true if the button is in the winning position, false otherwise
	 */
	private boolean isCorrectlyPlaced(NumberButton button, int row, int column) {
		return row * SIZE + column + 1 == button.getNumber();
	}

	/**
	 * Accessor for the buttons in the current state of the puzzle.
	 * @return buttons
	 */
	public NumberButton[][] getButtons() {
		return buttons;
	}

	private NumberButton getSwap( NumberButton button ) {
		int row = button.getRow();
		int column = button.getColumn();
		if( row > 0 && buttons[row - 1][column].isEmpty() )
			return buttons[row - 1][column];
		else if( row < SIZE - 1 && buttons[row + 1][column].isEmpty() )
			return buttons[row + 1][column];
		else if( column > 0 && buttons[row][column - 1].isEmpty() )
			return buttons[row][column - 1];
		else if( column < SIZE - 1 && buttons[row][column + 1].isEmpty() )
			return buttons[row][column + 1];
		else
			return null;
	}

	
	/**
	 * Method scrambles the tiles in a puzzle.
	 * Internally, this method calls the internal scramble method with a
	 * parameter equal to the total number of tiles squared. This parameter
	 * determines how many random moves are made to scramble the tiles.
	 * Total tiles squared generates a reasonably randomized puzzle.
	 */
	public void scramble() {
		scramble(SIZE*SIZE*SIZE*SIZE);
	}

	/**
	 * Private utility method to scramble the puzzle by making a random number
	 * of moves equal to the input parameter times.
	 * @param times number of times to move randomly
	 */
	private void scramble(int times) {
		Direction[] directions = new Direction[4];
		Random random = new Random();
		// Repeatedly fill directions with available moves
		for( int i = 0; i < times; ++i ) {
			int availableDirections = 0;
			if( blankRow > 0 )
				directions[availableDirections++] = Direction.DOWN;
			if( blankRow < SIZE - 1 )
				directions[availableDirections++] = Direction.UP;
			if( blankColumn > 0 )
				directions[availableDirections++] = Direction.RIGHT;
			if( blankColumn < SIZE - 1 )
				directions[availableDirections++] = Direction.LEFT;
			// Randomly select one of the available moves and move that way
			Direction direction = directions[random.nextInt(availableDirections)];
			move(direction, false);
		}	
	}

	
	/**
	 * Method to move in one of the four possible directions.
	 * This method will check for a win after the move is made.
	 * @param direction direction of move
	 */
	public void move(Direction direction) {
		move(direction, true);
	}

	/**
	 * Private utility method to move in one of the four possible directions,
	 * checking for a win or not, based on the input parameter.  Manually
	 * clicking on a button will cause check for win, but scrambling doesn't.
	 * @param direction direction to move in
	 * @param checkForWin true if it should check for a win, false otherwise
	 */
	private void move(Direction direction, boolean checkForWin) {
		// direction is which way a numbered square is moving
		// which is the *opposite* of the way the blank square is moving
		int swapRow = blankRow + direction.getRowChange();
		int swapColumn = blankColumn + direction.getColumnChange();

		if( swapRow >= 0 && swapRow < SIZE && swapColumn >= 0 && swapColumn < SIZE ) {
			NumberButton swapButton = buttons[swapRow][swapColumn];
			NumberButton blankButton = buttons[blankRow][blankColumn];
			int swapNumber = swapButton.getNumber();
			int buttonNumber = blankButton.getNumber();
			swapButton.setNumber(buttonNumber);
			blankButton.setNumber(swapNumber);
			blankRow = swapRow;
			blankColumn = swapColumn;
			if( checkForWin ) // Used when manually clicking
				checkForWin();
		}
	}
}
