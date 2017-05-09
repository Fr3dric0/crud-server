import no.fredrfli.http.HttpRequestHandler;
import no.fredrfli.http.controller.Controller;
import no.fredrfli.http.route.Router;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.*;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 07.05.2017
 */
public class Main {

    private static Router router = new Router();


    public static void main(String[] args) throws IOException {
        int port = 8080;

        router.register("/api", new Controller());

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server running on port: " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            try {
                HttpRequestHandler client = new HttpRequestHandler(clientSocket, router);

                Thread thread = new Thread(client);

                thread.start();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }
    }
}
