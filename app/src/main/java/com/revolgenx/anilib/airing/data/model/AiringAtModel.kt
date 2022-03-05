package com.revolgenx.anilib.airing.data.model

import com.revolgenx.anilib.common.preference.isAiring12hrFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class AiringAtModel(val airingAt: LocalDateTime) {
    private val airingTime24: String =
        airingAt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) //20:00
    private val airingTime12: String =
        airingAt.toLocalTime().format(DateTimeFormatter.ofPattern("h:mm a")) //12:00

    val airingTime = if (isAiring12hrFormat) airingTime12 else airingTime24

    val airingDay: String = airingAt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val airingDayMedium: String =
        airingAt.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val airingDateTime: String =
        airingAt.format(DateTimeFormatter.ofPattern("EE, dd MMM, yyyy, HH:mm"))
}