package no.fredrfli.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 13.05.2017
 *
 */
public class Configuration {
    private static final Properties properties = new Properties();
    private static String name;

    public static void loadProperties(String path) {
        try {
            properties.load(new FileInputStream(path));

            if (properties.containsKey("name")) {
                name = properties.get("name").toString();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String toString() {
        return name + "\n\n" + properties.toString();
    }

    public static Properties getProperties() { return properties; }
    public static String getName() { return name; }
}
