package controller;

import no.fredrfli.http.Configuration;
import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.auth.JWT;
import no.fredrfli.http.controller.Controller;
import no.fredrfli.http.model.Token;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 13.05.2017
 */
public class TokenController extends Controller {

    public String post(Request req, Response res) {
        String secret = "";

        if (Configuration.getProperties().containsKey("tokenSecret")) {
            secret = (String) Configuration.getProperties().get("tokenSecret");
        }

        JWT jwt = new JWT(secret);
        String token = jwt.create("bla bla", "FlowerPotServer", "Authorization", 5 * 1000000);

        return gson.toJson(new Token(token), Token.class);
    }
}
