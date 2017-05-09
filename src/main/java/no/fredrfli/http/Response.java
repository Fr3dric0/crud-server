package no.fredrfli.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 08.05.2017
 */
public class Response {
    private static final String CRLF = "\r\n";

    private HttpStatus status = HttpStatus.OK;
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private String version = "HTTP/1.1";

    public Response() {}

    public String encode() {
        StringBuilder response = new StringBuilder();

        response.append(String.format(
                "%s %s%s",
                this.version,
                this.status.getStatus(),
                CRLF)
        );

        //headers.put("Date", new Date().toString());
        //headers.put("Server", "HttpServer");

        response.append(joinHeaders());
        response.append(CRLF);

        response.append(this.body);
        response.append(CRLF);

        return response.toString();
    }


    public Response setBody(String body) {
        this.body = body;

        return this;
    }

    public Response addHeader(String key, String value) {
        this.headers.put(key, value);

        return this;
    }

    public Response setStatus(HttpStatus status) {
        this.status = status;

        return this;
    }

    private String joinHeaders() {
        StringBuilder headers = new StringBuilder();

        this.headers
                .entrySet()
                .forEach(e -> headers.append(Response.headerLine(e)));

        return headers.toString();
    }

    private static String headerLine(Map.Entry<String, String> entry) {
        return String.format("%s: %s%s", entry.getKey(), entry.getValue(), CRLF);
    }
}
