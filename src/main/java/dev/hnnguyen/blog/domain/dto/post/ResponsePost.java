package dev.hnnguyen.blog.domain.dto.post;

import dev.hnnguyen.blog.domain.enums.PostStatusEnum;
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
public class ResponsePost implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;

    private String title;

    private String heroImage;

    private String category;

    private String authorName;

    private Instant lastUpdated;

    private PostStatusEnum status;
}
