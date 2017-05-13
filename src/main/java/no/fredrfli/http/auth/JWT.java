package no.fredrfli.http.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.SignatureException;
import java.util.Date;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 13.05.2017
 *
 * Generalized class used to validate and create Json Web Tokens
 */
public class JWT {
    private byte[] secretBytes;
    private static final SignatureAlgorithm algorithm = SignatureAlgorithm.HS512;

    public JWT(String secret) {
        secretBytes = DatatypeConverter.parseBase64Binary(secret);
    }

    /**
     * Will create a JWT with the provided parameters
     *
     * @param id Identifier
     * @param iss Issuer
     * @param sub Subject
     * @param ttl Time To Live
     * @return String
     * */
    public String create(String id, String iss, String sub, long ttl) {
        // Get issuing-date
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // Our signing-key for the token
        Key key = new SecretKeySpec(secretBytes, algorithm.getJcaName());

        // Build the token
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(sub)
                .setIssuer(iss)
                .signWith(algorithm, key);

        // Add Time To Live, if it exists
        if (ttl >= 0) {
            Date exp = new Date(nowMillis + ttl);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    /**
     * Will attempt to validate the provided token
     * @param token Encoded token
     * */
    public void verify(String token) throws SignatureException {
        Jwts.parser()
                .setSigningKey(secretBytes)
                .parseClaimsJws(token).getBody();
    }

}
