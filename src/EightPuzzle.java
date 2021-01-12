import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

public class EightPuzzle {
	
	private int[][] inputBlocks = new int[3][3];
	
	private ArrayList<Board> openList = new ArrayList<Board>();
	private ArrayList<Board> closeList = new ArrayList<Board>();
	
	public void takeInput()
	{
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				System.out.println("Enter number for [" + i + "," + j + "]: ");
				inputBlocks[i][j] = reader.nextInt();
			}
		}

		reader.close();
	}
	
	public int getBoardIndexWithLowestHeruisticValueFromOpenList()
	{
		int tempValue;
		int currentHeuristicValue;
		int index;
		
		index = 0;
		
		currentHeuristicValue = openList.get(index).getHeuristicValue();
		
		for(int i = 0; i < openList.size(); i++)
		{
			tempValue = openList.get(i).getHeuristicValue();
			
			if(tempValue < currentHeuristicValue)
			{
				currentHeuristicValue = tempValue;
				index = i;
			}
		}
		
		return index;
	}
	
	public void addToCloseList(Board board)
	{
		this.closeList.add(board);
	}
	
	private void pushBoardsToOpenList(ArrayList<Board> boardList)
	{
		for(int i = 0; i < boardList.size(); i++)
			openList.add(boardList.get(i));
	}
	
	private void showResults(Board finalBoard)
	{
		ArrayList<Board> steps = new ArrayList<Board>();
		
		Board currentBoard = finalBoard;
		
		while(currentBoard.getParent() != null)
		{
			steps.add(currentBoard);
			currentBoard = currentBoard.getParent();
		}
		
		for(int i = (steps.size() - 1); i >= 0; i--)
			System.out.print(steps.get(i));
	}
	
	public void start()
	{
		this.takeInput();
		
		// after taking inputs create main node
		
		Board board = new Board(inputBlocks);
		
		if(!board.isSolvable()) {
			System.out.print("Not Solvable");
			return;
		}
		
		System.out.println("Initial State\n");
		System.out.println(board);
		System.out.println("Calculating...\n");
		
		int index = 0;
		this.openList.add(board);

		while(!board.isGoal())
		{	
			this.pushBoardsToOpenList(board.children());
			
			this.addToCloseList(board);
			this.openList.remove(index);
			
			index = this.getBoardIndexWithLowestHeruisticValueFromOpenList();
			
			board = this.openList.get(index);
		}
		
		this.showResults(board);
	}

	public static void main(String[] args) {
		
		EightPuzzle program = new EightPuzzle();
		
		program.start();
	}

}
