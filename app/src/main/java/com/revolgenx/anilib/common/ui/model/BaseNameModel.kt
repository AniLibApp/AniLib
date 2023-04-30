package com.revolgenx.anilib.common.ui.model

abstract class BaseNameModel(
    val full: String? = null,
    val native: String? = null,
    val alternative: List<String>? = null,
    val first: String? = null,
    val last: String? = null,
    val middle: String? = null,
    val userPreferred: String? = null
)