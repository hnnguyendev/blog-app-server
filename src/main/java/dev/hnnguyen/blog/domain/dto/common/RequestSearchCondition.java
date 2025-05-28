package dev.hnnguyen.blog.domain.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestSearchCondition {

    private List<RequestSqlCondition> searchConditions;

    private Integer page;

    private Integer size;

    private String sort;

    private String timezone;
}
