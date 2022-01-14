import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

public class HyperLogLog {

    long ha = 3924;
    long hb = 9231;
    long hc = 875713;

    private long[] m;
    private final int b = 5;

    public HyperLogLog() {
        // Create an array for registers of size 2^b
        m = new long[1 << b];
    }

    public HyperLogLog(long[] m) {
        this.m = m;
    }

    // Return the long array which stores the counter.
    public long[] getM() {
        return m;
    }

    // Add single element to the counter set
    public void add(Integer element) {
        String binary = hash(element);
        int i = Integer.parseInt(binary.substring(0, b), 2);
        long leadingZeroes = leadingZeroes(binary.substring(b, binary.length()));
        m[i] = Math.max(m[i], (leadingZeroes+1));
    }

    // Add collection of elements to the counter set
    public void add(Collection<Integer> elements) {
        for(Integer ele: elements) {
            add(ele);
        }
    }

    // Return approximated size of the counter set, by the formula alfa * p^2 * sum(2^-m[j])^-1
    // as described in the HyperBall paper
    public double size() {
        double z = 0;
        for(int j = 0; j < m.length; j++) {
            z += Math.pow(2, -1*m[j]);
        }
        return Math.floor(alfa() * Math.pow(m.length, 2) * 1/z);
    }

    // Calculate number of leading zeroes in binary sequence string
    private long leadingZeroes(String binarySequence) {
        long leadingZeroes = 0;
        for(int i = 0; i < binarySequence.length(); i++){
            if(binarySequence.charAt(i) != '0'){
                break;
            }
            leadingZeroes++;
        }
        return leadingZeroes;
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

    // return alfa constant for size calculation, as given by the HyperLogLog paper
    private double alfa() {
        switch(m.length){
            case 16:
                return 0.673;
            case 32:
                return 0.697;
            case 64:
                return 0.709;
            default:
                return 0.7213/(1 + 1.079/m.length);
        }
    }
}
