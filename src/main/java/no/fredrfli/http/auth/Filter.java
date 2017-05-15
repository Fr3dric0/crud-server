package no.fredrfli.http.auth;

import no.fredrfli.http.Request;
import no.fredrfli.http.Response;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 13.05.2017
 */
public abstract class Filter implements Comparable<Filterable>, Filterable {
    protected int priority = 0;

    public abstract boolean canActivate(Request req, Response res);

    public int getPriority() { return priority; }

    @Override
    public String toString() {
        return "Filter{priority=" + priority + '}';
    }

    @Override
    public int compareTo(Filterable f) {
        if (f instanceof Filter) {
            // We want the item with highest priority to come first
            return ((Filter) f).getPriority() - this.priority;
        }

        return -1; // Ours will by default have higher priority
    }
}
