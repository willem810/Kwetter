package util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordHash {
    public static String stringToHash(String input) {
        String output = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            output = bigInt.toString(16);

            //System.out.println(output);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(PasswordHash.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
}
