import java.util.*;

public class Program {
	
	private String[][] matrix;

    public Program(int size, int x, int y) {
		int n = 1;
        n = size;

		// matrix size is a perfect power of 2.
		matrix = new String[n][n];
		//2D array of Strings, where each element represents a square in the grid

		// Fill in the matrix with all empty squares.
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				matrix[i][j] = "--"; 
			}
		}		
		
		// This represents the missing tile in the tromino.
		matrix[n-1-y][x] = "MS";
    }

    void fillBaseTromino(int x_board, int y_board)
    {
        /*
        The program recursively divides the grid into quadrants until the base case of a 
        2x2 grid is reached, at which point a base tromino is filled in 
        */
        
        int i=x_board;
        int j=y_board;

        String tile = "";
        for(i=x_board; i<x_board + 2; i++)
        {
            for(j=y_board; j<y_board + 2; j++)
            {
                if(matrix[i][j] != "--")
                {
                    if(i == x_board && j == y_board)
                    {
                        tile = "LR";
                        break;
                    }
                    if(i == x_board+1 && j == y_board)
                    {
                        tile = "UR";
                        break;
                    }if(i == x_board && j == y_board+1)
                    {
                        tile = "LL";
                        break;
                    }if(i == x_board+1 && j == y_board+1)
                    {
                        tile = "UL";
                        break;
                    }
                }
            }
        }
        
        int x = x_board;
        while (x < x_board + 2) {
            int y = y_board;
            while (y < y_board + 2) {
                if (matrix[x][y] == "--") {
                    matrix[x][y] = tile;
                }
                y++;
            }
            x++;
        }
    }
	
	// Wrapper call for recursive method.
	public void trominoTile(int x, int y) {
		tromino(matrix.length, 0, 0, x, y);
	}
	
    private void tromino(int size, int x_board, int y_board, int x_missing, int y_missing) {
	    // Base case: size of matrix is 2x2
	    if (size == 2) {

	        fillBaseTromino(x_board, y_board);
            //print();

	    } else {

	        // Divide the matrix into four quadrants
	        int half_size = size / 2;
	        int x_center = x_board + half_size;
	        int y_center = y_board + half_size;

	        // Determine which quadrant the missing square is in
	        boolean ul = (x_missing < x_center && y_missing < y_center);
	        boolean ur = (x_missing < x_center && y_missing >= y_center);
	        boolean ll = (x_missing >= x_center && y_missing < y_center);
	        boolean lr = (x_missing >= x_center && y_missing >= y_center);

	        // Fill in the middle tromino
	        if (ul) {
	            matrix[x_center - 1][y_center] = "LR";
	            matrix[x_center][y_center] = "LR";
	            matrix[x_center][y_center-1] = "LR";
	        } else if (ur) {
	            matrix[x_center - 1][y_center - 1] = "LL";
	            matrix[x_center][y_center] = "LL";
	            matrix[x_center][y_center - 1] = "LL";
	        } else if (ll) {
	            matrix[x_center][y_center] = "UR";
	            matrix[x_center - 1][y_center-1] = "UR";
	            matrix[x_center-1][y_center] = "UR";
	        } else { // lr
	            matrix[x_center - 1][y_center - 1] = "UL";
	            matrix[x_center - 1][y_center] = "UL";
	            matrix[x_center][y_center - 1] = "UL";
	        }

            //print();
	        // Recursively call tromino on each quadrant
	        if (ul) {
	            tromino(half_size, x_board, y_board, x_missing, y_missing);
	            tromino(half_size, x_board, y_center, x_center - 1, y_center);
	            tromino(half_size, x_center, y_board, x_center, y_center - 1);
	            tromino(half_size, x_center, y_center, x_center, y_center);
	        } else if (ur) { 
                //System.out.println("Normal"+x_missing+" "+y_missing+" "+x_board+" "+y_board);

	            tromino(half_size, x_board, y_center, x_missing, y_missing);
	            tromino(half_size, x_board, y_board, x_center-1, y_center - 1);
	            tromino(half_size, x_center, y_board, x_center, y_center - 1);
	            tromino(half_size, x_center, y_center, x_center, y_center);
	        } else if (ll) {
	            tromino(half_size, x_center, y_board, x_missing, y_missing); //x_center, y_center - 1);
	            tromino(half_size, x_board, y_board, x_center - 1, y_center-1);
	            tromino(half_size, x_board, y_center, x_center - 1, y_center);
	            tromino(half_size, x_center, y_center, x_center, y_center);
	        } else { // lr
	            tromino(half_size, x_center, y_center, x_missing, y_missing);
	            tromino(half_size, x_board, y_board, x_center-1, y_center-1);
	            tromino(half_size, x_board, y_center, x_center-1, y_center);
	            tromino(half_size, x_center, y_board, x_center, y_center-1);
	        }
	    }
	}
	
	// Prints out the current object.
	public void print() {
		
		for (int i=0; i<matrix.length; i++) {
			for (int j=0; j<matrix[i].length; j++)
				System.out.print(matrix[i][j]+" ");
			System.out.println();
		}
        System.out.println();
	}
	
	public static void main(String[] args) {

        int size = -1;

        Scanner scanner = new Scanner(System.in);

        while(size != 0) {
            System.out.println("Please enter size of board (need to be 2^n and 0 to quit): ");
            size = scanner.nextInt();

			if(size == 0){
				System.exit(0);
			}
			
            if(size == 1) {
				System.out.println("board size should greater than 1");
                continue;   
            }
            
            if(!((size & (size - 1)) == 0)) {
            	System.out.println("The input board size should be a power of 2");
            	continue;
            }


            System.out.println("Please enter coordinates of missing square (separate by a space):");

            int x = scanner.nextInt();
            int y = scanner.nextInt();
            
            if(x >= size || y>= size) {
            	System.out.println("Missing hole's x co-ordinate or y co-ordinate is out of bounds, please enter correct values");
            	continue;
            }

            // Create our object and tile it!
            Program matrix = new Program(size, x, y);
            matrix.trominoTile(size-1-y , x);

            // Print out the trominoed matrix.
            matrix.print();
        }
        
        scanner.close();
    }
}
