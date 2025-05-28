package dev.hnnguyen.blog.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final String EMPTY_STRING = "";

    public static String wildcards(String searchKeyword) {
        if (Objects.isNull(searchKeyword)) return null;
        if (isBlank(searchKeyword)) return EMPTY_STRING;
        searchKeyword = searchKeyword.replace("%", "\\%");
        searchKeyword = searchKeyword.replace("_", "\\_");
        return searchKeyword.toLowerCase().trim();
    }
}
