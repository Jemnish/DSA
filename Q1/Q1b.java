import java.util.Arrays;

public class Q1b {

    // Function to calculate the minimum coins required for riders
    public static int calculateMinimumCoins(int[] ratings) {
        int n = ratings.length;
        int[] coins = new int[n]; // To store the minimum coins required for each rider

        // Initialize the first rider with 1 coin
        coins[0] = 1;

        // Traverse from left to right, update coins based on left neighbor
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                coins[i] = coins[i - 1] + 1; // If rating is higher, give one more coin than the left neighbor
            } else {
                coins[i] = 1; // If rating is not higher, give 1 coin
            }
        }

        // Traverse from right to left, update coins based on right neighbor and current
        // value
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1] && coins[i] <= coins[i + 1]) {
                coins[i] = coins[i + 1] + 1; // If rating is higher than right neighbor and has less coins, update coins
            }
        }

        // Calculate the total minimum coins required
        int totalCoins = 0;
        for (int coin : coins) {
            totalCoins += coin; // Sum up all the coins for total minimum coins
        }

        return totalCoins;
    }

    public static void main(String[] args) {
        int[] ratings = { 1, 0, 2 };
        int result = calculateMinimumCoins(ratings); // Calculate the minimum coins required
        System.out.println("Minimum coins required: " + result); // Print the result
    }
}
