package dev.hnnguyen.blog.util;

public class FileUtils {

    /**
     * Define the calculation constant of GB
     */
    private static final int GB = 1024 * 1024 * 1024;

    /**
     * Define the calculation constant of MB
     */
    private static final int MB = 1024 * 1024;

    /**
     * Define the calculation constant of KB
     */
    private static final int KB = 1024;

    public static String getExtensionName(final String filename) {
        if ((filename != null) && (!filename.isEmpty())) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static boolean checkSize(final long maxSize, final long size) {
        int len = MB;
        return size > (maxSize * len);
    }
}
