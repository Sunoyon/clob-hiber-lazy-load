# Lazy loading clob fields of Entites using Hibernate

This is an efficient approach to load clob fields lazily of an Entity. Using this approach, 

  
  - distinct operation over the entities can be executed.
  - avoid (n + 1) queries while fetching the clob fields. 
  
  
## How to

  1. Maintain two separate Entities (one is with clob field, another one is without clob field) pointing to the same database table. 
  2. In service layer, fetch the entities according to the search parameters without clob field. As clob field is not considering in this entity, We can run distinct operation if it is needed.
  3. We can get the ids of the entities fetched in step 2. Afterwards, we can fetch the entities with clob field by the ids in the same persistent context.


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

