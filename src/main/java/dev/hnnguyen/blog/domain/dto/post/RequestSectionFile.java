package dev.hnnguyen.blog.domain.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSectionFile {

    private String name;

    private String description;

    private String mediaUrl;

    private Integer position;
}
