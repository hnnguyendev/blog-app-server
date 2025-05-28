package dev.hnnguyen.blog.domain;

import com.google.gson.JsonElement;
import dev.hnnguyen.blog.config.GsonConfiguration;
import dev.hnnguyen.blog.domain.enums.SectionTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "section")
@SQLDelete(sql = "UPDATE section SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = 'false'")
public class Section extends AbstractAuditingEntity<UUID> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", length = 36)
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "section_type", length = 50, nullable = false)
    private SectionTypeEnum sectionType;

    @NotNull
    @Column(name = "position", nullable = false)
    private Integer position;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content", columnDefinition = "json", nullable = false)
    @Convert(converter = GsonConfiguration.JsonElementConverter.class)
    private JsonElement content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
}
