package com.jozefv.whatsnew.feat_articles.domain.articles

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

// TODO: This should be improved if needed
fun String.toZonedDateTime(): ZonedDateTime {
    // API Currently returns pubDate as a 12h string and there are challenges related to the conversion
    // Example API return format: "2025-03-09 06:00:40", to compile with ISO 8601 - standard format for date-time values
    // replace " " with T
    val modifiedText = this.replace(" ", "T")
    val localDateTime = LocalDateTime.parse(modifiedText)
    // Convert to UTC offset
    val offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC)
    // Zoned date time
    return offsetDateTime.atZoneSameInstant(ZoneId.systemDefault())
}
