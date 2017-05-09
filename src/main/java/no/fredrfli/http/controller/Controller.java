package no.fredrfli.http.controller;

import no.fredrfli.http.Request;
import no.fredrfli.http.Response;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 09.05.2017
 */
public class Controller implements Controllable {

    @Override
    public String get(Request req, Response res) {

        return "{\"title\":\"Hello World\"}";
    }

    @Override
    public String post(Request req, Response res) {
        return null;
    }

    @Override
    public String put(Request req, Response res) {
        return null;
    }

    @Override
    public String delete(Request req, Response res) {
        return null;
    }

    @Override
    public String patch(Request req, Response res) {
        return null;
    }

    public String getWrapper(Request req, Response res) {

        return this.get(req, res);
    }

    public String postWrapper(Request req, Response res) {

        return this.post(req, res);
    }

    public String putWrapper(Request req, Response res) {

        return this.put(req, res);
    }

    public String deleteWrapper(Request req, Response res) {

        return this.delete(req, res);
    }

    public String patchWrapper(Request req, Response res) {

        return this.patch(req, res);
    }
}
