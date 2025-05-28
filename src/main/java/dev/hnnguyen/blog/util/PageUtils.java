package dev.hnnguyen.blog.util;

import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

public class PageUtils {

    private PageUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Sort createSort(String sortParams) {
        if (ObjectUtils.isEmpty(sortParams)) {
            sortParams = StringUtils.EMPTY;
        }
        Sort sort = Sort.unsorted();
        String[] parts = sortParams.split(",");
        if (parts.length == 2) {
            String field = parts[0];
            Sort.Direction direction = Sort.Direction.fromString(parts[1]);
            sort = sort.and(Sort.by(direction, field));
        }
        return sort;
    }
}
