package dev.hnnguyen.blog.domain.dto.post;

import dev.hnnguyen.blog.domain.enums.PostStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RequestPost {

    private UUID id;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotNull
    private PostStatusEnum status;

    private String description;

    @NotNull
    private UUID category;

    @NotEmpty
    private List<UUID> tags;

    @Size(max = 255)
    private String heroImage;

    private List<RequestSection> sections;
}
