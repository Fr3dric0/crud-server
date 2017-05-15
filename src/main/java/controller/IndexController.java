package controller;

import model.HelloWorld;
import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.controller.Controller;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 09.05.2017
 */
public class IndexController extends Controller {

    @Override
    public String get(Request req, Response res) {

        HelloWorld h = gson.fromJson(req.getBody(), HelloWorld.class);

        return gson.toJson(h, HelloWorld.class);
    }

    @Override
    public String post(Request req, Response res) {
        //HelloWorld h = new HelloWorld().fromJson(req.getBody());

        return req.getBody();
    }
}
