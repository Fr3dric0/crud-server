package no.fredrfli.http.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 08.05.2017
 */
public enum HttpStatus {
    OK(200, "200 OK"),
    CREATED(200, "201 CREATED"),
    NO_CONTENT(204, "204 NO CONTENT"),
    UNAUTHORIZED(401, "401 UNAUTHORIZED"),
    FORBIDDEN(403, "403 FORBIDDEN"),
    NOT_FOUND(404, "404 NOT FOUND"),
    METHOD_NOT_ALLOWED(405, "405 Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "500 Internal Server Error");

    private final int code;
    private final String status;

    HttpStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public HttpStatus valueOf(int code) {
        List<HttpStatus> values = Arrays.asList(HttpStatus.values());

        return values.stream()
                .filter(e -> e.getCode() == code)
                .findFirst()
                .orElseGet(() -> null);
    }

    public List<Integer> getCodes() {
        List<HttpStatus> values = Arrays.asList(HttpStatus.values());

        return values.stream()
                .map(HttpStatus::getCode)
                .collect(Collectors.toList());
    }

    public int getCode() { return code; }
    public String getStatus() { return status; }
}
