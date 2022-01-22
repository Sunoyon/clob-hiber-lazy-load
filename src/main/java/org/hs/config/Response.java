package org.hs.config;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response<T> {

    private T data;
    private MetaResponse meta;

    public static <U> Response<List<U>> paged(Page<U> pagedData) {
        PageResponse pageResponse = null;
        Pageable pageable = pagedData.getPageable();
        if (pageable.isPaged()) {
            pageResponse = PageResponse.builder().page(pageable.getPageNumber())
                    .size(pageable.getPageSize()).totalPages(pagedData.getTotalPages())
                    .totalElements(pagedData.getTotalElements()).build();
        }

        return Response.<List<U>>builder().data(pagedData.getContent())
                .meta(MetaResponse.builder().page(pageResponse).build()).build();
    }
}