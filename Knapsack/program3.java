import java.io.*;
import java.util.*;


public class program3 {
    static int n, capacity, maxProfit, maxWeight, numItems, nodes;
    static int[] weight, profit, position, finalSolution;
    static List<Integer> bestItems = new ArrayList<>();
    static PrintWriter entries; 
    static PrintWriter output; 

    public static void main(String[] args) throws IOException {
        // Read input file and initialize variables
        Scanner in = new Scanner(new File(args[1]));
        String[] line = in.nextLine().split("\\s+");
        n = Integer.parseInt(line[0]);
        nodes = n;
        capacity = Integer.parseInt(line[1]);

        weight = new int[n];
        profit = new int[n];
        position = new int[n];
     
        finalSolution = new int[n];

        for (int i = 0; i < n; i++) {
            in.next(); // discard the first string column
            profit[i] = in.nextInt();
            weight[i] = in.nextInt();
            position[i] = i;
        }

        // Collections.sort(items, (a, b) -> Double.compare((double)b.getFirst() * a.getSecond(), (double)a.getFirst() * b.getSecond()));

        List<Integer> remainingItems = new ArrayList<>();
        // List<Integer> solution = new ArrayList<>();
        int taken[] = new int[n];

        // Create output files
        entries = new PrintWriter(new FileWriter("entries3.txt"));
        output = new PrintWriter(new FileWriter("output3.txt"));
        
        sortKnapsack(weight,profit,position);

        for(int i=0; i<n; i++){
            remainingItems.add(i);
        }

        // Run the backtracking algorithm


        System.out.println();
        System.out.println("Backtracking algorithm");
        System.out.println();

        for (int i = 0; i < n; i++) {
            double pUw = (double) profit[i] / weight[i];
            System.out.printf("index:%d, profit:%d, weight:%d, pUw:%f%n", i, profit[i], weight[i], pUw);
        }

    
        backtrack(0, 0, 0,remainingItems, taken, capacity );

       
        //System.out.println("Backtracking Algorithm");
        System.out.println();

        System.out.println("max profit:" + maxProfit);
        System.out.println("max weight:" + maxWeight);
        System.out.println();



        
    
        output.println(bestItems.size() + " " + maxProfit + " " + maxWeight);
        

        
        for (int i: bestItems) {
            //maxWeight+= weight[i];
            double pUw = (double) profit[i] / weight[i];
            output.println("Item" + (position[i]+1) + " " + profit[i] + " " + weight[i]);
            System.out.println("Item" + (position[i]+1) + ": " + "profit:" + profit[i] + ", " + "weight:" + weight[i] + "\n");
        }

        entries.close();
        output.close();
    }

    public static void sortKnapsack(int[] weight, int[] profit, int[] position) {
        int n = weight.length;
        double[] pwRatio = new double[n];
    
        // Calculate the p/w ratio for each item
        for (int i = 0; i < n; i++) {
            pwRatio[i] = (double) profit[i] / weight[i];
        }
    
        // Sort the items based on the p/w ratio in descending order
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (pwRatio[i] < pwRatio[j]) {
                    // Swap the p/w ratio
                    double tempRatio = pwRatio[i];
                    pwRatio[i] = pwRatio[j];
                    pwRatio[j] = tempRatio;
    
                    // Swap the weight
                    int tempWeight = weight[i];
                    weight[i] = weight[j];
                    weight[j] = tempWeight;
    
                    // Swap the profit
                    int tempProfit = profit[i];
                    profit[i] = profit[j];
                    profit[j] = tempProfit;
                    
                    // Swap the position
                    int tempPosition = position[i];
                    position[i] = position[j];
                    position[j] = tempPosition;
                }
            }
        }
    }

    
    public static void backtrack(int nodeNum, int currentValue, int currentWeight, List<Integer> remainingItems, int[] taken, int capacity) {
        double upperBound = calculateUpperBound(currentValue, currentWeight, remainingItems, capacity, maxProfit, maxWeight);
        
    
        System.out.println("node: Index:" + nodeNum + " profit:" + currentValue + " weight:" + currentWeight + " upperBound:" + upperBound);
      
        entries.println(nodeNum + " " + currentValue + " " + currentWeight + " " + upperBound);

        if (currentValue > maxProfit) {
            maxProfit = currentValue;
            maxWeight = currentWeight;
            bestItems.clear();
            for (int j = 0; j < nodes; j++) {
                if (taken[j] == 1) {
                    bestItems.add(j);
                }
            }
        }

        if (upperBound <= maxProfit) {
            return;
        }

        int item = remainingItems.get(0);
        remainingItems.remove(0);

        if (currentWeight + weight[item] <= capacity) {
            taken[item] = 1;
            backtrack(nodeNum+1, currentValue + profit[item], currentWeight + weight[item], new ArrayList<Integer>(remainingItems), taken, capacity);
        } else {
            System.out.println("node: Index:" + (nodeNum +1) + " profit:" + (currentValue + profit[item]) + " weight:" + (currentWeight + weight[item]) + " upperBound:" + upperBound);
            entries.println((nodeNum +1) + " " + (currentValue + profit[item]) + " " + (currentWeight + weight[item]) + " " + upperBound);
        }
        taken[item] = 0;

        backtrack(nodeNum+1, currentValue, currentWeight, new ArrayList<Integer>(remainingItems), taken, capacity);
    }


    public static double calculateUpperBound(int currentValue, int currentWeight, List<Integer> remainingItems, int capacity, int maxProfit, int maxWeight) {
        int n = remainingItems.size();
        //System.out.println(n);
        double upperBound = currentValue;
        for (int i = 0; i < n; i++) {
            int item = remainingItems.get(i);

            //System.out.print(profit[item] + " " + weight[item] + " ");
            if (currentWeight + weight[item] <= capacity) {
                upperBound += profit[item];
                currentWeight += weight[item];
            }
            else {
                double fraction = (double)(capacity - currentWeight) / weight[item];
                upperBound += fraction * profit[item];
                break;
            }
        }
        return upperBound;
    }


    static int getFinalWeight() {
        int finalWeight = 0;
        for (int i = 0; i < n; i++) {
            if (finalSolution[i] == 1) {
                finalWeight += weight[i];
            }
        }
        return finalWeight;
    }
}
