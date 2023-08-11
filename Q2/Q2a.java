import java.util.Arrays;

public class Q2a {

    // Function to calculate the length of the longest decreasing subsequence
    public static int longestDecreasingSubsequence(int[] nums, int k) {
        int len = nums.length;

        // Initialize an array to store the length of decreasing subsequences
        int dp[] = new int[len];
        for (int i = 0; i < len; i++) {
            dp[i] = 1; // Initialize each element as a standalone subsequence
        }

        // Iterate through the array to find the longest decreasing subsequence
        for (int i = 1; i < len; i++) {
            for (int j = 0; j < i; j++) {
                // Check if the current number is smaller than a previous number
                // and the difference is within the given limit 'k'
                if (nums[i] < nums[j] && nums[j] - nums[i] <= k) {
                    dp[i] = Math.max(dp[i], dp[j] + 1); // Update the subsequence length
                }
            }
        }

        // Find the maximum length of decreasing subsequences
        int maxLength = 1;
        Arrays.sort(dp); // Sort the dp array to get the maximum value
        maxLength = dp[len - 1];
        return maxLength;
    }

    public static void main(String[] args) {
        int[] nums = { 8, 4, 5, 2, 1, 4, 3, 4, 3, 1, 15 };
        int k = 3;

        int lds = longestDecreasingSubsequence(nums, k);
        // Calculate and print the length of the longest decreasing subsequence
        System.out
                .println("Length of the longest decreasing subsequence: " + lds);
    }
}
