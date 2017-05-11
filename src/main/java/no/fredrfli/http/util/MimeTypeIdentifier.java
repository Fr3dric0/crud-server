package no.fredrfli.http.util;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.regex.Pattern;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
public class MimeTypeIdentifier {
    private static final String HTML_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
    private static Pattern pattern = Pattern.compile(HTML_PATTERN);

    public static boolean isJson(String json) {
        try {
            new JsonParser().parse(json);
        } catch (JsonSyntaxException jse) {
            return false;
        }

        return true;
    }

    public static boolean isHtml(String text) {
        // Dead certainty if it starts with 'DOCTYPE html'='

        return text.startsWith("<DOCTYPE html>") || isXml(text);
    }

    public static boolean isXml(String text) {
        // TODO - a more thorough check
        return pattern.matcher(text).matches();
    }

    public static MimeTypes findContentType(String body) {
        if (isJson(body)) {
            return MimeTypes.APPLICATION_JSON;
        } else if (isHtml(body)) {
            return MimeTypes.HTML;
        } else if (isXml(body)) {
            return MimeTypes.XML;
        } else {
            return MimeTypes.PLAIN;
        }
    }
}
