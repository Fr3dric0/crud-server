package no.fredrfli.http.db;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import java.util.function.Predicate;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 11.05.2017
 */
public interface PredicateBuilder<T> {
    java.util.function.Predicate build(CriteriaBuilder criteriaBuilder, Root<T> root);
}
