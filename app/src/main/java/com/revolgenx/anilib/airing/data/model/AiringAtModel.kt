package com.revolgenx.anilib.airing.data.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class AiringAtModel(val airingAt: LocalDateTime) {
    val airingTime: String //20:00
    val airingDay: String
    val airingDayMedium: String
    val airingDateTime: String
    init {
        airingTime = airingAt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        airingDay = airingAt.dayOfWeek.getDisplayName(
            TextStyle.FULL,
            Locale.getDefault()
        )
        airingDayMedium = airingAt.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        airingDateTime = airingAt.format(DateTimeFormatter.ofPattern("EE, dd MMM, yyyy, HH:mm"))
    }
}