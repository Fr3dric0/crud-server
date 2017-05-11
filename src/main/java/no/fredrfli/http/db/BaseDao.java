package no.fredrfli.http.db;

import java.sql.*;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 11.05.2017
 */
public class BaseDao<T> {
    public static Properties connectionProps;
    private Class<T> type;
    private String table;

    public BaseDao(String table, Class<T> type) {
        Objects.requireNonNull(
                type,
                "Requires the model-type, to handle database queries");
        checkDatabaseConnection(); // Ensure connectionProps exists

        this.table = table;
        this.type = type;
    }

    public boolean saveQuery(Supplier<String> supplier) {
        try (Connection c =
                     DriverManager.getConnection(
                             connectionProps.get("url").toString(),
                             connectionProps)) {

            Statement s = c.createStatement();

            s.executeUpdate(supplier.get());
        } catch (SQLException sqle) {
            // Ignore
            return false;
        }

        return true;
    }

    /**
     *
     *
     * */
    public ResultSet findQuery(Supplier<String> supplier) {
        try (Connection c =
                     DriverManager.getConnection(
                             connectionProps.get("url").toString(),
                             connectionProps)) {

            Statement s = c.createStatement();

            return s.executeQuery(supplier.get());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    public List<T> find() {
        ResultSet match = findQuery(
                () -> String.format("SELECT * FROM %s", table)
        );


        return null;
    }

    public T findById(long id) {

        return null;
    }

    public T create(T obj) {
        return obj;
    }

    public T update(T obj) {

        return obj;
    }

    public T destroy(T obj) {

        return obj;
    }


    private void checkDatabaseConnection() throws IllegalStateException {
        if (connectionProps == null) {
            throw new IllegalStateException(
                    "Missing required database properties");
        }

        // Validate the necessary keys exists
    }

}
