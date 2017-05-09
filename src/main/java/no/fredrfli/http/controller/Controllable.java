package no.fredrfli.http.controller;

import no.fredrfli.http.Request;
import no.fredrfli.http.Response;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 09.05.2017
 */
public interface Controllable {

    String get(Request req, Response res);
    String post(Request req, Response res);
    String put(Request req, Response res);
    String delete(Request req, Response res);
    String patch(Request req, Response res);

}
