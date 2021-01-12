import java.util.ArrayList;

public class Board {
	public int[][] blocks;
	public int heuristicValue;
	private Board parent = null;
	private int spacePositionRow;
	private int spacePositionCol;
	private int level = 0;
	
	public Board(int[][] blocks) // construct a board from an N-by-N array of blocks, (where blocks[i][j] = block in row i, column j)
	{
		this.setBlocks(blocks);
		this.setSpacePosition();
	}
	
	public void setParent(Board parent)
	{
		this.parent = parent;
	}
	
	public Board getParent()
	{
		return this.parent;
	}
	
	public void incrementLevel()
	{
		this.level++;
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	public void setBlocks(int[][] blocks)
	{
		this.blocks = blocks;
	}
	
	public void setSpacePosition()
	{
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(blocks[i][j] == 0)
				{
					this.spacePositionRow = i;
					this.spacePositionCol = j;
					
					return;
				}
			}
		}
	}
	
	public int[][] getGoalBlocks()
	{
		int[][] goalBlocks = {
			{1, 2, 3},
			{4, 5, 6},
			{7, 8, 0}
		};
		
		return goalBlocks;
	}
	
	public void updateHeuristicValue()
	{
		this.heuristicValue = this.getHeuristicValue();
	}
	
	// heuristic Function to calculate heuristic value f(x) = h(x) + g(x)
	
	public int getHeuristicValue()
	{
		return this.hammingDistance() + this.level;
	}
	
	public int hammingDistance()
	{
		int distanceSum = 0;
		
		int[][] coordinatesGoalState = new int[9][2];
		int[][] coordinatesCurrentState = new int[9][2];
		
		int[][] goalBlocks = this.getGoalBlocks();
		
		int counter = 1;
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(goalBlocks[i][j] != 0)
				{	
					coordinatesGoalState[counter][0] = i;
					coordinatesGoalState[counter][1] = j;
					
					counter++;
				}
			}
		}
		
		int value = 0;
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				value = this.blocks[i][j];
				
				if(value != 0)
				{
					coordinatesCurrentState[value][0] = i;
					coordinatesCurrentState[value][1] = j;
				}
			
			}
		}
	
		for(int i = 1; i <= 8; i++)
		{
			distanceSum += (Math.abs(coordinatesCurrentState[i][0] - coordinatesGoalState[i][0]) + Math.abs(coordinatesCurrentState[i][1] - coordinatesGoalState[i][1]));
		}
		
		return distanceSum;
	}
	
	public boolean canSpaceMoveLeft()
	{
		return (this.spacePositionCol > 0);
	};
	
	public boolean canSpaceMoveRight()
	{
		return (this.spacePositionCol < 2);
	};
	
	public boolean canSpaceMoveUp()
	{
		return (this.spacePositionRow > 0);
	}
	
	public boolean canSpaceMoveDown()
	{
		return (this.spacePositionRow < 2);
	}
	
	public void moveSpaceLeft()
	{
		int tempValue = this.blocks[this.spacePositionRow][this.spacePositionCol - 1];
		
		this.blocks[this.spacePositionRow][this.spacePositionCol] = tempValue;
		this.blocks[this.spacePositionRow][this.spacePositionCol - 1] = 0;
		
		this.spacePositionCol -= 1;
	}
	
	public void moveSpaceRight()
	{
		int tempValue = this.blocks[this.spacePositionRow][this.spacePositionCol + 1];
		
		this.blocks[this.spacePositionRow][this.spacePositionCol] = tempValue;
		this.blocks[this.spacePositionRow][this.spacePositionCol + 1] = 0;
		
		this.spacePositionCol += 1;
	}
	
	public void moveSpaceUp()
	{
		int tempValue = this.blocks[this.spacePositionRow - 1][this.spacePositionCol];
		
		this.blocks[this.spacePositionRow][this.spacePositionCol] = tempValue;
		this.blocks[this.spacePositionRow - 1][this.spacePositionCol] = 0;
		
		this.spacePositionRow -= 1;
	}
	
	public void moveSpaceDown()
	{
		int tempValue = this.blocks[this.spacePositionRow + 1][this.spacePositionCol];
		
		this.blocks[this.spacePositionRow][this.spacePositionCol] = tempValue;
		this.blocks[this.spacePositionRow + 1][this.spacePositionCol] = 0;
		
		this.spacePositionRow += 1;
	}
	
	public Board clone()
	{
		Board boardClone = new Board(this.getBlocksClone());
		
		boardClone.level = this.getLevel();
		boardClone.parent = this.parent;
		
		return boardClone;
	}
    
	public int size() // board size N
	{
		return blocks.length;
	}
	
	public boolean isGoal() // is this board the goal board?
	{
		int[][] goalBlocks = this.getGoalBlocks();
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(this.blocks[i][j] != goalBlocks[i][j])
					return false;
			}
		}
		
		return true;
	}
	
	public boolean isSolvable() // is the board solvable?
	{
		int inversionsCount = 0;
		
		// converting to single dimensional array
		
		ArrayList<Integer> singleDimBlocks = new ArrayList<Integer>();
		
        for (int i = 0; i < this.blocks.length; i++)
        {
            for (int j = 0; j < this.blocks[i].length; j++)
            {
               singleDimBlocks.add(this.blocks[i][j]);
            }
        }
		
		for (int i = 0; i < 9 - 1; i++)
		{
	        for (int j = i + 1; j < 9; j++)
	        {
	             if (singleDimBlocks.get(i) != 0 && singleDimBlocks.get(j) != 0 &&  singleDimBlocks.get(i) > singleDimBlocks.get(j))
	            	inversionsCount++;
	        }
	    }
	    
		return (inversionsCount % 2 == 0);
	}
	
	public boolean equals(Board y) // does this board equal y?
	{
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(this.blocks[i][j] != y.blocks[i][j])
					return false;
			}
		}
		
		return true;
	}
	
	public int[][] getBlocksClone()
	{
		int[][] newBlocks = new int[3][3];
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				newBlocks[i][j] = this.blocks[i][j];
			}
		}
		
		return newBlocks;
	}

	public ArrayList<Board> children() // all children boards
	{
		ArrayList <Board> children = new ArrayList <Board>();
		
		if(this.canSpaceMoveLeft())
		{
			Board leftMoveBoard = this.clone();
			
			leftMoveBoard.moveSpaceLeft();
			leftMoveBoard.setParent(this);
			leftMoveBoard.incrementLevel();
			
			children.add(leftMoveBoard);
		}
		
		if(this.canSpaceMoveRight())
		{
			Board rightMoveBoard = this.clone();
			
			rightMoveBoard.moveSpaceRight();
			rightMoveBoard.setParent(this);
			rightMoveBoard.incrementLevel();
			
			children.add(rightMoveBoard);
		}
		
		if(this.canSpaceMoveUp())
		{
			Board upMoveBoard = this.clone();
			
			upMoveBoard.moveSpaceUp();
			upMoveBoard.setParent(this);
			upMoveBoard.incrementLevel();
			
			children.add(upMoveBoard);
		}
		
		if(this.canSpaceMoveDown())
		{
			Board downMoveBoard = this.clone();
			
			downMoveBoard.moveSpaceDown();
			downMoveBoard.setParent(this);
			downMoveBoard.incrementLevel();
			
			children.add(downMoveBoard);
		}
		
		return children;
	}
	
	public String toString() // string representation of the board (in the output format specified below)
	{
		String str = "";
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				str += blocks[i][j] + " ";
			}
			
			str += "\n";
		}
		
		str += "\n";
		
		return str;
	}
}
