import java.util.Random;
import java.util.Stack;

public class IterativeQuickSort implements Runnable {
    private int[] arr;
    private int start;
    private int end;
    private static  Random random = new Random();  // Create a single Random instance for repeated use

    public IterativeQuickSort(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    public void sort() {
        iterativeQuickSort();
    }

    private void iterativeQuickSort() {
        if (arr == null || arr.length == 0) {
            return;
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(start);
        stack.push(end);

        while (!stack.isEmpty()) {
            int high = stack.pop();
            int low = stack.pop();

            if (high <= low) {
                continue;
            }

            int pivotIndex = partition(arr, low, high);

            // Push left subarray to be sorted
            if (pivotIndex - 1 > low) {
                stack.push(low);
                stack.push(pivotIndex - 1);
            }

            // Push right subarray to be sorted
            if (pivotIndex + 1 < high) {
                stack.push(pivotIndex + 1);
                stack.push(high);
            }
        }
    }

    private static int partition(int[] arr, int low, int high) {
        // Randomly select pivot index and swap it with the last element
        int pivotIndex = low + random.nextInt(high - low + 1);
        swap(arr, pivotIndex, high);

        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        // Swap pivot into its correct place
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    @Override
    public void run() {
        sort();
    }

    public static void main(String[] args) {
        int[] array = {10, 7, 8, 9, 1, 5, 3, 6, 4, 2};
        int start = 0; // start index of the subarray to sort
        int end = 9;   // end index of the subarray to sort

        IterativeQuickSort sorter = new IterativeQuickSort(array, start, end);
        Thread thread = new Thread(sorter);
        thread.start();

        try {
            thread.join(); // Wait for the sorting to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted, Failed to complete sort");
        }

        System.out.println("Sorted array: ");
        for (int i : array) {
            System.out.print(i + " ");
        }
    }
}
