package dev.hnnguyen.blog.domain.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RequestTag {

    private UUID id;

    @NotBlank
    @Size(max = 255)
    private String name;
}
