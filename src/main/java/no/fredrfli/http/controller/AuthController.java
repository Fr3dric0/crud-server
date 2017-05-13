package no.fredrfli.http.controller;

import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.auth.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 13.05.2017
 */
public class AuthController extends Controller {

    protected List<Filter> filters = new ArrayList<>();

    /**
     * Adds single filtes
     * @param filter
     * @return AuthController
     * */
    public AuthController addFilter(Filter filter) {
        if (!this.filters.contains(filter)) {
            this.filters.add(filter);
        }

        return this;
    }

    public AuthController addFilters(List<Filter> filters) {
        // Adds only filters which haven't been declared
        filters.forEach(this::addFilter);

        return this;
    }


    @Override
    public String getWrapper(Request req, Response res) {
        runFilters(req, res);
        return super.getWrapper(req, res);
    }

    @Override
    public String postWrapper(Request req, Response res) {
        runFilters(req, res);
        return super.postWrapper(req, res);
    }

    @Override
    public String putWrapper(Request req, Response res) {
        runFilters(req, res);
        return super.putWrapper(req, res);
    }

    @Override
    public String deleteWrapper(Request req, Response res) {
        runFilters(req, res);
        return super.deleteWrapper(req, res);
    }

    @Override
    public String patchWrapper(Request req, Response res) {
        runFilters(req, res);
        return super.patchWrapper(req, res);
    }

    private void runFilters(Request req, Response res) {
        // Expects a failing filter to throw an exeption
        for (Filter f : filters) {
            f.canActivate(req, res);
        }

    }
}
