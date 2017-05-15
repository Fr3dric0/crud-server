package no.fredrfli.http;

import no.fredrfli.http.controller.Controller;
import no.fredrfli.http.exception.HttpException;
import no.fredrfli.http.exception.MethodNotAllowedException;
import no.fredrfli.http.exception.NotFoundException;
import no.fredrfli.http.route.Router;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 07.05.2017
 */
public class RequestHandler implements Runnable {
    private static final String CRLF = "\r\n";

    private Socket socket;

    private Router router;

    private OutputStream out;
    private BufferedReader br;

    private boolean logging = true;
    private Date startTime;

    public RequestHandler(Socket socket, Router router, boolean logging) throws IOException {
        startTime = new Date();

        this.socket = socket;
        this.out = socket.getOutputStream();
        this.br = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        this.router = router;
        this.logging = logging;
    }

    /**
     * Handles the request
     *
     * */
    @Override
    public void run() {
        Request req = new Request(processRequest());
        Response res = new Response();

        Controller ctrl = router.find(req);

        try {
            res = this.delegate(ctrl, req, res);

        // Catches all HttpExceptions, which might have been thrown
        // from the controllers
        } catch (HttpException e) {
            res.setStatus(e.getStatus());

            if (e.getMessage() != null && e.getMessage().length() > 0) {
                res.addHeader("Content-Type", "application/json");
                res.setBody(String.format(
                        "{\"error\": \"%s\"}",
                        e.getMessage()));
            } else {
                res.setBody("");
            }

        }

        send(req, res); // Respond to client

        closeStreams(); // Close used streams
    }

    /**
     * Sends a response back to the server
     *
     * @param req Used for loggging
     * @param res
     * */
    private void send(Request req, Response res) {
        try {
            out.write(res.encode().getBytes());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (logging) {
            logg(req, res);
        }
    }

    private void logg(Request req, Response res) {
        // Process time
        long timeTaken = new Date().getTime() - startTime.getTime();

        String template = "%s - %s %s [%sms] %s%n";

        PrintStream out;
        if (res.getStatus().getCode() >= 400) {
            out = System.err;
        } else {
            out = System.out;
        }

        out.printf(template,
                startTime,
                req.getMethod(),
                req.getUri(),
                timeTaken,
                res.getStatus().getCode()
        );
    }

    /**
     * Responsible for calling the correct methods
     * on the controller
     *
     * @param ctrl
     * @param req
     * @param res
     * @return Response res
     */
    private Response delegate(Controller ctrl, Request req, Response res) throws HttpException {
        if (ctrl == null) {
            throw new NotFoundException(
                    String.format(
                            "Cannot find %s %s",
                            req.getMethod(),
                            req.getUri()));
        }

        String body;
        switch (req.getMethod()) {
            case "GET":
                body = ctrl.getWrapper(req, res);
                break;
            case "POST":
                body = ctrl.postWrapper(req, res);
                break;
            case "PUT":
                body = ctrl.putWrapper(req, res);
                break;
            case "PATCH":
                body = ctrl.patchWrapper(req, res);
                break;
            case "DELETE":
                body = ctrl.deleteWrapper(req, res);
                break;
            default:
                throw new MethodNotAllowedException();
        }

        res.setBody(body);
        return res;
    }

    /**
     * Will read the http-request from the socket
     * @return String Http-request
     * */
    private String processRequest() {
        StringBuilder sb = new StringBuilder();
        int contentLength = 0;

        try {
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);

                if (s.startsWith("Content-Length: ")) {
                    int idx = s.indexOf(":") + 1;
                    contentLength = Integer.parseInt(
                            s.substring(idx).trim());
                }

                if (s.isEmpty()) {
                    break;
                }

                sb.append(CRLF); // Give each header it's own line
            }

            if (contentLength > 0) {
                sb.append(CRLF);
                sb.append(parseBody(br, contentLength));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        return sb.toString();
    }

    /**
     * Extracts the body from the rest of the request
     * @param br
     * @param length
     * @return String
     * @throws IOException Thrown from br.read()
     * */
    private String parseBody(BufferedReader br, int length) throws IOException {
        int read;
        StringBuilder body = new StringBuilder();

        while((read = br.read()) != -1) {
            body.append((char) read);

            if (body.length() >= length) {
                break;
            }
        }

        return body.toString();
    }

    /**
     * Closes every stream
     * */
    private void closeStreams() {
        try {
            out.close();
            br.close();
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // Ignore handling
        }
    }
}
