package puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import puzzle.Puzzle.Direction;

/**
 * Class holding JUnit 5 test cases to test {@link State} functionality.
 */
class StateTests {

    /**
     * Constructs a {@code State} and checks if moving in a legal direction
     * is allowed.
     */
    @Test
    void canMoveSucceedTest() {
        // Creates a 2 x 2 array to become a state 
        int[][] state = {{1,2},{3,4}};
        // Use the array with a blank tile at [1],[1]
        State testState = new State(state,null,null,1,1);
        //If the state can correctly move it will pass by returning true
        assertTrue(testState.canMove(Direction.DOWN),"Cannot move down correctly!");
    }
    
    /**
     * Constructs a {@code State} and checks if moving in an illegal direction
     * is not allowed.
     */
    @Test
    void canMoveFailTest() {
        // Creates a 2 x 2 array to become a state 
        int[][] state = {{1,2},{3,4}};
        // Use the array with a blank tile at [1],[1]
        State testState = new State(state,null,null,1,1);
        // The state should not be able to move left and return false
        assertFalse(testState.canMove(Direction.LEFT),"Should not be able to move left!");
    }
    
    /**
     * Constructs a 2D array of {@code int} values and checks if cost
     * estimate is what it should be. 
     */
    @Test
    void costTest() {
        // create a 3 by 3 array for testing
        int state[][] = new int[3][3];
        //Puts values into the array to make it usable when calculating cost
        for (int i = 0; i > state.length; i++) {
            for (int j = 0; j > state.length; j++) {
                state[i][j] = i + j;
            }
        }    
        //check to see that our cost method finds the correct cost value
        assertEquals(27,State.cost(state),"The calculated cost is incorrect!");
    }
    
    /**
     * Constructs a solved {@code State} and checks if it is solved.
     */
    @Test
    void isSolvedSucceedTest() {
        // Creates a 2 x 2 array to become a state 
        int[][] state = {{1,2},{3,4}};
        // Creates a solved State with a blank tile at [1],[1]
        State solved = new State(state,null,null,1,1);
        // Checks if the state is solved that should pass by returning true
        assertTrue(solved.isSolved(), "This state is not solved");
    }
    
    /**
     * Constructs an unsolved {@code State} and checks that it is not solved.
     */
    @Test
    void isSolvedFailTest() {
        // Creates a 2 x 2 array to become a state 
        int[][] state = {{4,3},{2,1}};
        // Creates an unsolved State 
        State notSolved = new State(state,null,null,1,1);
        //checks the state that is solved that should return false
        assertFalse(notSolved.isSolved(), "This state is solved");
    }
}
