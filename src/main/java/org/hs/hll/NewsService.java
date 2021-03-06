package org.hs.hll;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private NewsWithContentRepository newsWithContentRepository;


    @Transactional(readOnly = true)
    public Page<NewsWithContent> getNewsWithContent(Map<String, String> parameters, Pageable page) {

        // Fetch the news (without content clob) using the search parameters
        // run distinct if we want to fetch distinct news
        String title = parameters.getOrDefault("title", null);
        Specification<News> distinctNewsSpecification =
                Specification.where(NewsSpecification.titleEquals(title))
                             .and(NewsSpecification.distinct());
        Page<News> pagedNews = newsRepository.findAll(distinctNewsSpecification, page);

        // get the news ids which are fetched
        List<Long> newsIds = pagedNews.getContent().stream().map(news -> news.getNewsId())
                                      .collect(Collectors.toList());

        // fetch the news (with content clob) of the news ids
        Specification<NewsWithContent> newsWithContentSpecification =
                Specification.where(NewsWithContentSpecification.newsIdIn(newsIds));
        List<NewsWithContent> newsWithContent =
                newsWithContentRepository.findAll(newsWithContentSpecification);

        return new PageImpl<NewsWithContent>(newsWithContent, pagedNews.getPageable(),
                pagedNews.getTotalElements());

    }

    @Transactional(readOnly = true)
    public Page<News> getNewsWithoutContent(Map<String, String> parameters, Pageable page) {

        String title = parameters.getOrDefault("title", null);
        Specification<News> specification = Specification
                .where(NewsSpecification.titleEquals(title)).and(NewsSpecification.distinct());

        return newsRepository.findAll(specification, page);
    }

}
