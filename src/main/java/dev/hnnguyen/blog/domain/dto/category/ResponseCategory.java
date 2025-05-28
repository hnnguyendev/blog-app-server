package dev.hnnguyen.blog.domain.dto.category;

import dev.hnnguyen.blog.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;

    private String name;

    private String slug;

    public ResponseCategory(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.slug = category.getSlug();
    }
}
