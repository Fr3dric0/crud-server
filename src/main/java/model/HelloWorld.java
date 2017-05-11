package model;

import no.fredrfli.http.model.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 10.05.2017
 */
@Entity
@Table(name = "HelloWorld")
public class HelloWorld extends Model {

    @Column(name = "`title`")
    private String title;

    @Column(name = "`description`")
    private String description;

    public HelloWorld() {}

    public HelloWorld(HelloWorldBuilder builder) {
        this.title = builder.title;
        this.description = builder.description;
    }

    public HelloWorld(String title, String description) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);

        this.title = title;
        this.description = description;
    }

    public HelloWorld fromJson(String json) {
        return (HelloWorld) super.fromJson(json);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static class HelloWorldBuilder {
        private String title;
        private String description;

        public HelloWorldBuilder(String title) {
            this.title = title;
        }

        public HelloWorldBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public HelloWorld build() {
            return new HelloWorld(this);
        }
    }
}
