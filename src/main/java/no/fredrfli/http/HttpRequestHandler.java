package no.fredrfli.http;

import no.fredrfli.http.controller.Controller;
import no.fredrfli.http.exception.HttpException;
import no.fredrfli.http.exception.MethodNotAllowedException;
import no.fredrfli.http.exception.NotFoundException;
import no.fredrfli.http.route.Router;

import java.io.*;
import java.net.Socket;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 07.05.2017
 */
public class HttpRequestHandler implements Runnable {
    private static final String CRLF = "\r\n";

    private Socket socket;

    private Router router;

    private OutputStream out;
    private BufferedReader br;

    public HttpRequestHandler(Socket socket, Router router) throws IOException {
        this.socket = socket;
        this.out = socket.getOutputStream();
        this.br = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        this.router = router;
    }

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
                res.setBody(e.getMessage());
            } else {
                res.setBody("");
            }

        }

        send(res); // Respond to client

        closeStreams(); // Close used streams
    }

    /**
     * Sends the response back
     *
     * @param res
     * */
    private void send(Response res) {
        try {
            out.write(res.encode().getBytes());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Responsible for calling the correct method
     * ont the controller
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
                            "{\"error\": \"Cannot find %s %s\"}",
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

    private String processRequest() {
        String s;
        StringBuilder sb = new StringBuilder();

        try {
            while ((s = br.readLine()) != null) {
                sb.append(s);

                if (s.isEmpty()) {
                    break;
                }

                sb.append(CRLF); // Give each header it's own line
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();

            return null;
        }

        return sb.toString();
    }

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
