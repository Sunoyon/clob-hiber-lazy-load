package org.hs.hll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "news")
@Getter
@Setter
public class NewsWithContent extends BaseNews {

    @Column(name = "content", columnDefinition = "CLOB")
    private String content;
}
