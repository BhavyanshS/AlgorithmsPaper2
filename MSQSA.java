import java.util.Random;

public class MSQSA {
    private static final int NUM_THREADS = 4;
    private static final int NUM_OF_RAN_INT = 100000;
    private static int[] globalArray;



    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100); // Random numbers between 0 and 19
        }
        return array;
    }
    public void msqsa(int numThreads, int numRandInt) {
        int N = numRandInt;
        int n = numThreads;
        globalArray = generateRandomArray(numRandInt);
        Thread[] threads = new Thread[numThreads];


        for (int i = 0; i < n; i++) {
            int start = (N / n) * i;
            int end = (i == n - 1) ? N - 1 : (N / n) * (i + 1) - 1;
            threads[i] = new Thread(new IterativeQuickSort(globalArray, start, end));
            threads[i].start();
        }

        // Wait for all threads to complete
        for (int i = 0; i < n; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int interval = N / n;
        while (interval < N) {
            int j = 0;
            while (j + interval < N) {
                int low = j;
                int mid = j + interval - 1;
                int high = Math.min(j + interval * 2 - 1, N - 1);
                merge(globalArray, low, mid, high);
                j += interval * 2;
            }
            interval *= 2;
        }



    }
    public static void merge(int[] array, int low, int mid, int high) {
        int n1 = mid - low + 1;
        int n2 = high - mid;
        // temp arr
        int[] L = new int[n1];
        int[] R = new int[n2];

        /* Copy data to temporary arrays */
        for (int i = 0; i < n1; ++i) {
            L[i] = array[low + i];
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = array[mid + 1 + j];
        }

        /* Merge the temporary arrays back into the original array */
        int i = 0, j = 0;

        // Initial index of merged subarray
        int k = low;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy the remaining elements of L[], if there are any */
        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
        }

        /* Copy the remaining elements of R[], if there are any */
        while (j < n2) {
            array[k] = R[j];
            j++;
            k++;
        }
    }


    public static void main(String[] args) {
        MSQSA msqsa = new MSQSA();
        int runs = 100;
        long totalDuration = 0;

        for (int i = 0; i < runs; i++) {
            long startTime = System.currentTimeMillis();
            msqsa.msqsa(NUM_THREADS, NUM_OF_RAN_INT);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            totalDuration += duration;
        }

        double averageTime = (double) totalDuration / runs;
        System.out.println("Average sorting time: " + averageTime + " ms for threads:" + NUM_THREADS);

    }
}
