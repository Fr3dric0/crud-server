package no.fredrfli.http.util;

import no.fredrfli.http.exception.HttpException;

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

    /**
     * Finds the first matching MIME-type,
     * for the filepath
     *
     * @param path
     * @return MimeTypes
     * */
    public static MimeTypes matchEnding(String path) {
        if (path == null) {
            return null;
        }

        return Arrays.stream(MimeTypes.values())
                .filter(t -> endsWith(t, path))
                .findFirst()
                .orElse(null);
    }

    /**
     * Helper method for matchEnding.
     * Will check if the path ends with the mimetype
     * @param mime
     * @param path
     * @return boolean
     * */
    private static boolean endsWith(MimeTypes mime, String path) {
        // If no ending exists on type, it matches everything.
        if (mime.endings == null) {
            return true;
        }

        for (String ending : mime.endings) {
            if (path.endsWith(ending)) {
                return true;
            }
        }

        return false;
    }

    public MimeTypes findType(String type) {
        return Arrays.asList(MimeTypes.values())
                .stream()
                .filter(t -> t.equals(type))
                .findFirst()
                .orElseGet(() -> null);
    }

}
