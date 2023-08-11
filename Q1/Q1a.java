public class Q1a {

    // Function to calculate the minimum cost of painting houses
    public static int calculateMinimumCost(int[][] housePrices) {
        int numHouses = housePrices.length;

        int prevCost0 = housePrices[0][0];
        int prevCost1 = housePrices[0][1];
        int prevCost2 = housePrices[0][2];

        // Iterate over the remaining houses
        for (int i = 1; i < numHouses; i++) {
            int currentCost0 = housePrices[i][0] + Math.min(prevCost1, prevCost2);
            int currentCost1 = housePrices[i][1] + Math.min(prevCost0, prevCost2);
            int currentCost2 = housePrices[i][2] + Math.min(prevCost0, prevCost1);

            prevCost0 = currentCost0;
            prevCost1 = currentCost1;
            prevCost2 = currentCost2;
        }

        // Find the minimum cost from the last row (painting all houses)
        int minCost = Math.min(prevCost0, Math.min(prevCost1, prevCost2));

        return minCost;
    }

    public static void main(String[] args) {
        // Define the prices for painting each house with different colors
        int[][] housePrices = { { 14, 4, 11 }, { 11, 14, 3 }, { 14, 2, 10 } };

        // Calculate and print the minimum cost of painting
        int minCost = calculateMinimumCost(housePrices);
        System.out.println("Minimum cost of painting all houses: " + minCost);
    }
}
