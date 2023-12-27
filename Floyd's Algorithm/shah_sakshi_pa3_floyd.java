import java.util.Random;

public class shah_sakshi_pa3_floyd {
    
    // Prints the matrix P that stores intermediate vertices of shortest paths
    static void printP(int matrix[][], int n) {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Prints all pairs shortest paths
    static void printAllPairsShortestPaths(int dist[][], int P[][], int n) {
        for (int i = 0; i < n; ++i) {
            System.out.print("V" + (i+1) + "-Vj shortest path and length\n"); // print a header for the paths starting from vertex i
            for (int j = 0; j < n; ++j) {
                System.out.print("V" + (i+1) + " ");
                printShortestPath(i, j, P); // print the shortest path from vertex i to vertex j
                System.out.print("V" + (j+1) + ": " + dist[i][j]); // print the length of the shortest path from vertex i to vertex j
                System.out.print("\n");   
            }
        }
    }

     // Prints the shortest path from q to r
    static void printShortestPath(int q, int r, int[][] P) {
        if (P[q][r] != 0) { // if there is an intermediate vertex between q and r
            printShortestPath(q, P[q][r], P); // print the shortest path from q to the intermediate vertex
            System.out.print("V" + (P[q][r]+1) + " "); // print the intermediate vertex
            printShortestPath(P[q][r], r, P); // print the shortest path from the intermediate vertex to r
        } else {
            return; // not intermediate nodes i.e if q and r are directly connected, return
        }
    }
    
    // Prints the given matrix
    static void printMatrix(int matrix[][], int n) {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Randomly select n between 5 and 10
        Random rand = new Random();
        int n = rand.nextInt(6) + 5; // 5 <= n <= 10
        // int n = 5; //rand.nextInt(1) + 3; // Generate a random integer between 5 and 10
        System.out.println("Selected n value: " + n);
        
        // Create an n x n adjacency matrix A and randomly assign weights
          int[][] A = new int[n][n];
    

                // int[][] A = {
                //     {0, 5, 4, 8, 10, 7},
                //     {5, 0, 1, 8, 2, 7},
                //     {4, 1, 0, 10, 1, 7},
                //     {8, 8, 10, 0, 8, 2},
                //     {10, 2, 1, 8, 0, 2},
                //     {7, 7, 7, 2, 2, 0}
                // };
        int[][] P = new int[n][n];

     
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (i == j) {
                    A[i][j] = 0; // No loops in an undirected graph
                } else {
                    int weight = rand.nextInt(10) + 1; // Generate a random weight between 1 and 10
                    A[i][j] = weight;
                    A[j][i] = weight; // Undirected graph, so A[i,j] = A[j,i]
                }
            }
        }
        System.out.println("Amatrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }
        
        // Find all pairs shortest paths using Floyd's algorithm


        int[][] D = new int[n][n]; // Initialize matrix D with the weights in matrix A
     
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                D[i][j] = A[i][j]; // Initialize D with the weights in A
                P[i][j] = 0; // Initialize matrix P with all zeroes
            }
        }


        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                // If there is a path between vertex i and k and a path between vertex k and j
                    if(D[i][k] !=0 &&  D[k][j] != 0)
                    {
                       // If the current path from i to j is greater than the sum of the path from i to k and path from k to j 
                        if (D[i][j] > D[i][k] + D[k][j]) {
                            D[i][j] = D[i][k] + D[k][j]; // Update D with a shorter path
                            P[i][j] = k; // Update matrix P with the intermediate vertex k in the path from i to j
                        }
                    }
                }
            }
        }
        
        // Print all pairs shortest paths and their lengths
         System.out.println("Dmatrix:");
         printMatrix(D, n);

        System.out.println("Pmatrix:");
        printP(P, n);

        printAllPairsShortestPaths(D,P,n);

    }
    
}

//time complexity : O(n^3)
//space complexity : O(n^2)
