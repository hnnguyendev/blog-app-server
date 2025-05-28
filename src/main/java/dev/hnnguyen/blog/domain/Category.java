package dev.hnnguyen.blog.domain;

import dev.hnnguyen.blog.domain.listener.SlugListenerEntity;
import dev.hnnguyen.blog.domain.listener.SlugSource;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serial;
import java.io.Serializable;
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
@Table(name = "category")
@SQLDelete(sql = "UPDATE category SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = 'false'")
public class Category extends AbstractAuditingEntity<UUID> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", length = 36)
    private UUID id;

    @SlugSource
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "parent_id", length = 36)
    private UUID parentId;

    @OneToMany(mappedBy = "category")
    private Set<Post> posts = new HashSet<>();
}
