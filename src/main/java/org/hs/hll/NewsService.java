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

        String title = parameters.getOrDefault("title", null);
        Specification<News> specification =
                Specification.where(NewsSpecification.titleEquals(title))
                        .and(NewsSpecification.distinct());

        Page<News> pagedNews = newsRepository.findAll(specification, page);
        List<Long> newsIds = pagedNews.getContent().stream().map(news -> news.getNewsId())
                .collect(Collectors.toList());

        Specification<NewsWithContent> specification2 =
                Specification.where(NewsWithContentSpecification.newsIdIn(newsIds));
        List<NewsWithContent> newsWithContentList =
                newsWithContentRepository.findAll(specification2);

        return new PageImpl<NewsWithContent>(newsWithContentList, pagedNews.getPageable(),
                pagedNews.getTotalElements());

    }

    @Transactional(readOnly = true)
    public Page<News> getNews(Map<String, String> parameters, Pageable page) {

        String title = parameters.getOrDefault("title", null);
        Specification<News> specification = Specification
                .where(NewsSpecification.titleEquals(title)).and(NewsSpecification.distinct());

        return newsRepository.findAll(specification, page);
    }

}
