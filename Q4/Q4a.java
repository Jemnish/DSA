import java.util.*;

public class Q4a {

    // Function to calculate the minimum steps to complete tasks with dependencies
    public static int minSteps(int numTasks, int[][] prerequisites) {
        // Create a graph to represent task dependencies
        List<List<Integer>> graph = new ArrayList<>();
        int[] indegree = new int[numTasks + 1];

        // Initialize graph with empty adjacency lists
        for (int i = 0; i <= numTasks; i++) {
            graph.add(new ArrayList<>());
        }

        // Populate the graph and calculate indegree for each task
        for (int[] prerequisite : prerequisites) {
            int taskA = prerequisite[0];
            int taskB = prerequisite[1];
            graph.get(taskA).add(taskB); // taskA depends on taskB
            indegree[taskB]++;
        }

        // Queue to store tasks with no dependencies
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 1; i <= numTasks; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        // Process tasks and calculate the minimum steps required
        int steps = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int currentTask = queue.poll();
                for (int nextTask : graph.get(currentTask)) {
                    indegree[nextTask]--;
                    if (indegree[nextTask] == 0) {
                        queue.add(nextTask);
                    }
                }
            }
            steps++;
        }

        // Check for any remaining tasks with unresolved dependencies (cycles)
        for (int i = 1; i <= numTasks; i++) {
            if (indegree[i] > 0) {
                return -1; // Cycle detected, impossible to complete all tasks
            }
        }

        return steps;
    }

    public static void main(String[] args) {
        int numTasks = 3;
        int[][] prerequisites = { { 1, 3 }, { 2, 3 } };
        int result = minSteps(numTasks, prerequisites);
        System.out.println("Minimum steps to complete tasks: " + result);
    }
}
