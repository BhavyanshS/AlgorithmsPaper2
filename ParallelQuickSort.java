import java.util.Random;

public class ParallelQuickSort {
    private static final int NUM_THREADS = 1; // Number of threads to use
    private static final int NUM_OF_RAN_INT = 100; // Number of random integers to sort
    private static int[] globalArray; // Global array to be sorted

    // Method to generate an array of random integers
    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100); // Random numbers between 0 and 99
        }
        return array;
    }

    // Method to perform parallel quick sort
    public void pqsa(int[] array, int start, int end) {
        if (start < end) {
            int pivotIndex = partition(array, start, end); // Partition the array

            // Limit the number of threads
            if (Thread.activeCount() < NUM_THREADS) {
                Thread leftThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pqsa(array, start, pivotIndex - 1);
                    }
                });
                Thread rightThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pqsa(array, pivotIndex + 1, end);
                    }
                });

                leftThread.start(); // Start left thread
                rightThread.start(); // Start right thread

                try {
                    leftThread.join(); // Wait for left thread to finish
                    rightThread.join(); // Wait for right thread to finish
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // If maximum number of threads reached, perform sequential quick sort
                pqsa(array, start, pivotIndex - 1);
                pqsa(array, pivotIndex + 1, end);
            }
        }
    }

    // Method to partition the array
    private static int partition(int[] array, int start, int end) {
        int pivot = array[end];
        int i = start - 1;
        for (int j = start; j < end; j++) {
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[end];
        array[end] = temp;
        return i + 1;
    }

    public static void main(String[] args) {
        ParallelQuickSort pqsa = new ParallelQuickSort();
        int runs = 100; // Number of runs for averaging sorting time
        long totalDuration = 0;

        for (int i = 0; i < runs; i++) {
            globalArray = generateRandomArray(NUM_OF_RAN_INT); // Generate random array
            long startTime = System.currentTimeMillis();
            pqsa.pqsa(globalArray, 0, NUM_OF_RAN_INT - 1); // Perform parallel quick sort
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            totalDuration += duration;
        }

        double averageTime = (double) totalDuration / runs;
        System.out.println("Average sorting time: " + averageTime + " ms for threads:" + NUM_THREADS);
    }

}
