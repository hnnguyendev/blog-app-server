package dev.hnnguyen.blog.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CombinationTypeEnum implements CodeEnum {

    AND("AND", "AND"),
    OR("OR", "OR");

    private final String value;

    private final String name;

    @JsonValue
    public String getValue() {
        return value;
    }
}
