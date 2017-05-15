package no.fredrfli.http.controller;

import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.auth.Filter;
import no.fredrfli.http.auth.Filterable;
import no.fredrfli.http.exception.ForbiddenException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 13.05.2017
 */
public class AuthController extends Controller {

    protected List<Filterable> filters = new ArrayList<>();
    protected Set<String> ignore = new HashSet<>();

    /**
     * Adds an authentication filter to
     * the controller.
     * Will be called on every request, which is not ignored
     * through `ignoreMethods(String... methods)`
     *
     * @param filter
     * @return AuthController
     */
    public AuthController addFilter(Filterable filter) {
        if (!this.filters.contains(filter)) {
            this.filters.add(filter);
        }

        return this;
    }

    public AuthController addFilters(List<Filterable> filters) {
        // Adds only filters which haven't been declared
        filters.forEach(this::addFilter);

        return this;
    }

    /**
     * Will ignore authentication on
     * the methods added to this list
     */
    public AuthController ignoreMethods(String... methods) {

        for (String m : methods) {
            m = m.toLowerCase();
            if (!this.ignore.contains(m)) {
                this.ignore.add(m);
            }
        }
        return this;
    }

    @Override
    public String getWrapper(Request req, Response res) {
        if (!ignore.contains("get")) {
            runFilters(req, res);
        }
        return super.getWrapper(req, res);
    }

    @Override
    public String postWrapper(Request req, Response res) {
        if (!ignore.contains("post")) {
            runFilters(req, res);
        }
        return super.postWrapper(req, res);
    }

    @Override
    public String putWrapper(Request req, Response res) {
        if (!ignore.contains("put")) {
            runFilters(req, res);
        }
        return super.putWrapper(req, res);
    }

    @Override
    public String deleteWrapper(Request req, Response res) {
        if (!ignore.contains("delete")) {
            runFilters(req, res);
        }
        return super.deleteWrapper(req, res);
    }

    @Override
    public String patchWrapper(Request req, Response res) {
        if (!ignore.contains("patch")) {
            runFilters(req, res);
        }
        return super.patchWrapper(req, res);
    }

    private void runFilters(Request req, Response res) {
        filters.stream()
                .filter(f -> !f.canActivate(req, res))
                .findFirst()
                .ifPresent((f) -> {
                    throw new ForbiddenException();
                });

//        for (Filterable f : filters) {
//            if (!f.canActivate(req, res)) {
//
//                // Default error-response is 403 Forbidden
//                //
//                // Note: to customize your error-response,
//                // throw HttpExceptions directly from canActivate
//                throw new ForbiddenException();
//            }
//        }

    }
}
