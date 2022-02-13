package data_structures;

public class CountPrimes {

    /**
     * Given an integer n, return the number of prime numbers that are strictly less than n.
     * @param n
     * @return number of prime numbers
     */
    public static int countPrimes(int n) {
        // base case, list [0, 1] no primes
        if (n <= 2) return 0;

        int count = 0;
        // sentinel values to distinguish prime nums
        // true: non-prime, false: prime
        boolean[] sentinels = new boolean[n];

        for (int num = 2; num <= (int)Math.sqrt(n); num++) {
            if (sentinels[num] == false) {
                for (int j = num*num; j < n; j += num ) {
                    sentinels[j] = true;
                }
            }
        }

        // count primes
        for (int i = 2; i < n; i++) {
            if (sentinels[i] == false) {
                System.out.print(i + " ");
                count++;
            }
        }
        System.out.println();

        return count;
    }

    public static void main(String[] args) {
        int n = 20;
        int count = countPrimes(n);
        System.out.println("n = " + n + ", count = " + count);
    }
}
