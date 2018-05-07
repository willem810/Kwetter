package util;

import domain.Group;
import domain.User;
import io.jsonwebtoken.*;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Stateless
public class AuthController {
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    private static final int JWT_DURATION_MIN = 30;

    public String generateJWT(User u) throws Exception {
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date expirationDate = new Date(t + (JWT_DURATION_MIN * 60000));


        ArrayList<String> groups = new ArrayList<>();
        for (Group g : u.getGroups()) {
            groups.add(g.getName());
        }

        String jwtToken = Jwts.builder()
                .setSubject(u.getUsername())
                .claim("groups", groups)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, JWTStore.getKey())
                .compact();
        return jwtToken;
    }

    public String getUserFromJWT(String bearer) throws Exception {
        String token = bearer
                .substring(AUTHENTICATION_SCHEME.length()).trim();

        byte[] key = JWTStore.getKey();
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token).getBody().getSubject();

    }
}
