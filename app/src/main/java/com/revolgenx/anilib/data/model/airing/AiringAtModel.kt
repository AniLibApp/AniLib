package com.revolgenx.anilib.data.model.airing

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class AiringAtModel(val airingAt: LocalDateTime) {
    val airingTime: String //20:00
    val airingDay: String
    val airingDate: String
    init {
        airingTime = airingAt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        airingDay = airingAt.dayOfWeek.getDisplayName(
            TextStyle.FULL,
            Locale.getDefault()
        )
        airingDate = airingAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }
}