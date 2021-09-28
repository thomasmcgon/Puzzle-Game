package puzzle;

/**
 * The {@code State} class holds a representation of one of the (many)
 * states a {@link Puzzle} can be in.
 */
public class State implements Comparable<State> {
	private final int[][] state; 			// Array holding tile numbers (blank has value dimension*dimension) 
	private final int cost;					// Estimate of cost to solve
	private final Puzzle.Direction move; 	// Move that led to this state 
	private final State previous;			// Previous state
	private final int blankRow;				// Row of blank tile
	private final int blankColumn;			// Column of blank tile
	
	
	/**
	 * Constructor sets member variables and calculates cost.
	 * @param state state of tiles
	 * @param move move that led to this state
	 * @param previous previous state
	 * @param blankRow row of blank tile
	 * @param blankColumn column of blank tile
	 */
	//Constructor setting all the member variables to the input variables
	public State(int[][] state, Puzzle.Direction move, State previous, int blankRow, int blankColumn) {
		this.state = state;
		this.move = move;
		this.previous = previous;
		this.blankRow = blankRow;
		this.blankColumn = blankColumn;
		cost = cost(state);
	}
	
	/**
	 * Accessor for previous state
	 * @return previous state
	 */
	//Accessor used to get the previous state
	public State getPrevious() {
		return previous;
	}
	
	/**
	 * Accessor for move that led to this state
	 * @return move
	 */
	//Accessor used to get the next state
	public Puzzle.Direction getMove() {
		return move;
	}
	
	/**
	 * Method that checks to see if moving in a direction is allowed
	 * (within bounds of puzzle).
	 * @param direction of move
	 * @return true if the move is allowed, false otherwise
	 */
	public boolean canMove(Puzzle.Direction direction) {
		//As long as the RowChange + the blankRow are less than the length of the state and greater to or equal to 0
		//And the ColumnChange + the blankColumn are less or equal to the states length and greater to or equal to 0
		//then it is legal to move
        if((direction.getRowChange() + blankRow) < state.length && (direction.getRowChange() + blankRow) >= 0 && 
                (direction.getColumnChange() + blankColumn) < state[0].length && (direction.getColumnChange() + blankColumn >= 0)) {
            return true;
        }
        //It is not legal to move
        else {
        	return false;
        }
    }
    
    /**
     * Creates a new state from current state based on moving in the given
     * direction.
     * @param direction of move
     * @return new state
     */
    public State move(Puzzle.Direction direction) { 
    	//Creating a 2d array called newState
        int[][] newState = new int[state.length][state[0].length];
        //Looping over the state to copy its values into newState
        for(int i = 0; i < state.length; ++i) {
            for(int j = 0; j < state[0].length; ++j) {
                newState[i][j]= state[i][j];
            }
        }
        //Getting the Row and Column that the blank tile will move to
        int newBlankRow = blankRow + direction.getRowChange();
        int newBlankColumn = blankColumn + direction.getColumnChange();
        //Use a 3 line swap to the location of where the blank tile is to where the blank tile will be
        int temp = newState[blankRow][blankColumn];
        newState[blankRow][blankColumn] = newState[newBlankRow][newBlankColumn];
        newState[newBlankRow][newBlankColumn] = temp;
        return new State(newState, direction, this, newBlankRow, newBlankColumn);      
    }

	/**
	 * Estimates the cost in moves of solving a state.
	 * For each tile (except the blank tile), it finds the row where it
	 * should be (by dividing one less than its value by the width of the
	 * puzzle) and the column where it should be (by modding one less than
	 * its value by the width of the puzzle). Then, it finds the absolute
	 * value of the difference between the row where it is and the row
	 * where it should be and the absolute value of the difference of the
	 * column where it is and the column where it should be and add these
	 * values to the total cost.
	 * @param state state to estimate the cost of
	 * @return estimated cost in moves
	 */
    public static int cost(int[][] state) {
    	//Initializing variables
        int total = 0;
        int updatedRow;
        int updatedColumn;
        int absRow;
        int absColumn;
        //Looping through the state to see where the Row and Column should be
        for(int i = 0; i < state.length; i++) {
            for(int j = 0; j < state[0].length; j++) {
            	//If the tile is not at the length or width of the state then it will calculate the cost
                if(state[i][j] != state.length * state[0].length) {
                	//Dividing the width by the tile - 1 to get the updatedRow 
                    updatedRow = (state[i][j] - 1) / state[0].length;
                    //Modding the width by the tile - 1 to get the updatedColumn 
                    updatedColumn = (state[i][j] - 1) % state[0].length;
                    //Finding the absolute value of the difference between the row and 
                    //column it is from the row and column it should be
                    absRow = Math.abs(i - updatedRow);
                    absColumn = Math.abs(j - updatedColumn);
                    //Adds the absolute values of the rows and columns to a total to get the total cost
                    total += absRow + absColumn;
                }        
            }
        }
        //Returns the cost
        return total;  
    }        
    
    /**x
     * Checks to see whether the state is in a solved position.
     * For any tile, if the row times the size of the puzzle plus the 
     * column plus one is ever *not* equal to its tile value, it's not
     * solved.  Once all of the tiles have been checked and none of them
     * violate this rule, the puzzle must be solved.
     * @return true if the puzzle is solved, false otherwise
     */
    public boolean isSolved() {
    	//Loops through the state to see if it's solved
    	for(int i = 0; i < state.length; ++i) {
    		for(int j = 0; j < state[0].length; ++j) {
    			if((i * state.length + j + 1) != state[i][j]) {
    				return false;
    			}
    		}	
    	}
    	return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * Needed to make PriorityQueue work.
     */
    @Override
    public int compareTo(State other) {
        return cost - other.cost;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     * Also needed to make PriorityQueue work.
     */
    @Override
    public boolean equals(Object object) {
        if( !(object instanceof State) )
            return false;
        
        State other = (State) object;
        if( other.state.length != state.length )
            return false;            
        
        int size = state.length;
        for( int i = 0; i < size; i++ )
            for( int j = 0; j < size; j++ )
                if( state[i][j] != other.state[i][j] )
                    return false;
        
        return true;                
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     * Needed to make hashCode() work.
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for( int i = 0; i < state.length; i++ )
            for( int j = 0; j < state[i].length; j++ )
                output.append(state[i][j] + ",");
        
        return output.toString();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     * Needed to make HashSet work.
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}