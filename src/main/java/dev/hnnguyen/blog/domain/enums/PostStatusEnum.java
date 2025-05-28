package dev.hnnguyen.blog.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PostStatusEnum implements CodeEnum {

    DRAFT("DRAFT", "Draft"),
    PUBLISHED("PUBLISHED", "Published"),
    ARCHIVED("ARCHIVED", "Archived");

    private final String value;

    private final String name;

    @JsonValue
    public String getValue() {
        return value;
    }
}
