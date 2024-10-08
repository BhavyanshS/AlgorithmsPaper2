import java.util.Arrays;
import java.util.Random;

public class ParallelQuickSort {
    private static final int NUM_THREADS = 4;
    private static final int NUM_OF_RAN_INT = 1000;
    private static int[] globalArray;
    private static Random random = new Random();  // Create a single Random instance for repeated use

    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100); // Random numbers between 0 and 99
        }
        return array;
    }

    public void pqsa(int[] array, int start, int end, int Threads) {
        //Check if array has more than 1 element
        if (start < end) {
            //First we split the array
            int pivot = partition(array, start, end);

            if (Thread.activeCount() < Threads) {
                Thread firstThread = new Thread(new Runnable() {
                    //This thread runs the first half
                    @Override
                    public void run() {
                        pqsa(array, start, pivot - 1, Threads);
                    }
                });
                Thread secondThread = new Thread(new Runnable() {
                    //This thread runs the second half
                    @Override
                    public void run() {
                        pqsa(array, pivot + 1, end, Threads);
                    }
                });

                firstThread.start();
                secondThread.start();

                try {
                    //Wait for both threads to finish
                    firstThread.join();
                    secondThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                //When you reach the max number of threads
                //Just quick sort the array
                pqsa(array, start, pivot - 1, Threads);
                pqsa(array, pivot + 1, end, Threads);
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

    public static void main(String[] args) {
        ParallelQuickSort pqsa = new ParallelQuickSort();
        int runs = 10;
        long totalDuration = 0;

        for (int i = 0; i < runs; i++) {
            globalArray = generateRandomArray(NUM_OF_RAN_INT);
            long startTime = System.currentTimeMillis();
            pqsa.pqsa(globalArray, 0, NUM_OF_RAN_INT - 1, NUM_THREADS);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            totalDuration += duration;
        }
        System.out.println("Sorted array: " + Arrays.toString(globalArray));

        double averageTime = (double) totalDuration / runs;
        System.out.println("Average sorting time: " + averageTime + " ms for threads:" + NUM_THREADS);
    }

}
