package no.fredrfli.http;

import no.fredrfli.http.controller.StaticController;
import no.fredrfli.http.db.BaseDao;
import no.fredrfli.http.route.Router;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Properties;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
public abstract class Server {
    protected Router router = new Router();

    /**
     * Port-number. Can be set through server.properties
     * of by directly setting the a value to `port`.
     * Default port is 8000
     * */
    public int port = -1; // Uninitialized port value

    /**
     * Socket which the server runs on
     *
     * */
    protected ServerSocket socket;

    /**
     * Properties for database setup.
     * Extracted from `database.properties`,
     * when setupDb() is called
     * */
    protected static Properties databaseProps;

    /**
     * Decides if requests should be logged
     * or not. Can be set in `server.properties`
     * */
    private boolean logging = true;

    /**
     * Default value for the location of
     * the static resources. `static.root`
     * in `server.properties`
     * will override this.
     * */
    protected String staticRoot = "/static";

    /**
     * Loads the values of `server.properties`
     * into Configuration.
     *
     * @param url Path to `server.properties`
     * */
    public static void setup(URL url) {
        Configuration.loadProperties(url.getPath());
    }

    /**
     * Will initiate a database connection,
     * with the values specified in `database.properties`.
     * The properties is stored in BaseDao and `databaseProps`
     * */
    public static void setupDb(URL config) throws ClassNotFoundException {
        setupDatabase(config);
    }

    /**
     * Can be used to register urls,
     * by simply overriding the method
     * */
    public void urls(Router router) {}

    /**
     * Initiates the HTTP-server
     * */
    public void start() throws IOException {
        if (Configuration.getProperties().containsKey("logging")) {
            start(
                    !Configuration.getProperties().get("logging")
                    .equals("false")
            );
        } else {
            start(true);
        }
    }

    /**
     * Initiates the HTTP-server
     *
     * @param log Tells the server should log requests or not
     * */
    public void start(boolean log) throws IOException {
        this.logging = log;

        urls(router);
        registerStaticRoute();

        int port = getPort();
        this.socket = new ServerSocket(port);

        System.out.println("Server listening on port: " + port);

        while (true) {
            Socket clientSocket = socket.accept();
            try {
                // Will parse and respond to the requests
                RequestHandler client = new RequestHandler(clientSocket, router, logging);

                // Give each request it's own thread
                Thread thread = new Thread(client);
                thread.start();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Will register a StaticController
     * if server.properties contains `static.url`
     *
     *
     * */
    private void registerStaticRoute() {
        Properties props = Configuration.getProperties();
        String root = (String) props.getOrDefault(
                "static.root",
                "/static");

        if (props.containsKey("static.url")) {
            router.register(
                    (String) props.get("static.url"),
                    new StaticController(
                            this.getClass().getResource(root).getPath())
            );
        }
    }

    /**
     * Will set the port number for the socket.
     * Default value is 8000.
     * You can set the port number through server.properties
     * or by directly setting it through this.port.
     *
     * @return int
     * @throws IllegalArgumentException If the port-number is invalid
     * */
    private int getPort() {
        int port = 8000;

        if (Configuration.getProperties().containsKey("port")) {
            port = Integer.parseInt((String) Configuration.getProperties().get("port"));
        }

        if (this.port > -1) {
            port = this.port;
        }

        if (port != 80 && port != 443 && port < 1024) {
            throw new IllegalArgumentException("Illegal port-number: " + port);
        }

        return port;
    }

    /**
     * Will initiate a database connection,
     * and save the properties in BaseDao
     *
     * @param path
     * */
    private static void setupDatabase(URL path) throws ClassNotFoundException {
        databaseProps = getProperties(path);

        if (databaseProps == null || !databaseProps.containsKey("driver")) {
            return;
        }

        Class.forName(databaseProps.get("driver").toString());

        BaseDao.connectionProps = databaseProps;
    }

    /**
     *
     * @param path
     * @return Properties
     * */
    private static Properties getProperties(URL path) {
        try (FileInputStream in = new FileInputStream(
                new File(path.getPath()))) {

            Properties props = new Properties();
            props.load(in);

            return props;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
