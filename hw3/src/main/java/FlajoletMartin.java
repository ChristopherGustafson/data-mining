import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

public class FlajoletMartin {

    long a = 3924;
    long b = 9231;
    long c = 875713;

    long distinct;

    public long getDistinct() {
        return distinct;
    }

    // Add a collection of elements to the counter
    public void add(Collection<Integer> elements) {
        long maxZeroes = 0;
        for(Integer ele: elements) {
            // Hash the
            String binary = hash(ele);
            long trailingZeroes = 0;
            for(int i = binary.length()-1; i > -1; i--){
                if(binary.charAt(i) != '0'){
                    break;
                }
                trailingZeroes++;
            }
            if(trailingZeroes > maxZeroes) {
                maxZeroes = trailingZeroes;
            }
        }
        // Return 2^maxZeroes (by using bit shifting), approximating number of distinct elements
        distinct = 1L << maxZeroes > distinct ? 1L << maxZeroes : distinct;
    }

    public void add(Integer element) {
        String binary = hash(element);
        long trailingZeroes = 0;
        for(int i = binary.length()-1; i > -1; i--){
            if(binary.charAt(i) != '0'){
                break;
            }
            trailingZeroes++;
        }
        // Return 2^maxZeroes (by using bit shifting), approximating number of distinct elements
        distinct = 1L << trailingZeroes > distinct ? 1L << trailingZeroes : distinct;
    }

    // Hash integer element into bit sequence using sha-256
    private String hash(Integer element) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(element.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < hash.length; i++) {
                sb.append(String.format("%8s", Integer.toBinaryString(hash[i] & 0xFF)).replace(' ', '0'));
            }
            return sb.toString();
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
