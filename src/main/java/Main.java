import model.HelloWorld;
import no.fredrfli.http.Server;
import no.fredrfli.http.controller.StaticController;
import no.fredrfli.http.db.BaseDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.*;
import java.util.List;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 07.05.2017
 */
public class Main extends Server {

    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        try {
            sessionFactory = buildSessionFactory();
        } catch(CommandAcceptanceException cme) {
            System.err.println(cme.getMessage());
        }

        Main app = new Main();
        app.port = 8080;

        try {
            app.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();


        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
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
