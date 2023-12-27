import java.util.*;

public class Program4 {

    public static void main(String[] args) {
        Random rand = new Random();
        int n = rand.nextInt(6) + 5;

        // int n = 8;

        // int n = 10;

        System.out.println("randomly selected " + n + " vertices");

        int[][] adjacencyMatrix = generateAdjacencyMatrix(n);

        // int[][] adjacencyMatrix = { { 0, 8, 10, 4, 10, 8, 3, 10 },
        //         { 5, 0, 1, 10, 3, 9, 4, 5 },
        //         { 8, 1, 0, 4, 10, 2, 5, 3 },
        //         { 3, 1, 1, 0, 6, 7, 1, 8 },
        //         { 2, 10, 9, 3, 0, 5, 3, 5 },
        //         { 2, 1, 3, 8, 2, 0, 3, 3 },
        //         { 10, 3, 9, 10, 8, 1, 0, 3 },
        //         { 4, 4, 8, 7, 3, 1, 6, 0 } };

        displayMatrix(adjacencyMatrix);

        Scanner input = new Scanner(System.in);
        System.out.println("select the algorithm: ");

        String choice = input.next();

        switch (choice) {
            case "prim":
                prim(adjacencyMatrix);
                break;
            case "kruskal":
                kruskal(adjacencyMatrix);
                break;
            default:
                System.out.println("Error: Invalid choice");
                break;
        }

        input.close();

    }

    // function to generate an adjacency matrix for an undirected complete graph
    public static int[][] generateAdjacencyMatrix(int n) {
        int[][] adjacencyMatrix = new int[n][n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int weight = rand.nextInt(10) + 1;
                adjacencyMatrix[i][j] = weight;
                adjacencyMatrix[j][i] = weight;
            }
        }
        return adjacencyMatrix;
    }

    // function to display the adjacency matrix
    public static void displayMatrix(int[][] matrix) {
        System.out.println("random matrix:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // function to implement Prim's algorithm using a priority queue
    public static void prim(int[][] adjacencyMatrix) {
        int n = adjacencyMatrix.length;

        int[][] primMatrix = new int[n][n];
        boolean[] visited = new boolean[n]; // an array to keep track of which vertices have been visited.
        int[] cost = new int[n]; // an array to keep track of the minimum cost to connect each vertex to the
                                 // minimum spanning tree
        int[] prev = new int[n]; // array to keep track of the previous vertex in the path to the minimum
                                 // spanning tree
        int[] pq = new int[n]; // array-based priority queue
        int pqSize = 0; // size of priority queue

        // initialize cost and prev arrays
        for (int i = 0; i < n; i++) {
            cost[i] = Integer.MAX_VALUE;
            prev[i] = -1;
        }

        // add first vertex to MST and update cost and prev arrays
        cost[0] = 0;
        prev[0] = 0;
        pq[0] = 0;
        pqSize++;

        // add remaining vertices to MST
        while (pqSize > 0) {
            // extract minimum element from priority queue
            int u = pq[0];
            visited[u] = true;

            // remove minimum element from priority queue
            pqSize--;
            pq[0] = pq[pqSize];

            // percolate down to restore heap property
            int i = 0;

            // he while loop continues until the left child of i is within the bounds of the
            // priority queue.
            // This condition ensures that the current node has at least one child.
            while (2 * i + 1 < pqSize) {
                int leftChild = 2 * i + 1;
                int rightChild = 2 * i + 2;
                int minChild = leftChild; // minChild is initially set to leftChild, assuming that it is the minimum
                                          // child

                // If the right child exists and has a lower cost than the left child, minChild
                // is updated to be the right child
                if (rightChild < pqSize && cost[pq[rightChild]] < cost[pq[leftChild]]) {
                    minChild = rightChild;
                }

                // If the cost of the minimum child is less than the cost of the current node, a
                // swap is performed between
                // the current node and the minimum child in the priority queue.
                // The index i is updated to the index of the minimum child to continue the
                // percolate down process.
                // If the cost of the minimum child is greater than or equal to the cost of the
                // current node, the heap property is restored, and the loop is terminated
                if (cost[pq[minChild]] < cost[pq[i]]) {
                    int temp = pq[i];
                    pq[i] = pq[minChild];
                    pq[minChild] = temp;
                    i = minChild;
                } else {
                    break;
                }
            }

            // update priority queue and cost array for adjacent vertices
            for (int v = 0; v < n; v++) {
                if (adjacencyMatrix[u][v] != 0 && !visited[v])
                // Check if there is an edge between u and v and v is not already visited
                // If the cost to reach v from u is less than the current cost[v], update
                // cost[v] and prev[v]
                {
                    if (adjacencyMatrix[u][v] < cost[v]) {
                        cost[v] = adjacencyMatrix[u][v];
                        prev[v] = u;

                        // if v is already in priority queue, update its position
                        boolean found = false;
                        int j = 0;
                        for (; j < pqSize; j++) {
                            if (pq[j] == v) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            // Move v up the heap if its cost is less than its parent's cost
                            while (j > 0 && cost[pq[(j - 1) / 2]] > cost[v]) {
                                pq[j] = pq[(j - 1) / 2];
                                j = (j - 1) / 2;
                            }
                            pq[j] = v;
                        } else { // otherwise, add v to priority queue
                            pq[pqSize] = v;
                            j = pqSize;
                            pqSize++;
                            // Move v up the heap if its cost is less than its parent's cost
                            while (j > 0 && cost[pq[(j - 1) / 2]] > cost[v]) {
                                pq[j] = pq[(j - 1) / 2];
                                j = (j - 1) / 2;
                            }
                            pq[j] = v;
                        }
                    }
                }
            }
        }

        // construct prim's matrix
        for (int i = 0; i < n; i++) {
            if (prev[i] != -1) {
                primMatrix[i][prev[i]] = cost[i];
                primMatrix[prev[i]][i] = cost[i];
            }
        }

        System.out.println("Prim's matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(String.format("%2d ", primMatrix[i][j]));
            }
            System.out.println();
        }

        // display prim's matrix
        System.out.println("Prim's MST:");
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (primMatrix[i][j] != 0) {
                    System.out.println("V" + (i + 1) + "-V" + (j + 1) + ": " + primMatrix[i][j]);
                }
            }
        }
    }

    // function to implement Kruskal's algorithm using the find3() and union3()
    // functions
    public static void kruskal(int[][] adjacencyMatrix) {
        int n = adjacencyMatrix.length;

        List<Edge> edges = new ArrayList<Edge>(); // Create a list of all edges in the graph

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adjacencyMatrix[i][j] != 0) {
                    edges.add(new Edge(i, j, adjacencyMatrix[i][j]));
                }
            }
        }

        Collections.sort(edges); // Sort the list of edges by weight

        List<Edge> mst = new ArrayList<Edge>(); // an empty list to hold the edges of the minimum spanning tree
        int[][] mstMatrix = new int[n][n];

        int[] parent = new int[n]; // an array to keep track of the parent of each vertex in the disjoint sets
        int[] height = new int[n];

        // Initialize each vertex to be the root of its own disjoint set
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            height[i] = 0;
        }

        int edgeCount = 0;

        // Iterate over the sorted edges, adding them to the minimum spanning tree if
        // they do not create a cycle
        for (Edge e : edges) {
            int u = e.from;
            int v = e.to;

            // Find the root of the set containing u and v
            int ucomp = find3(parent, u);
            ucomp = find3(parent, ucomp); // update ucomp to the root of the set

            int vcomp = find3(parent, v);
            vcomp = find3(parent, vcomp); // update vcomp to the root of the set

            // If u and v are not already in the same set, add the edge to the minimum
            // spanning tree
            if (ucomp != vcomp) {
                mst.add(e);

                mstMatrix[u][v] = e.weight;
                mstMatrix[v][u] = e.weight;

                // Merge the sets containing u and v
                union3(parent, height, ucomp, vcomp);
                edgeCount++;
            }

            // If the number of edges in the minimum spanning tree is n-1, we have found the
            // minimum spanning tree
            if (edgeCount == n - 1) {
                break;
            }
        }

        // Print the adjacency matrix of the minimum spanning tree
        System.out.println("Kruskal's matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(mstMatrix[i][j] + " ");
            }
            System.out.println();
        }

        // System.out.println("Kruskal's MST cost: " + mstCost);

        // If the minimum spanning tree has less than n-1 edges, the graph is not
        // connected
        if (mst.size() < n - 1) {
            System.out.println("Error: Graph is not connected");
        } else {
            // Print the edges of the minimum spanning tree
            System.out.println("Kruskal's MST:");
            for (Edge e : mst) {
                System.out.println("V" + (e.from + 1) + "-V" + (e.to + 1) + ": " + e.weight);
            }
        }
    }

    // find the root of a node in a union-find data structure
    public static int find3(int[] parent, int i) {
        // If the parent of vertex 'i' is itself, return 'i'
        if (parent[i] == i) {
            return i;
        }
        // If the parent of vertex 'i' is not itself, call the find3 method recursively
        // with parent[i]
        else {
            parent[i] = find3(parent, parent[i]);
            return parent[i];
        }
    }

    // union two sets in a union-find data structure
    public static void union3(int[] parent, int[] height, int i, int j) {

        // repx and repy are set to the representative elements (i.e., roots) of the
        // sets containing elements i and j, respectively.
        // The find3 method is used to find these representatives.

        int repx = find3(parent, i);
        int repy = find3(parent, j);

        // The heights of the two sets are compared, and the smaller one is merged into
        // the larger one.
        if (height[repx] == height[repy]) {
            height[repx]++;
            parent[repy] = repx;
        } else if (height[repx] > height[repy]) {
            parent[repy] = repx;
        } else {
            parent[repx] = repy;
        }
    }

    // class to represent an edge with a weight
    static class Edge implements Comparable<Edge> {
        int from; // store the index of the source vertex.
        int to; // store the index of the destination vertex
        int weight; // store the weight of the edge

        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
            // Returns the result of the comparison between the weight attribute of this
            // object and the weight attribute of the other object using Integer.compare()
            // method
        }
    }

}
