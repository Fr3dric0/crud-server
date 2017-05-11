package no.fredrfli.http.controller;

import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.exception.HttpException;
import no.fredrfli.http.exception.NotFoundException;
import no.fredrfli.http.util.MimeTypes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 *
 * Controller used to server static content from
 * the registered base url
 */
public class StaticController extends Controller {
    protected String root;

    public StaticController(String root) {
        super();
        this.root = root;
    }

    public String get(Request req, Response res) {
        String filePath = req.getUri().replace(req.getBaseUrl(), "");

        try (BufferedReader br = new BufferedReader(
                new FileReader(this.root + filePath))) {

            String mimeType = getMimeType(this.root + filePath);
            res.addHeader("Content-Type", mimeType);

            StringBuilder sb = new StringBuilder();

            String s;
            while((s = br.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }

            return sb.toString();

        } catch (FileNotFoundException fnfe) {
            throw new NotFoundException("Could not find resource: " + filePath);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new HttpException(ioe.getMessage());
        }
    }

    /**
     * Will first prope the file for content-type.
     * If this fails, we use the fileending to determine the type
     * @param path
     * @return String The mime-type
     * */
    private String getMimeType(String path) throws IOException {
        String type = Files.probeContentType(Paths.get(path));

        if (type == null) {
            MimeTypes t = MimeTypes.matchEnding(path);
            type = t != null ? t.type : null;
        }

        return type;
    }
}
