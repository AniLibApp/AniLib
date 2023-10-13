package com.revolgenx.anilib.common.ui.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DateModel(
    val localDate: LocalDate,
    val formattedDate: String = localDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
)