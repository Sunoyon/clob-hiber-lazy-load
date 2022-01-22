package org.hs.hll;

import javax.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public final class NewsSpecification {

    private NewsSpecification() {}

    public static Specification<News> titleEquals(String title) {

        return (root, query, builder) -> {
            return (title == null) ? null : builder.equal(root.get(News_.title), title);
        };
    }

    public static Specification<News> distinct() {

        return (root, query, builder) -> {
            root.fetch(News_.publisher, JoinType.LEFT);
            query.distinct(true);
            return builder.conjunction();
        };
    }

}
