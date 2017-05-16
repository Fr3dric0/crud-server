package controller;

import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.controller.JWTAuthController;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 13.05.2017
 */
public class ProtectedController extends JWTAuthController {

    public ProtectedController() {
        super();
    }

    @Override
    public String get(Request req, Response res) {

        return "Hello world";
    }
}
