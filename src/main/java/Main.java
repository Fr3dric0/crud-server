import controller.IndexController;
import controller.ProtectedController;
import controller.TokenController;
import no.fredrfli.http.Configuration;
import no.fredrfli.http.Server;
import no.fredrfli.http.route.Router;

import java.io.*;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 07.05.2017
 */
public class Main extends Server {

    public static void main(String[] args) throws ClassNotFoundException {
        // Server setup (required)
        // After setup, we can access configuration values through
        // Configuration.getProperties()
        setup(Main.class.getResource("/server.properties"));

        // Database setup (optional)
        //setupDb(Main.class.getResource("/database.properties"));

        Main app = new Main();

        try {
            app.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /**
     * Register urls here
     * @param router
     * */
    @Override
    public void urls(Router router) {
        // Dynamic routes
        router.register("/api", new IndexController());
        router.register("/secure", new ProtectedController()
                .ignoreMethods("POST"));
        router.register("/user/token", new TokenController());

    }

    /**
     *
     * */
    private String getStaticRoot() {
        if (!Configuration.getProperties().containsKey("static.root")) {
            return null;
        }

        String root = (String) Configuration.getProperties().get("static.root");

        return this.getClass().getResource(root).getPath();
    }
}
