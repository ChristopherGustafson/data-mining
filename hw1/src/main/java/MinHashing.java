import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.IntStream;

public class MinHashing {
    // Number of used hash functions for signature
    int n;
    // Maximum allowed coefficient for hash function
    int max;
    // Constant to use for hash function h(x) = (a*x + b) % c
    // Should be large prime number
    int c;

    private int[] aCoefficients;
    private int[] bCoefficients;

    public MinHashing(int n, int max, int c) {
        this.n = n;
        this.max = max;
        this.c = c;
        // Generate set of n random coefficients between 0 and max to use in hash function
        this.aCoefficients = getRandomCoefficients(n, c);
        this.bCoefficients = getRandomCoefficients(n, c);
    }

    public int[] buildMinHash(LinkedHashSet<Integer> input) {

        int[] signature = new int[n];
        // For every hash function, apply it to all shingles and find the minimum hash
        // which then is saved as spot i in the signature.
        for(int i = 0; i < n; i++) {
            int minHash = Integer.MAX_VALUE;
            for(int shingle: input) {
                // Calculate hash with the hash function h(x) = (a*x+b) % c where
                // c is a large prime number and a,b are random coefficients
                int hash = Math.floorMod((aCoefficients[i] * shingle + bCoefficients[i]), c);
                if(hash < minHash) minHash = hash;
            }
            signature[i] = minHash;
        }
        return signature;
    }

    private int[] getRandomCoefficients(int n, int max){
        Random rm = new Random(System.currentTimeMillis());
        ArrayList<Integer> coefficients = new ArrayList<Integer>();
        for(int i = 0; i < n; i++) {
            // Generate random coefficient between 1 and max
            Integer rand = rm.nextInt(max);
            // Ensure all coefficients are unique
            while(coefficients.contains(rand)) rand = rm.nextInt(max);
            // Add unique coefficient to list of coefficient
            coefficients.add(rand);
        }
        return coefficients.stream().mapToInt(i -> i).toArray();
    }
}
