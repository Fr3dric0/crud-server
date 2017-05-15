package no.fredrfli.http.auth;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.exception.ForbiddenException;
import no.fredrfli.http.exception.UnauthorizedException;

import java.util.Objects;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 13.05.2017
 *
 * Filter to validate a Json Web Token (JWT).
 * Expected format is
 */
public class JWTFilter extends Filter {
    private String secret;
    private String prefix = "bearer";
    private JWT jwt;

    public JWTFilter(String secret, int priority) {
        Objects.requireNonNull(secret,"Missing token-secret");

        this.priority = priority;

        this.secret = secret;
        jwt = new JWT(this.secret);
    }

    public JWTFilter(String secret) {
        this(secret, 0);
    }

    /**
     * Filter method.
     * Will check if the authorization header exists,
     * check if the token is prefixed with bearer,
     * and ensures the token is valid.
     *
     * @return boolean
     * */
    @Override
    public boolean canActivate(Request req, Response res) {
        requireTokenExistence(req);
        requireTokenBearer(req);
        requireValidToken(req);

        return true;
    }

    private void requireTokenExistence(Request req) {
        if (!req.getHeaders().containsKey("authorization") &&
                !req.getHeaders().containsKey("Authorization")) {
            throw new UnauthorizedException("Missing authorization header");
        }
    }

    private void requireTokenBearer(Request req) {
        String token;

        try {
            token = req.getHeaders().get("authorization");
        } catch(Exception e) {
            token = req.getHeaders().get("Authorization");
        }

        if (!token.toLowerCase().startsWith(prefix)) {
            throw new UnauthorizedException(
                    "Authorization header must be prefixed with '" + prefix + "'");
        }
    }

    private void requireValidToken(Request req) {
        String token = req.getHeaders()
                .get("authorization")
                .substring(prefix.length() + 1);

        try {
            jwt.verify(token);
        } catch(SignatureException se) {
            throw new ForbiddenException("Invalid token");
        } catch (ExpiredJwtException ie) {
            throw new ForbiddenException("Expired token");
        }
    }

    public String toString(String token) {
        return String.format("bearer %s", token);
    }

}
