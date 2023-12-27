import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class Item {
    int profit;         //Item class has two member variables, profit and weight
    int weight;
}

public class program1 {

    public static void main(String[] args) throws IOException {
        String knapsackFile = null;

        // args: [-k, knapsack01.txt]
        for (int i = 0; i < args.length; i++) {

            //This loop goes through the args array and sets knapsackFile to the value of the next argument if the current argument is -k
            if (args[i].equals("-k") && i + 1 < args.length) {
                knapsackFile = args[i + 1];
            }
        }

        // Create knapsack problem
        Random random = new Random();
        int n = random.nextInt(5) + 4;  //This generates a random number between 4 and 8 and sets it to the variable n

        System.out.println("0/1 Knapsack problem solutions");

        System.out.print("Randomly selected " + n + " numbers\n");
        // System.out.println("Number of items: " + n);
        Item[] items = new Item[n];
        int totalWeight = 0;
        for (int i = 0; i < n; i++) {
            // loop that iterates over the items
            Item item = new Item();
            item.profit = random.nextInt(21) + 10;  //generates a random number between 10 and 30 and sets it to the profit field of the item.
            item.weight = random.nextInt(16) + 5; //This generates a random number between 5 and 20 and sets it to the weight field of the item
            items[i] = item;
            totalWeight += item.weight;
        }
        int capacity = (int) Math.floor(0.6 * totalWeight);

        // Write knapsack problem to file
        FileWriter writer = new FileWriter(knapsackFile);
        writer.write(n + " " + capacity + "\n");
        for (int i = 0; i < n; i++) {
            writer.write("Item" + (i + 1) + " " + items[i].profit + " " + items[i].weight + "\n");
        }

        for (int i = 0; i < n; i++) {
            System.out.println("item" + (i + 1) + ":" + " " + "profit:" + items[i].profit + ", " + "weight:" + items[i].weight);
        }

        System.out.println("\nThe capacity of the knapsack:" + capacity + "\n");
        System.out.println("Brute Force Method");

        writer.close();

        // Solve knapsack problem using brute force method
        int maxProfit = 0;
        int maxWeight = 0;
        ArrayList<Item> selectedItems = new ArrayList<>();
        int numSolutions = (int) Math.pow(2, n); //n is the number of items.
        for (int i = 0; i < numSolutions; i++) {

           
            int profit = 0;
            int weight = 0;
            ArrayList<Item> itemsSelected = new ArrayList<>();
            for (int j = 0; j < n; j++) {

                 //used to generate all possible combinations of items that could be included in the knapsack.

                 //checks if the j-th item is included in the i-th solution. If it is, its profit and weight are added to the profit and weight variables, respectively. The selected item is also added to the itemsSelected ArrayList

                if ((i & (1 << j)) > 0) {
                    profit += items[j].profit;
                    weight += items[j].weight;
                    itemsSelected.add(items[j]);
                }
            }

            //If the total weight of the items in the solution is less than or equal to the knapsack capacity and the total profit is greater than maxProfit, maxProfit, maxWeight, and selectedItems are updated to reflect the current solution
            if (weight <= capacity && profit > maxProfit) {
                maxProfit = profit;
                maxWeight = weight;
                selectedItems = itemsSelected;
            }
        }

        // Write output to file
        // contains the number items that produced the maximum profit, the
// produced maximum profit, and the total weight for the items that produced the
// maximum profit. Each of the following lines contains the name of the item, the profit of
// the item, and the weight of the item

        writer = new FileWriter("output1.txt");
        writer.write(selectedItems.size() + " " + maxProfit + " " + maxWeight + "\n");
        System.out.println("max profit:" +  maxProfit);
        System.out.println("max weight:" + maxWeight + "\n");

        for (Item item : selectedItems) {
            writer.write("Item" + (getIndex(items, item) + 1) + " " + item.profit + " " + item.weight + "\n");

            System.out.println("Item" + (getIndex(items, item) + 1) + ": " + "profit:" + item.profit + ", " + "weight:" + item.weight);
    
        }
        writer.close();
    }

    //below function is used to find the index of an item in the items array.
    private static int getIndex(Item[] items, Item item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == item) {
                return i;
            }
        }
        return -1;
    }
}
