import java.util.List;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.ArrayList;

// Implementation of the Bellman-Ford algorithm to find shortest paths in a weighted graph
public class Q3b {

    public static int[] bellmanFord(int[][] graph, int vertices, int source) throws Exception {
        int[] distance = new int[vertices];
        Arrays.fill(distance, Integer.MAX_VALUE); // Initialize distances as infinity
        distance[source] = 0; // Distance from source to itself is 0

        // Relaxation step for each edge for (vertices - 1) times
        for (int i = 0; i < vertices - 1; i++) {
            for (int j = 0; j < graph.length; j++) {
                int u = graph[j][0]; // Source vertex of the edge
                int v = graph[j][1]; // Destination vertex of the edge
                int w = graph[j][2]; // Weight of the edge
                if (distance[u] != Integer.MAX_VALUE && distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w; // Update distance if a shorter path is found
                }
            }
        }

        // Detecting negative weight cycles
        for (int j = 0; j < graph.length; j++) {
            int u = graph[j][0];
            int v = graph[j][1];
            int w = graph[j][2];
            if (distance[u] != Integer.MAX_VALUE && distance[u] + w < distance[v]) {
                // Throw an exception if a negative weight cycle is found
                throw new Exception("Graph contains a negative weight cycle");
            }
        }

        return distance; // Return the array of shortest distances
    }

    public static void main(String[] args) {
        int[][] graph = {
                { 0, 1, 2 },
                { 0, 2, 3 },
                { 0, 3, 5 },
                { 1, 2, -1 },
                { 2, 3, 4 },
                { 3, 4, 2 },
                { 4, 0, 1 }
        };

        int vertices = 5; // Number of vertices in the graph
        int sourceVertex = 0; // Source vertex for the shortest paths

        try {
            int[] distance = bellmanFord(graph, vertices, sourceVertex);
            System.out.println("Shortest distance from source vertex " + sourceVertex + ": ");
            for (int i = 0; i < vertices; i++) {
                System.out.println(i + "= " + distance[i]); // Print shortest distances from the source vertex
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()); // Print any exception message
        }
    }
}

// Implementation of a MaxHeap data structure
class MaxHeap {
    private List<Integer> heap; // List to store the heap elements

    public MaxHeap() {
        heap = new ArrayList<>(); // Initialize the heap as an empty list
    }

    public void push(int val) {
        heap.add(val); // Add a new value to the heap
        shiftUp(heap.size() - 1); // Restore the heap property by shifting the value up
    }

    public int pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }

        int maxValue = heap.get(0); // Store the maximum value
        int lastValue = heap.remove(heap.size() - 1); // Remove the last value

        if (!isEmpty()) {
            heap.set(0, lastValue); // Replace the root with the last value
            shiftDown(0); // Restore the heap property by shifting the value down
        }

        return maxValue; // Return the maximum value
    }

    public boolean isEmpty() {
        return heap.isEmpty(); // Check if the heap is empty
    }

    private void shiftUp(int index) {
        int parent = (index - 1) / 2; // Calculate the index of the parent node

        while (index > 0 && heap.get(index) > heap.get(parent)) {
            swap(index, parent); // Swap the value with its parent if it's larger
            index = parent;
            parent = (index - 1) / 2; // Update indices for the next iteration
        }
    }

    private void shiftDown(int index) {
        int leftChild = index * 2 + 1; // Calculate the index of the left child
        int rightChild = index * 2 + 2; // Calculate the index of the right child
        int largest = index; // Assume the current index has the largest value

        if (leftChild < heap.size() && heap.get(leftChild) > heap.get(largest)) {
            largest = leftChild; // Update largest if the left child is larger
        }

        if (rightChild < heap.size() && heap.get(rightChild) > heap.get(largest)) {
            largest = rightChild; // Update largest if the right child is larger
        }

        if (largest != index) {
            swap(index, largest); // Swap the value with the largest child if necessary
            shiftDown(largest); // Recursively shift the value down
        }
    }

    private void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp); // Swap two values in the heap
    }
}
