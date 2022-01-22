package org.hs.hll;

import java.util.List;
import java.util.Map;
import org.hs.config.MetaResponse;
import org.hs.config.PageResponse;
import org.hs.config.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping(value = "/news-with-content", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Response<List<NewsWithContent>> getNewsWithContent(
            @RequestParam Map<String, String> parameters,
            final Pageable pageable) {
        Page<NewsWithContent> pagedNewsWithContent =
                newsService.getNewsWithContent(parameters, pageable);
        PageResponse pageResponse = PageResponse.builder()
            .page(pagedNewsWithContent.getNumber())
            .size(pagedNewsWithContent.getSize())
            .totalPages(pagedNewsWithContent.getTotalPages())
            .totalElements(pagedNewsWithContent.getTotalElements()).build();
        
        
        MetaResponse metaResponse = MetaResponse.builder()
                .error(null)
                .page(pageResponse)
                .build();

        return Response.<List<NewsWithContent>>builder()
                .data(pagedNewsWithContent.getContent())
                .meta(metaResponse).build();
    }

    @GetMapping(value = "/news", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Response<List<News>> getNews(@RequestParam Map<String, String> parameters,
            final Pageable pageable) {
        Page<News> pagedNews = newsService.getNewsWithoutContent(parameters, pageable);

        PageResponse pageResponse = PageResponse.builder()
                .page(pagedNews.getNumber())
                .size(pagedNews.getSize())
                .totalPages(pagedNews.getTotalPages())
                .totalElements(pagedNews.getTotalElements())
                .build();


        MetaResponse metaResponse = MetaResponse.builder().error(null).page(pageResponse).build();
        return Response.<List<News>>builder()
                .data(pagedNews.getContent())
                .meta(metaResponse)
                .build();
    }

}
