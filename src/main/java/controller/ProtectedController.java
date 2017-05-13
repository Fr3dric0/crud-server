package controller;

import no.fredrfli.http.Configuration;
import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.auth.JWTFilter;
import no.fredrfli.http.controller.AuthController;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 13.05.2017
 */
public class ProtectedController extends AuthController {

    public ProtectedController() {
        super();

        String token = "";

        if (Configuration.getProperties().containsKey("tokenSecret")) {
            token = (String) Configuration.getProperties().get("tokenSecret");
        }

        addFilter(new JWTFilter(token));
    }

    public String get(Request req, Response res) {
        return "Hello world";
    }
}
