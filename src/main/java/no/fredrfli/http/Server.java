package no.fredrfli.http;

import no.fredrfli.http.route.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
public abstract class Server {
    protected Router router = new Router();
    public int port;
    public String host;
    protected ServerSocket socket;

    /**
     * Can be used to register urls,
     * by simply overriding the method
     * */
    public void urls() {}

    public void start() throws IOException {
        urls();

        this.socket = new ServerSocket(this.port);

        System.out.println("Server listening on port: " + this.port);

        while (true) {
            Socket clientSocket = socket.accept();

            try {
                HttpRequestHandler client = new HttpRequestHandler(clientSocket, router);

                Thread thread = new Thread(client);
                thread.start();
            } catch (IOException ioe) {
                // We do not want to forward this exception
                ioe.printStackTrace();
            }
        }
    }
}
