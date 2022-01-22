package org.hs.hll;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

public final class NewsWithContentSpecification {

    private NewsWithContentSpecification() {}

    public static Specification<NewsWithContent> bookNameEquals(String name) {

        return (root, query, builder) -> {
            return (name == null) ? null : builder.equal(root.get(NewsWithContent_.title), name);
        };
    }

    public static Specification<NewsWithContent> newsIdIn(List<Long> newsIds) {

        return (root, query, builder) -> {
            return (CollectionUtils.isEmpty(newsIds)) ? null
                    : root.get(NewsWithContent_.newsId).in(newsIds);
        };
    }

    public static Specification<NewsWithContent> distinct() {

        return (root, query, builder) -> {
            query.distinct(true);
            return builder.conjunction();
        };
    }

}
