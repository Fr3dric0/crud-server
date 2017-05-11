package model;

import jdk.nashorn.internal.ir.annotations.Ignore;
import no.fredrfli.http.db.BaseDao;
import no.fredrfli.http.model.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
@Entity
public class HelloWorld extends Model {

    @Transient
    private static BaseDao<HelloWorld> repo =
            new BaseDao<>("helloworld", HelloWorld.class);

    @Column(name = "`title`")
    private String title;

    @Column(name = "`description`")
    private String description;

    public HelloWorld() {}

    public HelloWorld(String title, String description) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);

        this.title = title;
        this.description = description;
    }

    public static List<HelloWorld> find() {
        return mapToMultiple(repo.findQuery(
                () -> "SELECT * FROM helloworld"));
    }


    public HelloWorld fromJson(String json) {
        return (HelloWorld) super.fromJson(json);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    private static List<HelloWorld> mapToMultiple(ResultSet rs) {
        List<HelloWorld> items = new ArrayList<>();

        try {
            while(rs.next()) {
                items.add(mapToSingle(rs));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            // Ignore
        }

        return items;
    }

    private static HelloWorld mapToSingle(ResultSet rs) {
        HelloWorld h = new HelloWorld();

        try {
            h.setId((long) rs.getInt("id"));
            h.setTitle(rs.getString("title"));
            h.setDescription(rs.getString("description"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }

        return h;
    }
}
