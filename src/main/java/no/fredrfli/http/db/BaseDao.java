package no.fredrfli.http.db;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 11.05.2017
 */
public class BaseDao<T> {
    private SessionFactory sessionFactory;
    private Class<T> type;

    public BaseDao(SessionFactory sessionFactory, Class<T> type) {
        Objects.requireNonNull(
                sessionFactory,
                "Requires a SessionFactory to connect to a database");
        Objects.requireNonNull(
                type,
                "Requires the model-type, to handle database queries");
        this.sessionFactory = sessionFactory;
        this.type = type;
    }

    public List<T> find() {
//        Session session = sessionFactory.openSession();
//
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<T> criteria = builder.createQuery(type);
//        Root<T> rootEntry = criteria.from(type);
//
//        CriteriaQuery<T> all = criteria.select(rootEntry);
//
//        TypedQuery<T> allQuery = session.createQuery(all);
//        List<T> hw = allQuery.getResultList();
//
//        session.close();

        List<T> matches = findWhere((cb, root) -> (t) -> true);

        return matches;
    }

    /**
     * Warning: Because of hibernates setup, this
     * is a hard to read method.
     *
     * In essence,
     *
     * */
    public List<T> findWhere(PredicateBuilder<T> pb) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(type);

        Root<T> rootEntry = query.from(type);

        CriteriaQuery<T> criteriaQuery = query.select(rootEntry);
        ParameterExpression<Integer> p = builder.parameter(Integer.class);

        criteriaQuery.where(
                builder.equal(rootEntry.get("title"), p)
        );

        TypedQuery<T> typedQuery = session.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    public T findById(long id) {

        return null;
    }

    public T create(T obj) {
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        session.save(obj);

        session.getTransaction().commit();

        session.close();

        return obj;
    }

    public T update(T obj) {

        return obj;
    }

    public T destroy(T obj) {

        return obj;
    }


}
