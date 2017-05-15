package no.fredrfli.http.controller;

import no.fredrfli.http.Configuration;
import no.fredrfli.http.auth.JWTFilter;

/**
 * @author Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created 15.05.2017
 *
 * Ready-to-go controller which provides Json Web Token authentication.
 */
public class JWTAuthController extends AuthController {

    public JWTAuthController () {
        super();

        String secret = "";
        if (Configuration.getProperties().containsKey("tokenSecret")) {
            secret = (String) Configuration.getProperties().get("tokenSecret");
        }

        addFilter(new JWTFilter(secret));
    }


}
