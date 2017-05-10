package no.fredrfli.http.controller;

import com.google.gson.Gson;
import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.exception.MethodNotAllowedException;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 09.05.2017
 */
public class Controller implements Controllable {
    protected Gson gson = new Gson();

    @Override
    public String get(Request req, Response res) {
        throw new MethodNotAllowedException();
    }

    @Override
    public String post(Request req, Response res) {
        throw new MethodNotAllowedException();
    }

    @Override
    public String put(Request req, Response res) {
        throw new MethodNotAllowedException();
    }

    @Override
    public String delete(Request req, Response res) {
        throw new MethodNotAllowedException();
    }

    @Override
    public String patch(Request req, Response res) {
        throw new MethodNotAllowedException();
    }

    /**
     * Method which should be called by the request handlers.
     * Override this method to add custom pre- or post-
     * controller checks. Like authentication before
     * get() is called.
     *
     * @param req
     * @param res
     * @return String
     * */
    public String getWrapper(Request req, Response res) {
        return this.get(req, res);
    }

    /**
     * Method which should be called by the request handlers.
     * Override this method to add custom pre- or post-
     * controller checks. Like authentication before
     * get() is called.
     *
     * @param req
     * @param res
     * @return String
     * */
    public String postWrapper(Request req, Response res) {
        return this.post(req, res);
    }

    /**
     * Method which should be called by the request handlers.
     * Override this method to add custom pre- or post-
     * controller checks. Like authentication before
     * get() is called.
     *
     * @param req
     * @param res
     * @return String
     * */
    public String putWrapper(Request req, Response res) {
        return this.put(req, res);
    }

    /**
     * Method which should be called by the request handlers.
     * Override this method to add custom pre- or post-
     * controller checks. Like authentication before
     * get() is called.
     *
     * @param req
     * @param res
     * @return String
     * */
    public String deleteWrapper(Request req, Response res) {
        return this.delete(req, res);
    }

    /**
     * Method which should be called by the request handlers.
     * Override this method to add custom pre- or post-
     * controller checks. Like authentication before
     * get() is called.
     *
     * @param req
     * @param res
     * @return String
     * */
    public String patchWrapper(Request req, Response res) {
        return this.patch(req, res);
    }

}
