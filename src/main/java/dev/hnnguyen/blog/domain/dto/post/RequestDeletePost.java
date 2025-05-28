package dev.hnnguyen.blog.domain.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RequestDeletePost {

    private List<UUID> ids;
}
