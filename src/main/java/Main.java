import no.fredrfli.http.HttpRequestHandler;
import no.fredrfli.http.Server;
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
public class Main extends Server {

    public static void main(String[] args) {
        Main app = new Main();
        app.port = 8080;

        try {
            app.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /**
     * Register urls here
     *
     * */
    public void urls() {
        router.register("/api", new Controller());
    }
}
