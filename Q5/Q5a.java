import java.util.Random;

public class Q5a {
  public static void main(String[] args) {
    // Generate a random initial solution of length 3
    char[] bestSolution = generateRandomSolution(3);
    int bestScore = evaluate(bestSolution);

    // Continuously improve the solution until the best score is 0
    while (true) {
      // Display the current best score and the corresponding solution
      System.out.println("Best score so far: " + bestScore + " Solution: " + new String(bestSolution));

      // If the best score is already 0, exit the loop
      if (bestScore == 0) {
        break;
      }

      // Clone the best solution and introduce a mutation
      char[] newSolution = bestSolution.clone();
      mutateSolution(newSolution);

      // Evaluate the new solution
      int newScore = evaluate(newSolution);

      // If the new solution has a lower score, update the best solution and best
      // score
      if (newScore < bestScore) {
        bestSolution = newSolution;
        bestScore = newScore;
      }
    }
  }

  // Generate a random solution of a given length
  public static char[] generateRandomSolution(int length) {
    char[] solution = new char[length];
    Random random = new Random();

    for (int i = 0; i < length; i++) {
      // Generate a random character in the range of printable ASCII characters
      solution[i] = (char) (random.nextInt(94) + 32);
    }

    return solution;
  }

  // Evaluate the difference between a solution and the target string
  public static int evaluate(char[] solution) {
    String target = "DSA";
    int diff = 0;

    for (int i = 0; i < target.length(); i++) {
      char s = solution[i];
      char t = target.charAt(i);
      // Calculate the absolute difference between ASCII values
      diff += Math.abs((int) s - (int) t);
    }

    return diff;
  }

  // Introduce a mutation in a solution by randomly changing a character
  public static void mutateSolution(char[] solution) {
    Random random = new Random();
    int index = random.nextInt(solution.length);
    // Generate a new random character to replace the existing one
    solution[index] = (char) (random.nextInt(94) + 32);
  }
}
