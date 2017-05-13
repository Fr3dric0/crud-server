package no.fredrfli.http.controller;

import com.google.gson.Gson;
import no.fredrfli.http.Configuration;
import no.fredrfli.http.Request;
import no.fredrfli.http.Response;
import no.fredrfli.http.exception.MethodNotAllowedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 09.05.2017
 */
public class Controller implements Controllable {
    protected Gson gson = new Gson();

    @Override
    public String get(Request req, Response res) {
        throw new MethodNotAllowedException();
    }

    @Override
    public String post(Request req, Response res) {
        throw new MethodNotAllowedException();
    }

    @Override
    public String put(Request req, Response res) {
        throw new MethodNotAllowedException();
    }

    @Override
    public String delete(Request req, Response res) {
        throw new MethodNotAllowedException();
    }

    @Override
    public String patch(Request req, Response res) {
        throw new MethodNotAllowedException();
    }

    /**
     * Method which should be called by the request handlers.
     * Override this method to add custom pre- or post-
     * controller checks. Like authentication before
     * get() is called.
     *
     * @param req
     * @param res
     * @return String
     * */
    public String getWrapper(Request req, Response res) {
        return this.get(req, res);
    }

    /**
     * Method which should be called by the request handlers.
     * Override this method to add custom pre- or post-
     * controller checks. Like authentication before
     * get() is called.
     *
     * @param req
     * @param res
     * @return String
     * */
    public String postWrapper(Request req, Response res) {
        return this.post(req, res);
    }

    /**
     * Method which should be called by the request handlers.
     * Override this method to add custom pre- or post-
     * controller checks. Like authentication before
     * get() is called.
     *
     * @param req
     * @param res
     * @return String
     * */
    public String putWrapper(Request req, Response res) {
        return this.put(req, res);
    }

    /**
     * Method which should be called by the request handlers.
     * Override this method to add custom pre- or post-
     * controller checks. Like authentication before
     * get() is called.
     *
     * @param req
     * @param res
     * @return String
     * */
    public String deleteWrapper(Request req, Response res) {
        return this.delete(req, res);
    }

    /**
     * Method which should be called by the request handlers.
     * Override this method to add custom pre- or post-
     * controller checks. Like authentication before
     * get() is called.
     *
     * @param req
     * @param res
     * @return String
     * */
    public String patchWrapper(Request req, Response res) {
        return this.patch(req, res);
    }


    /**
     * Will load the file and return it as a `string`.
     * Should the file not exist, will it return `null`.
     *
     * @param path
     * @return String
     * @throws IOException Thrown if a non-natural exception happens,
     *                     FileNotFoundExceptions will return `null`.
     * */
    protected static Optional<String> readFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (Scanner sc = new Scanner(new File(path))) {

            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
                sb.append("\n"); // Keep the new-lines
            }

        } catch (FileNotFoundException fnfe) {
            return Optional.empty();
        }

        return Optional.of(sb.toString());
    }
}
