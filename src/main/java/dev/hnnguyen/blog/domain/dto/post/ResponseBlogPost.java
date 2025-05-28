package dev.hnnguyen.blog.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseBlogPost implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;

    private String title;

    private String description;

    private String heroImage;

    private String authorName;

    private String imageUrl;

    private Instant lastUpdated;

    private String slug;
}
