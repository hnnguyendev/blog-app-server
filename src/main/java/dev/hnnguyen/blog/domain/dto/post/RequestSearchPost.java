package dev.hnnguyen.blog.domain.dto.post;

import dev.hnnguyen.blog.domain.dto.common.RequestSearchCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSearchPost extends RequestSearchCondition {

    private String searchKeyword;
}
