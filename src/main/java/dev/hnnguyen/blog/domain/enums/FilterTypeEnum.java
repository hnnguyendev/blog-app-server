package dev.hnnguyen.blog.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FilterTypeEnum implements CodeEnum {

    NUMBER("number", "NUMBER"),
    TEXT("text", "TEXT"),
    DATE("date", "DATE");

    private final String value;

    private final String name;

    @JsonValue
    public String getValue() {
        return value;
    }
}
