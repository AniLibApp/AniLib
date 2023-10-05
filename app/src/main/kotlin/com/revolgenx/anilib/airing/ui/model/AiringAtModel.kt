package com.revolgenx.anilib.airing.ui.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

data class AiringAtModel(val airingAt: LocalDateTime) {
    private val airingTime24: String =
        airingAt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) //20:00
    private val airingTime12: String =
        airingAt.toLocalTime().format(DateTimeFormatter.ofPattern("h:mm a")) //12:00
    fun airingTime(is12hrFormat: Boolean) = if (is12hrFormat) airingTime12 else airingTime24

    val airedAt = airingAt.format(DateTimeFormatter.ofPattern("dd MMM, yyyy, HH:mm"))

    val airingDay: String = airingAt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val airingDayMedium: String =
        airingAt.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val airingDateTime: String =
        airingAt.format(DateTimeFormatter.ofPattern("EE, dd MMM, yyyy, HH:mm"))
}