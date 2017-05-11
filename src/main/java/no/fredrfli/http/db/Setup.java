package no.fredrfli.http.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
public class Setup {
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private String host = "jdbc:mysql://localhost/HelloWorld";

    private String user = "username";
    private String pass = "password";

    public Setup(String url) throws IOException {
        loadConfig(url);
    }

    public void connect() {

    }


    private void loadConfig(String url) throws IOException {
        Properties props = new Properties();

        InputStream input = new FileInputStream(url);
        props.load(input);


        if (props.containsKey("dbhost") ||
                props.containsKey("dbname") ||
                props.containsKey("dbuser") ||
                props.containsKey("dbpass")) {
            throw new IllegalArgumentException("Missing property 'dbhost', 'dbname', 'dbuser' or 'dbpass'");
        }

        host = String.format("jdbc:%s", props.getProperty("dbhost"));

        if (!host.endsWith("/")) {
            host += "/";
        }

        host += props.getProperty("dbname");

        user = props.getProperty("dbuser");
        pass = props.getProperty("dbpass");
    }
}
