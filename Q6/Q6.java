import java.util.Arrays;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class Q6 {

    // Class to perform merge sort using Fork-Join framework
    private static class MergeSortingTask extends RecursiveAction {
        private final int[] arr;
        private final int start;
        private final int end;

        // Constructor to initialize the task
        public MergeSortingTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {

            // Base case: If the subarray size is 1 or less, it's already sorted
            if (end - start <= 1) {
                return;
            }

            // Calculate the midpoint of the subarray
            int midIndex = (start + end) / 2;

            // Create tasks to sort the left and right halves
            MergeSortingTask leftTask = new MergeSortingTask(arr, start, midIndex);
            MergeSortingTask rightTask = new MergeSortingTask(arr, midIndex, end);

            // Fork both tasks and wait for them to complete
            invokeAll(leftTask, rightTask);

            // Merge the sorted left and right halves
            merge(arr, start, midIndex, end);
        }

        // Function to merge two sorted halves
        private void merge(int[] array, int start, int midIndex, int end) {
            int[] merged = new int[end - start];
            int leftIndex = start;
            int rightIndex = midIndex;
            int mergedIndex = 0;

            // Merge elements from both halves in sorted order
            while (leftIndex < midIndex && rightIndex < end) {
                if (array[leftIndex] < array[rightIndex]) {
                    merged[mergedIndex++] = array[leftIndex++];
                } else {
                    merged[mergedIndex++] = array[rightIndex++];
                }
            }

            // Copy any remaining elements from the left and right halves
            while (leftIndex < midIndex) {
                merged[mergedIndex++] = array[leftIndex++];
            }

            while (rightIndex < end) {
                merged[mergedIndex++] = array[rightIndex++];
            }

            // Copy the merged array back to the original array
            System.arraycopy(merged, 0, array, start, merged.length);
        }
    }

    // Function to perform merge sort using Fork-Join framework
    public static void mergeSortWithForkJoin(int[] array) {
        ForkJoinPool pool = new ForkJoinPool();
        MergeSortingTask task = new MergeSortingTask(array, 0, array.length);
        pool.invoke(task);
    }

    public static void main(String[] args) {

        // Test case 1
        int[] inputArray1 = { 5, 3, 9, 1, 7, 2, 8, 4, 6 };
        mergeSortWithForkJoin(inputArray1);
        System.out.println("Sorted array 1: " + Arrays.toString(inputArray1));

        // Test case 2
        int[] inputArray2 = { 1, 2, 3, 4, 5, 6 };
        mergeSortWithForkJoin(inputArray2);
        System.out.println("Sorted array 2: " + Arrays.toString(inputArray2));

        // Test case 3
        int[] inputArray3 = { 6, 5, 4, 3, 2, 1 };
        mergeSortWithForkJoin(inputArray3);
        System.out.println("Sorted array 3: " + Arrays.toString(inputArray3));

    }
}
