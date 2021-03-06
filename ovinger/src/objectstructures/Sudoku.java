package objectstructures;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

class Sudoku implements SaveGame{

	private Cell[][] board = new Cell[9][9]; 
	private Stack<State> undoStack;
	private Stack<State> redoStack;
	private String startBoard;
	
	public Sudoku(){
		
		this.undoStack = new Stack<State>();
		this.redoStack = new Stack<State>();
		this.startBoard = "";
		
		for (int i = 0; i <9; i++){
			this.board[i] = new Cell[9];
			for(int j = 0; j<9; j++){
				this.board[i][j] = new Cell();
			}

		}
	}
	
	
	public void clearBoard(){
		this.undoStack = new Stack<State>();
		this.redoStack = new Stack<State>();
		this.startBoard = "";
		
		for (int i = 0; i <9; i++){
			this.board[i] = new Cell[9];
			for(int j = 0; j<9; j++){
				this.board[i][j] = new Cell();
			}
		}
	}
	
	public void importBoard(String importedBoard){
		
		char temp = ' ';
		int value = 0;
		this.startBoard = importedBoard;
		
		for (int i = 0; i <9; i++){
			 
			 for(int j = 0; j<9; j++){
				 temp = importedBoard.charAt(i*9+j);
							 
				 if (temp == '.'){
					 this.board[i][j].setValue(0);
				 }
				 else{
					 value = Character.getNumericValue(temp);
					 this.board[i][j].setReadStatus(true);
					 this.board[i][j].setValue(value);
				 }
			 }
		}
	}
	
	public String toString(){
		
	
		String line = "\n ----------------------------------------------------- \n";
		
		for (int i = 0; i<9; i++){
			line += "| ";
			for (int j = 0; j<9; j++){
				line += this.board[i][j].toString();
				if ((j == 2) || (j == 5)){
					line += " | ";
				}
				else if (j == 8){
					line += " |\n";
				}	
			}
			if ((i == 2) || (i == 5) || (i == 8)){
				line += " ----------------------------------------------------- \n";
			}

			
		}
		return line + "\n";
		
	}
	
	public void setNumber(int number, int row, int col){		
		

		
		if(this.board[row][col].getReadStatus() == false){
			
			int currentValue = this.board[row][col].getValue();
			State currentState = new State(currentValue, row, col);
			this.undoStack.add(currentState);
			
			this.clearConflicts(row, col);
			
			if(number != 0){
				this.isHorizontalConflict(number, row, col);
				this.isVerticalConflict(number, row, col);
				this.isSquareConflict(number, row, col);
			}
			
			this.board[row][col].setValue(number);
		}
		

	}
	
	public void setNumber(State state){		
		
		int value = state.getValue();
		int row = state.getRow();
		int col = state.getCol();
		
		this.clearConflicts(row, col);
		
		if(value != 0){
			this.isHorizontalConflict(value, row, col);
			this.isVerticalConflict(value, row, col);
			this.isSquareConflict(value, row, col);
		}
		
		this.board[row][col].setValue(value);
	}
	
	
	public void getInput(String move){
		
		if(move.equals("import")){
			this.importBoard(".....2..38.273.45....6..87.9.8..5367..6...1..4513..9.8.84..3....79.512.62..8......");
		}
		else if(move.equals("clear")){
			this.clearBoard();
		}
		else if(move.equals("save")){
			this.saveGame("MySave2");
		}
		else if(move.equals("load")){
			this.loadGame("MySave2");
		}
		else if(move.equals("undo")){
			this.undoMove();
		}
		else if(move.equals("redo")){
			this.redoMove();
		}
		else{
			int value = Character.getNumericValue(move.charAt(0));
			int row = Character.getNumericValue(move.charAt(1));
			int col = Character.getNumericValue(move.charAt(2));
			
			if (value == 0){
				this.clearConflicts(row, col);
				if(this.board[row][col].getReadStatus() == false){
					this.board[row][col].setValue(value);
				}
			}
			else{
				this.setNumber(value, row, col);
			}

		}
		
		
	}
	
	public void setConflict(int inputRow, int inputCol, int row, int col){
		
		this.board[inputRow][inputCol].setConflictStatus(true);
		this.board[row][col].setConflictStatus(true);
		this.board[inputRow][inputCol].addToConflictList(row, col);
	}
		 
	public boolean isHorizontalConflict(int number, int row, int col){
		
		boolean conflict = false;
		
		if(number == 0){
			return false;
		}
		
		for (int c = 0; c < 9; c++){
			
			if (c == col){
				continue;
			}
			
			if (this.board[row][c].getValue() == number){
				this.setConflict(row, col, row, c);
				conflict = true;
			}
			
		}
		
		return conflict;
	}
	
	
	public boolean isVerticalConflict(int number, int row, int col){
		
		if(number == 0){
			return false;
		}
		
		boolean conflict = false;
		
		for (int r = 0; r < 9; r++){
			
			if (r == row){
				continue;
			}
			
			if (this.board[r][col].getValue() == number){
				this.setConflict(row, col, r, col);
				conflict = true;
			}
			
		}
		
		return conflict;
	}
	
	public boolean isSquareConflict(int number, int row, int col){
		
		if(number == 0){
			return false;
		}
		
		int squareRow = row - row % 3;
		int squareCol = col - col % 3;
		
		boolean conflict = false;
		
		for(int r = 0; r<3; r++){
			for(int c = 0; c<3; c++){
				if(r == row && c == col){
					continue;
				}
				if (this.board[squareRow + r][squareCol + c].getValue() == number){
					this.setConflict(row, col, squareRow + r, squareCol + c);
					conflict = true;
				}
				
				
			}
		}
		
		return conflict;
	}
	
	public void clearConflicts(int row, int col){
		
		ArrayList<Integer> rowList = this.board[row][col].getRowList();
		ArrayList<Integer> colList = this.board[row][col].getColList();
			
		int numberOfConflicts = rowList.size();
		
		int r = 0;
		int c = 0;
		
		for (int i = 0; i < numberOfConflicts; i++){
			r = rowList.get(i);
			c = colList.get(i);
			this.board[r][c].setConflictStatus(false);
		}
		
		
	}

	public void undoMove(){
		
		if(this.undoStack.size() > 0){
			State state = undoStack.pop();
			
			int row = state.getRow();
			int col = state.getCol();
			int currentValue = this.board[row][col].getValue();
			
			State currentState = new State(currentValue, row, col);
			
			this.redoStack.add(currentState);
			
			this.setNumber(state);
		}
		

	}
	
	public void redoMove(){
		
		if(this.redoStack.size() > 0){
			State state = redoStack.pop();
			
			int row = state.getRow();
			int col = state.getCol();
			int currentValue = this.board[row][col].getValue();
			
			State currentState = new State(currentValue, row, col);
			
			this.undoStack.add(currentState);
			
			this.setNumber(state);
		}

	}
	
	@Override
	public void saveGame(String id) {

		
		String line = "";
		
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				if (this.board[i][j].getValue() == 0){
					line += '.';
				}
				else{
					line += Integer.toString(this.board[i][j].getValue());
				}
			}
		}
		
        try {
            FileWriter writer = new FileWriter(id);
            writer.write(line);
            writer.write("\r\n\r");
            writer.write(this.startBoard);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}


	@Override
	public void loadGame(String id) {

	
		Scanner in;
        try
        {
            in = new Scanner(new FileReader(id));
            String currentBoard = "";
            String startBoard = "";
            int i = 0;
            
            while(in.hasNext()){
                String line = in.nextLine();
                System.out.println(line);
                if (i == 0){
                	currentBoard = line;
                }
                else{
                	startBoard = line;

                }
                i++;
            }
             
            in.close();
            
        	this.clearBoard();
        	this.importBoard(startBoard);
        	
        	char charValue;
        	int value;
        	
        	for ( int r = 0; r < 9; r++){
        		for (int c = 0; c < 9; c++){
            		charValue = currentBoard.charAt(r*9 + c);
            		value = Character.getNumericValue(charValue);
            		if (value > 0){
                		this.setNumber(value, r, c);
            		}

        		}

        	}
        	
        	
            
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error: file could not be opened. Does it exist?");
            System.exit(1);
        }

	}
	
	
	public static void main(String[] args){
		
	
		Sudoku myBoard = new Sudoku();
		
		myBoard.undoMove();
		System.out.println(myBoard.toString());
		
		myBoard.setNumber(4, 0, 0);
		System.out.println(myBoard.toString());
				
		myBoard.undoMove();
		System.out.println(myBoard.toString());
		
		myBoard.redoMove();
		System.out.println(myBoard.toString());
		
		myBoard.setNumber(2, 1, 1);
		System.out.println(myBoard.toString());
		
		myBoard.setNumber(0, 1, 1);
		System.out.println(myBoard.toString());
		
		myBoard.setNumber(8, 4, 1);
		System.out.println(myBoard.toString());

		myBoard.getInput("import");
		System.out.println(myBoard.toString());
		
		myBoard.saveGame("MySave3");
		
		myBoard.clearBoard();
		System.out.println(myBoard.toString());

		
		myBoard.loadGame("MySave3");
		System.out.println(myBoard.toString());
		


		
		
		


	}



	
	
}


