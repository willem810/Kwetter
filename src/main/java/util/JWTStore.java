package util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JWTStore {
    private static final Instant CURRENT_TIME = Instant.now();
    private static final Instant EXPIRED_TIME = CURRENT_TIME.plus(3, ChronoUnit.MINUTES);

    private static byte[] key;

    public static byte[] getKey() throws Exception{
        if(key == null) {
            key  = generateKey();
        }

        return key;

//        byte[] key = "secret".getBytes("UTF-8");
//        return key;
    }

    private static byte[] generateKey() throws Exception {
//        String keyString = "simpleKeyForKwetter";
//        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
//        return key;


        return "secret".getBytes("UTF-8");
    }


}
