package no.fredrfli.http;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 07.05.2017
 * <p>
 * Class will parse and build http-formatted string
 */
public class Request {
    public static final String CRLF = "\r\n";
    private String version;
    private String method;
    private String uri;

    private String body;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> query = new HashMap<>();

    private List<String> validMethods = Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH", "HEAD");
    private List<String> supportedVersions = Arrays.asList("1.0", "1.1");

    public Request(String req) {
        if (req != null) {
            decode(req);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    /**
     * Will Extract all the necessary components from the HTTP header
     * @param req
     * */
    private void decode(String req) {
        List<String> lines = new ArrayList<>(Arrays.asList(req.split(CRLF)));

        String reqLine = parseRequestLine(lines);

        lines.remove(reqLine); // Remove request-line


        this.headers = parseHeaders(lines);
    }


    private String parseRequestLine(List<String> lines) {
        String line = lines.get(0); // First line should be the request line
        this.method = parseMethod(line);

        List<String> components = Arrays.asList(line.split(" "));

        if (components.size() < 3) {
            throw new IllegalArgumentException(
                    "Request-line ("
                            + line
                            + ") is missing component method, URI or Version");
        }

        this.uri = parseUri(components.get(1));
        this.version = parseVersion(components.get(2));

        return line;
    }



    /**
     *
     * @param component The last item of the request-line
     * @return String protocol and version. E.g. 'HTTP/1.1'
     * */
    private String parseVersion(String component) {
        String[] items = component.split("/");

        // The last element of the Request-line should
        // contain the Protocol and version, sepperated with a slash '/'
        // 'HTTP/1.1' or 'HTTP/1.0'
        if (items.length < 2) {
            throw new IllegalArgumentException(
                    "Expected request-line to include protocol and version."
                    + " Instead got " + component);
        }

        // Check protocol
        if (!items[0].contains("HTTP")) {
            throw new IllegalArgumentException(
                    "Unknown Protocol ("
                    + component
                    + "). Expected HTTP"
            );
        }

        if (!supportedVersions.contains(items[1])) {
            throw new IllegalArgumentException(
                    "Unknown HTTP-version: " + items[1]
            );
        }

        return component;
    }

    private String parseUri(String line) {
        // Extract query params
        String[] components = line.split("\\?");

        if (components[0].getBytes().length > 255) {
            // TODO - Replace with RequestURITooLong (414) [RFC2616 3.2.1]
            throw new IllegalArgumentException("Request uri is too long: " + components[0]);
        }

        // If query-params exists, extract them
        if (components.length > 1) {
            this.query = parseQueryParams(components[1]);
        }

        return components[0];
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();

        List<String> elements = Arrays.asList(query.split("&"));

        for (String elem : elements) {
            List<String> values = Arrays.asList(elem.split("="));

            params.put(
                    values.get(0),
                    values.size() > 1 ? values.get(1) : null
            );
        }

        return params;
    }

    private String parseMethod(String line) throws IllegalArgumentException {
        String method = null;

        for (String m : validMethods) {
            if (line.startsWith(m)) {
                method = m;
            }
        }

        if (method == null) {
            // TODO - Replace with descriptive Http exception
            throw new IllegalArgumentException("Unknown HTTP-method " + line);
        }

        return method;
    }

    private Map<String, String> parseHeaders(List<String> lines) {
        Map<String, String> headers = new HashMap<>();

        for (String line : lines) {
            String[] component = line.split(": ");

            if (component.length < 2) {
                System.err.println("Invalid header: " + line);
                continue; // If a line is invalid, skip the item
            }

            headers.put(component[0].toLowerCase(), component[1]);
        }

        return headers;
    }


    public String getVersion() {
        return version;
    }
}
