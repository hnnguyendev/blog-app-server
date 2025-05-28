package dev.hnnguyen.blog.domain.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RequestCategory {

    private UUID id;

    @NotBlank
    @Size(max = 255)
    private String name;

    private UUID parentId;
}
