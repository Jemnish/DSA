public class Q3a {
    public static int maxPoints(int[] balloons) {
        int numBalloons = balloons.length;

        // Create a new array with padding on both ends
        int[] paddedArray = new int[numBalloons + 2];
        paddedArray[0] = 1;
        System.arraycopy(balloons, 0, paddedArray, 1, numBalloons);
        paddedArray[numBalloons + 1] = 1;

        // Initialize a 2D array for dynamic programming
        int[][] dpTable = new int[numBalloons + 2][numBalloons + 2];

        // Dynamic programming approach
        for (int len = 1; len <= numBalloons; len++) {
            for (int start = 1; start <= numBalloons - len + 1; start++) {
                int end = start + len - 1;
                for (int k = start; k <= end; k++) {
                    // Update the dynamic programming table using the recurrence relation
                    dpTable[start][end] = Math.max(dpTable[start][end],
                            dpTable[start][k - 1] + paddedArray[start - 1] * paddedArray[k] * paddedArray[end + 1]
                                    + dpTable[k + 1][end]);
                }
            }
        }

        return dpTable[1][numBalloons];
    }

    public static void main(String[] args) {
        int[] balloonsA = { 3, 1, 5, 8 };

        // Calculate and print the results
        int resultA = maxPoints(balloonsA);
        System.out.println("Maximum points for balloonsA: " + resultA); // Output: 167
    }
}
