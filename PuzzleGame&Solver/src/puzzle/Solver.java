package puzzle;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import puzzle.Puzzle.Direction;

/**
 * Class to solve an instance of a {@link Puzzle}.
 */
public class Solver {

	/**
	 * Method that creates a new {@code Solver}.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		new Solver();
	}

	/**
	 * Default constructor for {@code Solver} tries to solve a new 4 x 4
	 * {@code Puzzle}.
	 */
	public Solver() {
		this(new Puzzle(4));
	}

	/**
	 * Constructor for {@code Solver} that tries to solve the given {@code Puzzle}.
	 * 
	 * @param puzzle puzzle to solve
	 */
	public Solver(Puzzle puzzle) {
		// Map buttons to starting state
		int[][] state = initializeState(puzzle.getButtons());
		// Find row and column of blank tile
		int size = state.length;
		int blankRow = 0;
		int blankColumn = 0;
		for (int i = 0; i < size; ++i)
			for (int j = 0; j < size; ++j)
				if (state[i][j] == size * size) {
					blankRow = i;
					blankColumn = j;
				}

		// Construct starting state
		State startingState = new State(state, null, null, blankRow, blankColumn);

		// Find final solved state
		State finalState = solve(startingState);

		if (finalState != null) {

			// Backtrack from final state to beginning,
			// adding the move needed for each state to a deque of moves
			Deque<Puzzle.Direction> moves = new ArrayDeque<>();
			State currentState = finalState;
			while (currentState.getPrevious() != null) {
				moves.addFirst(currentState.getMove());
				currentState = currentState.getPrevious();
			}

			System.out.println("Solvable in " + moves.size() + " moves:");

			// Print out the moves needed to solve the puzzle
			// and perform the moves on the GUI at the same time
			boolean first = true;
			for (Puzzle.Direction move : moves) {
				puzzle.move(move);

				// Print a comma before all moves except the first
				if (first) {
					first = false;
					System.out.print(move);
				} else
					System.out.print(", " + move);

				// Sleep for 0.1 seconds (100 milliseconds) between each move,
				// allowing the user to see the puzzle solved in real time
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}

			System.out.println();
		} else
			System.out.println("Unsolvable!");
	}

	/**
	 * Solves a puzzle starting at a given {@code State}.
	 * 
	 * @param startingState starting state of solution
	 * @return final state or null if unsolvable
	 */
	private static State solve(State startingState) {
		//Creating queue and adding the startingState to it
		PriorityQueue<State> priorityQueue = new PriorityQueue<>();
		priorityQueue.add(startingState);
		//Creating HashSet
		Set<State> set = new HashSet<>();
		//While queue is not empty it decides if the state isSolved or canMove
		//If the state canMove then it moves in that direction
		while (!priorityQueue.isEmpty()) {
			State state = priorityQueue.remove();
			if (!set.contains(state)) {
				set.add(state);
				if (state.isSolved()) {
					return state;
				}
				if (state.canMove(Direction.LEFT)) {
					priorityQueue.add(state.move(Direction.LEFT));
				}
				if (state.canMove(Direction.RIGHT)) {
					priorityQueue.add(state.move(Direction.RIGHT));
				}
				if (state.canMove(Direction.UP)) {
					priorityQueue.add(state.move(Direction.UP));
				}
				if (state.canMove(Direction.DOWN)) {
					priorityQueue.add(state.move(Direction.DOWN));
				}
			}
		}
		//No solution was found
		return null;
	}

	/**
	 * Creates an array of state information based on buttons from a {@code Puzzle}.
	 * The 2D array of state information is created with the same size as the
	 * buttons array. Each {@code int} value in the array has is assigned the same
	 * value as the number from its corresponding button, except for the blank
	 * button, whose number is 0 but is assigned the dimension of the puzzle squared
	 * (giving it the appropriate position at the last tile in the puzzle when
	 * solved).
	 * 
	 * @param buttons 2D array of buttons corresponding to the current puzzle
	 * @return 2D array of {@code int} values numbered like the buttons
	 */
	private static int[][] initializeState(NumberButton[][] buttons) {
		//Creating 2d array named state
		int[][] state = new int[buttons.length][buttons[0].length];
		//Loops through the state array
		for (int i = 0; i < buttons.length; ++i) {
			for (int j = 0; j < buttons[0].length; ++j) {
				//If the value on the buttons is 0 then assign the width squared to the state
				//Otherwise assign the value on the button to the state
				if (buttons[i][j].getNumber() == 0) {
					state[i][j] = buttons.length * buttons[0].length;
				} else {
					state[i][j] = buttons[i][j].getNumber();
				}
			}
		}
		return state;
	}
}