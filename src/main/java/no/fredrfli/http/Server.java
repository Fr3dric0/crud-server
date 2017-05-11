package no.fredrfli.http;

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
    public int port;
    public String host;
    protected ServerSocket socket;
    protected static Properties databaseProps;
    private boolean logging = true;

    public static void setup(URL config) throws ClassNotFoundException {
        setupDatabase(config);
    }

    /**
     * Can be used to register urls,
     * by simply overriding the method
     * */
    public void urls(Router router) {}

    public void start() throws IOException {
        start(true);
    }

    public void start(boolean logg) throws IOException {
        this.logging = logg;

        urls(router);

        this.socket = new ServerSocket(this.port);

        System.out.println("Server listening on port: " + this.port);

        while (true) {
            Socket clientSocket = socket.accept();
            try {
                RequestHandler client = new RequestHandler(clientSocket, router, logging);

                Thread thread = new Thread(client);
                thread.start();
            } catch (IOException ioe) {
                // We do not want to forward this exception
                ioe.printStackTrace();
            }
        }
    }

    /**
     *
     *
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
