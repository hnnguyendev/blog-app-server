package dev.hnnguyen.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.hnnguyen.blog.domain.dto.post.ResponsePost;
import dev.hnnguyen.blog.domain.enums.PostStatusEnum;
import dev.hnnguyen.blog.domain.listener.SlugListenerEntity;
import dev.hnnguyen.blog.domain.listener.SlugSource;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@EntityListeners(SlugListenerEntity.class)
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = 'false'")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "search_post",
            classes = @ConstructorResult(
                targetClass = ResponsePost.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "title", type = String.class),
                    @ColumnResult(name = "heroImage", type = String.class),
                    @ColumnResult(name = "category", type = String.class),
                    @ColumnResult(name = "authorName", type = String.class),
                    @ColumnResult(name = "lastUpdated", type = Instant.class),
                    @ColumnResult(name = "status", type = PostStatusEnum.class)
                }
            )
        ),
    }
)
public class Post extends AbstractAuditingEntity<UUID> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", length = 36)
    private UUID id;

    @SlugSource
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private PostStatusEnum status;

    @Column(name = "description")
    private String description;

    @Column(name = "heroImage")
    private String heroImage;

    @NotNull
    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "published_date")
    private Instant publishedDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "post_tag",
        joinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")}
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<Section> sections = new HashSet<>();
}
