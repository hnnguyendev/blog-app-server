package dev.hnnguyen.blog.util;

import java.text.Normalizer;
import java.util.Objects;

public class SlugUtils {

    public static String toSlug(String input) {
        if (Objects.isNull(input)) return "";

        // Normalize Vietnamese characters
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "") // remove accents
            .replaceAll("Đ", "D").replaceAll("đ", "d");

        // Remove all non-alphanumeric characters except spaces and hyphens
        String slug = normalized.replaceAll("[^\\p{Alnum}]+", "-") // replace non-alnum with hyphen
            .replaceAll("[-]+", "-")      // collapse multiple hyphens
            .replaceAll("^-|-$", "")      // trim leading/trailing hyphen
            .toLowerCase();

        return slug;
    }
}

