import java.util.*;

public class LSH {

    // Number of bands
    int numBands;
    // Number of hash
    int numBuckets;
    // Similarity threshold
    double lshThreshold;

    public LSH(int numBands, int numBuckets, double lshThreshold) {
        this.numBands = numBands;
        this.numBuckets = numBuckets;
        this.lshThreshold = lshThreshold;
    }

    public Set<IndexPair> findSimilarPairs(int[][] signatures) {
        Set<IndexPair> candidates = findCandidatePairs(signatures);
        Set<IndexPair> similar = new HashSet<IndexPair>();
        for(IndexPair pair: candidates) {
            if(CompareSignatures.compareSignatures(signatures[pair.aIndex], signatures[pair.bIndex]) > lshThreshold) {
                similar.add(pair);
            }
        }
        return similar;
    }

    private Set<IndexPair> findCandidatePairs(int[][] signatures) {
        final int r = signatures[0].length/numBands;
        // numBuckets (k) buckets for each of the numBands (b) bands, contains a list of indexes marking the signatures
        ArrayList<Integer>[][] buckets = new ArrayList[numBands][numBuckets];
        for(int i = 0; i < signatures.length; i++) {
            for(int j = 0; j < numBands; j++){
                int hash = hash(Arrays.copyOfRange(signatures[i], j*r, j*r+r));
                if(buckets[j][hash] == null){
                    buckets[j][hash] = new ArrayList<Integer>();
                }
                buckets[j][hash].add(i);
            }
        }

        // Make pairs out of all signatures that hashed into at least one of the same buckets
        Set<IndexPair> candidates = new HashSet<IndexPair>();
        for(int i = 0; i < buckets.length; i++) {
            for(int j = 0; j < buckets.length; j++) {
                if(buckets[i][j] != null){
                    for(Integer a: buckets[i][j]) {
                        for(Integer b: buckets[i][j]) {
                            if(a != b) {
                                candidates.add(new IndexPair(a, b));
                            }
                        }
                    }
                }
            }
        }
        return candidates;
    }

    private int hash (int[] input) {
        int sum = 0;
        for(int i = 0; i < input.length; i++){
            sum += input[i];
        }
        return sum % numBuckets;
    }
}
