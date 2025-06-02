package dev.hnnguyen.blog.domain.dto.post;

import dev.hnnguyen.blog.domain.enums.SectionTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RequestSection {

    private UUID id;

    @NotNull
    private SectionTypeEnum sectionType;

    @NotNull
    private Integer position;

    private String heading;

    private String description;

    private String photoCredit;

    private String mediaUrl;

    private String textContent;

    private List<RequestSectionFile> sectionFiles;
}
