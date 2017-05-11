package no.fredrfli.http.util;

import java.util.Arrays;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
public enum MimeTypes {
    APPLICATION_JSON("application/json"),
    HTML("text/html"),
    JPEG("image/jpeg"),
    GIF("image/gif"),
    PLAIN("text/plain"),
    XML("application/xml"),
    OCTET_STREAM("application/octet-stream");

    public final String type;

    MimeTypes(String type) {
        this.type = type;
    }

    public MimeTypes findType(String type) {
        return Arrays.asList(MimeTypes.values())
                .stream()
                .filter(t -> t.equals(type))
                .findFirst()
                .orElseGet(() -> null);
    }

}
