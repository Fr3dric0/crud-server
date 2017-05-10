import no.fredrfli.http.Server;
import no.fredrfli.http.controller.StaticController;

import java.io.*;

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
        router.register("/api", new IndexController());
        router.register("/static", new StaticController("/"));
    }
}
