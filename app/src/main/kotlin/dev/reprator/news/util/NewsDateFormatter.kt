package dev.reprator.news.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime


interface NewsDateFormatter {
    fun formatShortDate(instant: Instant): String
    fun formatShortDate(date: LocalDate): String
    fun formatMediumDate(instant: Instant): String
    fun formatMediumDateTime(instant: Instant): String
    fun formatShortTime(localTime: LocalTime): String
    fun formatShortRelativeTime(date: Instant, reference: Instant = Clock.System.now()): String
    fun formatDayOfWeek(dayOfWeek: DayOfWeek): String
}
