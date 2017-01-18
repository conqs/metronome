package com.icarapovic.metronome;

import com.icarapovic.metronome.utils.FileUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

    @Test
    public void formatSizeBytes() {
        assertEquals("123 B", FileUtils.formatFileSize(123));
    }

    @Test
    public void formatSizeKilobytes() {
        assertEquals("277.64 kB", FileUtils.formatFileSize(277 * 1024 + 64));
    }

    @Test
    public void formatSizeKilobytes2() {
        assertEquals("256 kB", FileUtils.formatFileSize(256 * 1024));
    }

    @Test
    public void formatSizeKilobytes3() {
        assertEquals("277.08 kB", FileUtils.formatFileSize(277 * 1024 + 8));
    }

    @Test
    public void formatSizeMegabytes() {
        assertEquals("6.59 MB", FileUtils.formatFileSize(6 * 1024 * 1024 + 59));
    }

    @Test
    public void formatSizeMegabytes2() {
        assertEquals("10 MB", FileUtils.formatFileSize(10 * 1024 * 1024));
    }

    @Test
    public void formatSizeMegabytes3() {
        assertEquals("125.02 MB", FileUtils.formatFileSize(125 * 1024 * 1024 + 2));
    }
}
