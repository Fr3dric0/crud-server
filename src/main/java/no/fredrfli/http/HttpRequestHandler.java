package no.fredrfli.http;

import no.fredrfli.http.controller.Controller;
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

    private InputStream in;
    private OutputStream out;

    private BufferedReader br;

    public HttpRequestHandler(Socket socket, Router router) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.br = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        this.router = router;
    }

    @Override
    public void run() {
        Request req = new Request(processRequest());
        Response res = new Response();

        Controller ctrl = router.find(req.getUri());

        if (ctrl == null) {
            res.setStatus(HttpStatus.NOT_FOUND)
                    .addHeader("Content-Type", "application/json")
                    .setBody(String.format(
                            "{\"error\": \"Cannot find %s %s\"}",
                            req.getMethod(),
                            req.getUri()));
        } else {
            // TODO - Waay to much try/catch blocks here..
            try {
                res = this.delegate(ctrl, req, res);
            } catch (Exception e) {
                // TODO - Replace with HttpException
                e.printStackTrace();
            }
        }

        try {
            out.write(res.encode().getBytes());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        closeStreams();
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
    private Response delegate(Controller ctrl, Request req, Response res) throws Exception {

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
                res.setStatus(HttpStatus.METHOD_NOT_ALLOWED)
                        .addHeader("Content-Type", "application/json");

                body = String.format(
                        "{\"error\": \"Method '%s' on '%s' is not allowed\"}",
                        req.getMethod(),
                        req.getUri());
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
