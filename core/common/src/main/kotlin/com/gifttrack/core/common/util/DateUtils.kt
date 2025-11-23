package com.gifttrack.core.common.util

/**
 * Utility functions for date and time operations.
 */
object DateUtils {
    /**
     * Returns the current time in milliseconds since Unix epoch.
     */
    fun currentTimeMillis(): Long = System.currentTimeMillis()

    /**
     * Checks if a timestamp is today.
     */
    fun isToday(timestampMillis: Long): Boolean {
        val now = currentTimeMillis()
        val dayInMillis = 24 * 60 * 60 * 1000
        return (now - timestampMillis) < dayInMillis
    }
}
