package dev.hnnguyen.blog.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SectionTypeEnum implements CodeEnum {

    IMAGE("IMAGE", "Image"),
    TEXT("TEXT", "Text"),
    VIDEO("VIDEO", "Video"),
    AUDIO("AUDIO", "Audio"),
    FILE("FILE", "File");

    private final String value;

    private final String name;

    @JsonValue
    public String getValue() {
        return value;
    }
}
