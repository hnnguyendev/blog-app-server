package dev.hnnguyen.blog.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class SlugUtils {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

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

    public static String slugifyFilename(String fileName) {
        if (Objects.isNull(fileName) || fileName.isBlank()) {
            throw new IllegalArgumentException("File name cannot be null or blank");
        }

        fileName = fileName.trim();

        // Split extension
        int lastDot = fileName.lastIndexOf('.');
        String namePart = (lastDot > 0) ? fileName.substring(0, lastDot) : fileName;
        String extension = (lastDot > 0) ? fileName.substring(lastDot).toLowerCase() : "";

        // Normalize Vietnamese characters to ASCII
        String normalized = Normalizer.normalize(namePart, Normalizer.Form.NFD);
        String ascii = normalized.replaceAll("\\p{M}", ""); // Remove diacritics
        ascii = ascii.replaceAll("đ", "d").replaceAll("Đ", "d");

        // Slugify
        String slug = WHITESPACE.matcher(ascii).replaceAll("-");
        slug = NON_LATIN.matcher(slug).replaceAll("");
        slug = slug.toLowerCase(Locale.ENGLISH).replaceAll("-+", "-").replaceAll("^-|-$", "");

        return slug + extension;
    }
}

