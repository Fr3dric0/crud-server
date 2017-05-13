package no.fredrfli.http.controller;

import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.exception.HttpException;
import no.fredrfli.http.exception.NotFoundException;
import no.fredrfli.http.util.MimeTypes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 *
 * Controller serves static content from
 * the registered base url
 */
public class StaticController extends Controller {
    protected String root;

    public StaticController(String root) {
        super();
        this.root = root;
    }

    public String get(Request req, Response res) {
        String filePath = req.getUri().substring(req.getBaseUrl().length());

        Optional<String> body;
        try {
            body = readFile(this.root + filePath);
        } catch (IOException ioe) {
            // Respond to the user with a 500 error
            throw new HttpException(ioe.getMessage());
        }

        String mimeType = getMimeType(this.root + filePath);
        if (mimeType != null) {
            res.addHeader("Content-Type", mimeType);
        }

        return body.orElseThrow(() ->
                new NotFoundException("Could not find resource: " + filePath));
    }

    /**
     * Will first probe the file for content-type.
     * If this fails, we'll use the file-ending to determine it's type
     *
     * @param path
     * @return String The mime-type
     */
    private String getMimeType(String path) {
        String type = null;

        try {
            type = Files.probeContentType(Paths.get(path));
        } catch (IOException ioe) {
            // Ignore
        }

        if (type == null) {
            MimeTypes t = MimeTypes.matchEnding(path);
            type = t != null ? t.type : null;
        }

        return type;
    }
}
