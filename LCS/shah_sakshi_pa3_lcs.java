import java.util.*;

public class shah_sakshi_pa3_lcs {

    public static void main(String[] args) {
        // Check that two input strings have been provided
        if (args.length != 2) {
            System.out.println("error");
            System.exit(-1);
        }

        // Get the two input strings
        String str1 = args[0];
        String str2 = args[1];

        // Get the length of each string
        int m = str1.length();
        int n = str2.length();

        if (m > 100 || n > 100) {
            System.out.println("the string length too long");
            System.exit(-2);
        }

        // Create a 2D array to store the LCS lengths
        // for all possible pairs of substrings

        int[][] matrix = new int[m + 1][n + 1];
        // Populate the array using dynamic programming

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0) {
                    // If one of the substrings is empty, the LCS length is 0
                    matrix[i][j] = 0;
                }

                // If the current characters match, add 1 to the LCS length
                else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                }

                // If the current characters do not match, take the maximum of the
                // LCS lengths of the two substrings without the current characters
                else {
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i][j - 1]);
                }
            }
        }

        // Get the length of the LCS from the matrix array
        int lcsLength = matrix[m][n];

        // Create a character array to store the LCS
        char[] lcs = new char[lcsLength];

        // Start from the bottom-right corner of the matrix array
        // and traverse it backwards
        int i = m, j = n;
        while (i > 0 && j > 0) {

            // If the current characters match, add it to the LCS and move diagonally
            // up-left
            if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                lcs[--lcsLength] = str1.charAt(i - 1);
                i--;
                j--;
            }

            // If the LCS length in the top cell of matrix array is greater than the LCS
            // length in the left cell,
            // move up to the next row
            else if (matrix[i - 1][j] > matrix[i][j - 1]) {
                i--;
            }

            // If the LCS length in the left cell of matrix array is greater than the LCS
            // length in the top cell,
            // move left to the next column
            else {
                j--;
            }
        }

        // If either input string is empty or they don't have a single matching
        // character,
        // the LCS length is 0
        if (m == 0 || n == 0 || matrix[m][n] == 0) {
            System.out.println("Length of LCS: " + matrix[m][n]);
            // System.out.println("LCS: \"\" ");
            System.out.println("LCS:");
        }

        // Otherwise, print the LCS length
        else {
            System.out.println("Length of LCS: " + matrix[m][n]);
            System.out.println("LCS:" + new String(lcs));
        }

    }
}


//time complexity : O(mn) where m and n are length of the two strings. 
