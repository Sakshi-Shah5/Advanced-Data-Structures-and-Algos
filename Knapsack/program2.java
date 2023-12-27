import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class program2 {

    private static int[][] dpTable;
    private static int[] weights;   //stores the weights of the items
    private static int[] profits;   //stores the profits of the items
    private static ArrayList<Integer> selectedItems = new ArrayList<Integer>(); //used to keep track of the items that are selected for the optimal solution

    public static void main(String[] args) {
        String inputFile = "";
        if (args.length > 1 && args[0].equals("-k")) {
            inputFile = args[1];
        } else {
            System.out.println("Please provide input file.");
            System.exit(0);
        }
        File file = new File(inputFile);
        try {
            Scanner sc = new Scanner(file);
            int numItems = sc.nextInt();   // read number of items from input file
            int capacity = sc.nextInt();   //read capacity of knapsack from input file
            dpTable = new int[numItems + 1][capacity + 1];   //initialize dpTable 
            weights = new int[numItems + 1];
            profits = new int[numItems + 1];
            for (int i = 1; i <= numItems; i++) {
                String itemName = sc.next();
                profits[i] = sc.nextInt();
                weights[i] = sc.nextInt();
            }
            sc.close();

            FileWriter entriesFileWriter = new FileWriter("entries2.txt");   //FileWriter object to write entries
            FileWriter outputFileWriter = new FileWriter("output2.txt");  //FileWriter object to write output

            selectedItems.clear();
            // Calculate optimal solution
            int optimalProfit = knapsack(numItems, capacity);

            // Print entries to console
            System.out.println();
            System.out.print("Dynamic Programming\n");
            System.out.println();
            for (int i = 1; i <= numItems; i++) {
                // System.out.print("row"+i);
                for (int j = 0; j <= capacity; j++) {
                    System.out.print( " " + dpTable[i][j] );
                }
                System.out.print(" end");
                System.out.print("\n");
            }


            // Print entries to file entries2.txt
            for (int i = 1; i <= numItems; i++) {
                entriesFileWriter.write("row"+i);
                for (int j = 0; j <= capacity; j++) {
                    if(dpTable[i][j]!=0)
                        entriesFileWriter.write( " " + dpTable[i][j] );  // write entries to file
                }
                entriesFileWriter.write("\n");
            }

            int w = capacity;
            int res = dpTable[numItems][capacity];
            int tot_weight = 0;
            for (int i = numItems; i > 0 && res > 0; i--) {
                if (res == dpTable[i - 1][w])
                    continue;   
                else {
                    tot_weight+=weights[i];
                    //ans.push_back(i);
                    selectedItems.add(i);    // add item to list of selected items
                    res = res - profits[i];
                    w = w - weights[i];
                }
            }


              // Print total profit and weight to output2.txt file
            outputFileWriter.write(selectedItems.size() + " " + dpTable[numItems][capacity] + " " + tot_weight + " " );
            
            // Print selected items to output file
            for (int i = selectedItems.size() - 1; i >= 0; i--) {
                int itemIndex = selectedItems.get(i);
                outputFileWriter.write("\nItem" + itemIndex + " " + profits[itemIndex] + " " + weights[itemIndex]);
            }

            System.out.println();
            System.out.println("max profit:" + dpTable[numItems][capacity]);
            System.out.println("max weight:" + tot_weight);
            
            // Print selected items to console
            for (int i = selectedItems.size() - 1; i >= 0; i--) {
                int itemIndex = selectedItems.get(i);
                System.out.println("\nItem" + itemIndex + " " + "profit:" + profits[itemIndex] + " " + "weight:" + weights[itemIndex] );
            }

            entriesFileWriter.close();
            outputFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Recursive function to compute optimal solution
    private static int knapsack(int i, int j) {
        //It takes two arguments, i representing the number of items and j representing the remaining capacity of the knapsack

        //It returns the maximum profit that can be obtained with the given i items and j capacity. It first checks if the value is already stored in the dynamic programming table, and if not, it calculates the maximum profit by considering two cases: taking the ith item or not taking it. It then stores the result in the dynamic programming table


        if (i <= 0) {
            return 0;
        }
        if (dpTable[i][j] == 0) {
            if (j < weights[i]) {
                dpTable[i][j] = knapsack(i - 1, j);
            } else {
                int withoutItem = knapsack(i - 1, j);
                int withItem = profits[i] + knapsack(i - 1, j - weights[i]);
                dpTable[i][j] = Math.max(withoutItem, withItem);


                // if (withItem > withoutItem) {
                //     selectedItems.add(i);
                // }
            }
        }
        return dpTable[i][j];
    }
    
}
