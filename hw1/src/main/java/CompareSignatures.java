public class CompareSignatures {

    public static double compareSignatures(int[] signatureA, int[] signatureB) {
        // Signatures should be of equal length
        assert signatureA.length == signatureB.length;

        // Count number of signature rows where the two signatures agree
        double equalRows = 0.0;
        for(int i = 0; i < signatureA.length; i++) {
            if(signatureA[i] == signatureB[i]){
                equalRows++;
            }
        }
        // Return the fraction of all the rows where the signatures agree
        return equalRows/signatureA.length;
    }
}
