package no.fredrfli.http.route;

import no.fredrfli.http.Request;
import no.fredrfli.http.controller.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 09.05.2017
 */
public class Router {
    // urls and controllers must be of equal length
    private List<String> urls = new ArrayList<>();
    private List<Controller> controllers = new ArrayList<>();

    public Router() {}

    /**
     * Based on the urls registered,
     * (parameters is not parsed),
     * will find the first closest matching url
     *
     * Example:
     * urls <- {"/user", "/demographics/people"}
     * controllers <- {UserController, DemoController}
     *
     * url <- "/user"
     * find(url) -> UserController
     *
     * url <- "/user/jon.snow"
     * find(url) -> UserController
     *
     * url <- "/demographics/user"
     * find(url) -> DemoController
     *
     * @param req
     * @return Controller   The controller to run
     * */
    public Controller find(Request req) {
        String url = req.getUri();

        url = (!url.startsWith("/") ? "/": "") + url;

        for (int i = 0 ; i < urls.size(); i++) {
            String u = urls.get(i);

            // Find the first match
            if (url.startsWith(u)) {
                req.setBaseUrl(u); // attach base-url to request
                return controllers.get(i);
            }
        }

        return null;
    }

    /**
     * Registers a controller to a base-url
     *
     * @param url
     * @param ctrl
     * @return Router
     * */
    public Router register(String url, Controller ctrl) {
        if (!url.startsWith("/")) {
            url = "/" + url;
        }

        if (urls.contains(url)) {
            throw new IllegalArgumentException("A controller is already registered to the url: " + url);
        }

        this.urls.add(url);
        this.controllers.add(ctrl);

        return this;
    }
}
