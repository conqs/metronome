package com.icarapovic.metronome;

import com.icarapovic.metronome.utils.MediaUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MediaUtilsTest {

    @Test
    public void formatTimeSecondsSingle() {
        assertEquals("00:04", MediaUtils.formatDuration(4 * 1000));
    }

    @Test
    public void formatTimeSecondsDouble() {
        assertEquals("00:44", MediaUtils.formatDuration(44 * 1000));
    }

    @Test
    public void formatTimeMinutesSingle() {
        assertEquals("09:04", MediaUtils.formatDuration(9 * 60 * 1000 + 4 * 1000));
    }

    @Test
    public void formatTimeMinutesDouble() {
        assertEquals("26:47", MediaUtils.formatDuration(26 * 60 * 1000 + 47 * 1000));
    }

    @Test
    public void formatTimeHoursSingle() {
        assertEquals("2:03:04", MediaUtils.formatDuration(2 * 60 * 60 * 1000 + 3 * 60 * 1000 + 4 * 1000));
    }

    @Test
    public void formatTimeHoursDouble() {
        assertEquals("10:09:18", MediaUtils.formatDuration(10 * 60 * 60 * 1000 + 9 * 60 * 1000 + 18 * 1000));
    }


}