package no.fredrfli.http.controller;

import no.fredrfli.http.Request;
import no.fredrfli.http.Response;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 *
 * Controller used to server static content from
 * the registered base url
 */
public class StaticController extends Controller {
    protected String root;

    public StaticController(String root) {
        super();
        this.root = root;
    }

    public String get(Request req, Response res) {

        return null;
    }
}
