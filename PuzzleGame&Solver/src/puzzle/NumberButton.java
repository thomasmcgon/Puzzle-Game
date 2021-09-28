package puzzle;

import javax.swing.JButton;

/**
 * Class to represent a button with a number on it.
 */
@SuppressWarnings("serial")
public class NumberButton extends JButton {	
	private int number;
	private final int row;
	private final int column;
	
	/**
	 * Constructor to create a button in a given row and column with a given
	 * number.
	 * @param number number assigned to button
	 * @param row row of button
	 * @param column column of button
	 */
	public NumberButton(int number, int row, int column) {
		setNumber(number);
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Accessor for row of button.
	 * @return row of button
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Accessor for column of button.
	 * @return column of button
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Accessor for number of button.
	 * @return number of button
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Mutator that changes the number of the button and updates its label.
	 * The label will be set as blank if the number is 0 and as the number
	 * itself otherwise.  Also, the button will be set to enabled if the
	 * number is anything other than 0 and set to disabled otherwise.
	 * @param number number to change to
	 */
	public void setNumber(int number) {
		this.number = number;
		if( number == 0 )
			setText("");
		else
			setText("" + number);
		setEnabled(number != 0);		
	}
	
	/**
	 * Determines whether the corresponding tile is empty. 
	 * @return true if the number is 0, false otherwise
	 */
	public boolean isEmpty() {
		return number == 0;
	}	
}
