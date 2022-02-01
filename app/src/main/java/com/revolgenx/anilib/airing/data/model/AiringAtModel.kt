package com.revolgenx.anilib.airing.data.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class AiringAtModel(val airingAt: LocalDateTime) {
    val airingTime: String = airingAt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) //20:00
    val airingDay: String = airingAt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val airingDayMedium: String = airingAt.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val airingDateTime: String = airingAt.format(DateTimeFormatter.ofPattern("EE, dd MMM, yyyy, HH:mm"))
}