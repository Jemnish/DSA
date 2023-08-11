import java.util.Random;

// A class that generates random ports from a whitelist, excluding blacklisted ports
class WhitelistedRandomPort {
    private int k; // Total number of ports allowed (including both whitelisted and blacklisted)
    private int[] blacklist; // Array containing blacklisted port numbers
    private int remaining; // Number of remaining whitelisted ports
    private Random rand; // Random number generator

    // Constructor to initialize the class with a whitelist and blacklist
    public WhitelistedRandomPort(int k, int[] blacklist) {
        this.k = k;
        this.blacklist = blacklist;
        this.remaining = k - blacklist.length; // Calculate the number of whitelisted ports
        this.rand = new Random(); // Initialize the random number generator
    }

    // Method to get a random whitelisted port
    public int get() {
        int randNum = rand.nextInt(remaining); // Generate a random number within the remaining range

        // Check for each blacklisted port and adjust the random number if needed
        for (int blackPort : blacklist) {
            if (randNum >= blackPort) {
                randNum++; // Skip over blacklisted port by increasing the random number
            }
        }

        return randNum; // Return the final whitelisted random port number
    }
}

// Main class to demonstrate the usage of WhitelistedRandomPort
public class Q2b {
    public static void main(String[] args) {
        int[] blacklist = { 2, 3, 5 }; // List of blacklisted port numbers
        WhitelistedRandomPort wrp = new WhitelistedRandomPort(7, blacklist); // Create an instance

        // Generate and print random whitelisted port numbers
        System.out.println(wrp.get()); // Output: Any number from [0, 1, 4, 6] with equal probability
        System.out.println(wrp.get()); // Output: 4
        // You can call wrp.get() multiple times to generate more whitelisted random
        // ports
    }
}
