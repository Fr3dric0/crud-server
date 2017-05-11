package no.fredrfli.http.model;

import com.google.gson.Gson;

import javax.persistence.*;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
@MappedSuperclass
public class Model {
    private static Gson gson = new Gson();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "`id`")
    private long id;

    @Version
    private long version;

    public long getId() { return id; }
    public long getVersion() { return version; }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        return gson.toJson(this, this.getClass());
    }

    public Object fromJson(String json) {
        return gson.fromJson(json, this.getClass());
    }
}
