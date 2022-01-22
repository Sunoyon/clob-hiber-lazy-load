package org.hs.hll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsWithContentRepository
        extends JpaRepository<NewsWithContent, Long>, JpaSpecificationExecutor<NewsWithContent> {

}
