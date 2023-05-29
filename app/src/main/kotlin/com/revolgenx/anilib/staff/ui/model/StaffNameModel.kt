package com.revolgenx.anilib.staff.ui.model

data class StaffNameModel(
    val full: String? = null,
    val native: String? = null,
    val alternative: List<String>? = null,
    val alternativeText: String? = native?.let { "$it, " }?.plus(alternative?.joinToString(", ")),
)
