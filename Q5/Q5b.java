import java.util.*;

public class Q5b {

    public static int minReorder(int n, int[][] connections) {
        // Creating an adjacency list to represent the graph
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // Building the graph using connections array
        for (int[] connection : connections) {
            int from = connection[0];
            int to = connection[1];
            graph.get(from).add(to); // Adding directed edge from 'from' to 'to'
            graph.get(to).add(-from); // Adding reversed edge to indicate the need for reordering
        }

        // Starting depth-first search from node 0 and calculating the number of
        // reordering changes needed
        return dfs(graph, 0, new boolean[n]);
    }

    public static int dfs(List<List<Integer>> graph, int node, boolean[] visited) {
        visited[node] = true;
        int changes = 0;

        // Iterating through neighbors of the current node
        for (int neighbor : graph.get(node)) {
            if (!visited[Math.abs(neighbor)]) {
                // Counting the edges that need to be reversed
                changes += neighbor > 0 ? 1 : 0;
                // Recursively exploring the unvisited neighbors
                changes += dfs(graph, Math.abs(neighbor), visited);
            }
        }

        return changes;
    }

    public static void main(String[] args) {
        int n = 6;
        int[][] connections = { { 0, 1 }, { 1, 3 }, { 2, 3 }, { 4, 0 }, { 4, 5 } };
        int output = minReorder(n, connections);
        System.out.println("Minimum reordering changes needed: " + output); // Expected Output: 3
    }
}
