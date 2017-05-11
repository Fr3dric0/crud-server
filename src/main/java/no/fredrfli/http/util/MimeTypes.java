package no.fredrfli.http.util;

import java.util.Arrays;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
public enum MimeTypes {
    APPLICATION_JSON("application/json", "json"),
    HTML("text/html", "html"),
    JPEG("image/jpeg", "jpg", "jpeg"),
    GIF("image/gif", "git"),
    PLAIN("text/plain", "txt"),
    XML("application/xml", "xml"),
    MARKDOWN("text/markdown; charset=UTF-8", "md"),
    OCTET_STREAM("application/octet-stream", null);

    public final String type;
    public final String[] endings;

    MimeTypes(String type, String... endings) {
        this.type = type;
        this.endings = endings;
    }

    public static MimeTypes matchEnding(String path) {
        return Arrays.asList(MimeTypes.values())
                .stream()
                .filter(t -> {

                    for (String ending : t.endings) {
                        if (path.endsWith(ending)) {
                            return true;
                        }
                    }

                    return false;
                })
                .findFirst()
                .orElseGet(() -> null);
    }

    public MimeTypes findType(String type) {
        return Arrays.asList(MimeTypes.values())
                .stream()
                .filter(t -> t.equals(type))
                .findFirst()
                .orElseGet(() -> null);
    }

}
