package dev.hnnguyen.blog.domain.dto.common;


import dev.hnnguyen.blog.domain.enums.CombinationTypeEnum;
import dev.hnnguyen.blog.domain.enums.ConditionTypeEnum;
import dev.hnnguyen.blog.domain.enums.FilterTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestSqlCondition {

    private String column;

    private ConditionTypeEnum conditionType;

    private Object value;

    private FilterTypeEnum filterType;

    private List<String> values;

    private CombinationTypeEnum combinationType;

    private String timezone;

    private List<RequestSqlCondition> subConditions;

    public boolean isGroup() {
        return subConditions != null;
    }
}
