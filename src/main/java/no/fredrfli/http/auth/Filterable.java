package no.fredrfli.http.auth;

import no.fredrfli.http.Request;
import no.fredrfli.http.Response;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 15.05.2017
 */
public interface Filterable {
    boolean canActivate(Request req, Response res);
}
