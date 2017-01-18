package com.icarapovic.metronome.utils;

public class FileUtils {

    public static final int BASE_KB = 1024;
    public static final int BASE_MB = BASE_KB * BASE_KB;

    private FileUtils() {
    }

    /**
     * Properly format file size
     *
     * @param fileSize File size in bytes
     * @return String representation of the size (B, kB, MB)
     */
    public static String formatFileSize(long fileSize) {
        StringBuilder sb = new StringBuilder();

        if (fileSize / BASE_KB < 1) {
            sb.append(fileSize).append(" B");
        } else if (fileSize / BASE_KB >= 1 && fileSize / BASE_MB < 1) {
            sb.append(fileSize / BASE_KB);
            if (fileSize % BASE_KB > 0 && fileSize % BASE_KB < 10) {
                sb.append(".0").append(fileSize % BASE_KB);
            } else if (fileSize % BASE_KB >= 10) {
                sb.append(".").append((fileSize % BASE_KB));
            }
            sb.append(" kB");
        } else if (fileSize / BASE_MB >= 1) {
            sb.append(fileSize / BASE_MB);
            if (fileSize % BASE_MB > 0 && fileSize % BASE_MB < 10) {
                sb.append(".0").append(fileSize % BASE_MB);
            } else if (fileSize % BASE_MB >= 10) {
                sb.append(".").append((fileSize % BASE_MB));
            }
            sb.append(" MB");
        }
        return sb.toString();
    }
}
