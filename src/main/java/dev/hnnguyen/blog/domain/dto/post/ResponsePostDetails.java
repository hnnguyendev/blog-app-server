package dev.hnnguyen.blog.domain.dto.post;

import dev.hnnguyen.blog.domain.enums.PostStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponsePostDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;

    private String title;

    private PostStatusEnum status;

    private String description;

    private UUID category;

    private List<UUID> tags;

    private String heroImage;

    private List<ResponseSection> sections;

    private String authorName;

    private String imageUrl;

    private Instant lastUpdated;

    public ResponsePostDetails(UUID id, String title, PostStatusEnum status, String description, UUID category, String heroImage, String authorName, String imageUrl, Instant lastUpdated) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.description = description;
        this.category = category;
        this.heroImage = heroImage;
        this.authorName = authorName;
        this.imageUrl = imageUrl;
        this.lastUpdated = lastUpdated;
    }
}
