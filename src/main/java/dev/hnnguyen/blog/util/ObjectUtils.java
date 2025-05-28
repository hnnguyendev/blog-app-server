package dev.hnnguyen.blog.util;

import dev.hnnguyen.blog.constant.AppConstants;
import dev.hnnguyen.blog.domain.dto.common.RequestSqlCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ObjectUtils extends org.springframework.util.ObjectUtils {

    public static List<RequestSqlCondition> convertSortParams(List<RequestSqlCondition> requests, HashMap<String, String> sortMap) {
        if (Objects.isNull(requests)) {
            return null;
        }
        var timezone = sortMap.get(AppConstants.TIMEZONE_KEY);
        return requests.stream().peek(element -> {
            if (!element.isGroup()) {
                var sortExist = sortMap.get(element.getColumn());
                if (Objects.nonNull(sortExist)) {
                    element.setColumn(sortExist);
                }
            } else {
                var newSubCon = element.getSubConditions().stream().peek(sub -> {
                    var sortExist = sortMap.get(element.getColumn());
                    if (Objects.nonNull(sortExist)) {
                        sub.setColumn(sortExist);
                    }
                }).toList();
                element.setSubConditions(newSubCon);
            }
            if (Objects.nonNull(timezone)) {
                element.setTimezone(timezone);
            }
        }).toList();
    }
}
